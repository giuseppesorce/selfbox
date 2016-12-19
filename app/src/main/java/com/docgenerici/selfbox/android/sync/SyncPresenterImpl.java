package com.docgenerici.selfbox.android.sync;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.async.XmlTaskParse;
import com.docgenerici.selfbox.comm.ApiInteractor;
import com.docgenerici.selfbox.comm.storage.Environment;
import com.docgenerici.selfbox.config.SelfBoxConstants;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.MedicalList;
import com.docgenerici.selfbox.models.SyncContent;
import com.docgenerici.selfbox.models.contents.Folder;
import com.docgenerici.selfbox.models.farmacia.Farmacia;
import com.docgenerici.selfbox.models.medico.Medico;
import com.docgenerici.selfbox.models.persistence.ConfigurationApp;
import com.docgenerici.selfbox.models.persistence.InfoApp;
import com.docgenerici.selfbox.models.products.Formulazione;
import com.docgenerici.selfbox.models.products.Prodotto;
import com.docgenerici.selfbox.models.products.Product;
import com.docgenerici.selfbox.models.products.Risorsa;
import com.docgenerici.selfbox.models.products.RisorsaDb;
import com.docgenerici.selfbox.models.products.Scheda;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class SyncPresenterImpl implements SyncPresenter {
    private ApiInteractor apiInteractor;
    private SyncView view;
    private ArrayList<SyncContent> syncContents;
    private HashMap<Integer, Boolean> allDownloads = new HashMap<>();


    public SyncPresenterImpl(ApiInteractor loginInteractor) {
        this.apiInteractor = loginInteractor;
    }

    @Override
    public void setView(BaseView view) {
        if (!(view instanceof SyncView)) {
            throw new IllegalArgumentException("View must extend SyncPresenter.View");
        }
        this.view = (SyncView) view;
    }

    @Override
    public void setup() {
        view.setup();
    }

    @Override
    public void startSync() {
        Hawk.put("contentsServiceDestroy", false);
        Hawk.put("productServiceDestroy", false);
        view.onStartSync();
        allDownloads.put(SelfBoxConstants.ContentSyncType.PERSONAL, true);
        allDownloads.put(SelfBoxConstants.ContentSyncType.LOGS, true);
        allDownloads.put(SelfBoxConstants.ContentSyncType.ANAGRAFICHE, false);
        allDownloads.put(SelfBoxConstants.ContentSyncType.CONTENTS, false);
        allDownloads.put(SelfBoxConstants.ContentSyncType.PRODUCTS, false);
        checkEndSync();

        SyncContent syncContent = null;
        syncContent = getContentByType(SelfBoxConstants.ContentSyncType.PERSONAL);
        if (syncContent != null) {
            syncContent.setPercentage(100);
        }
        syncContent = getContentByType(SelfBoxConstants.ContentSyncType.LOGS);
        if (syncContent != null) {
            syncContent.setPercentage(100);
        }
        syncContent = getContentByType(SelfBoxConstants.ContentSyncType.ANAGRAFICHE);
        int percentsgeRnd = getRandom(1, 20);
        if (syncContent != null) {
            syncContent.setPercentage(percentsgeRnd);
        }
        view.updatePercentage();
        getAllMedicalData();
        getProduct();
        getAllContents();

    }

    @Override
    public void stopSync() {
        Hawk.put("contentsServiceDestroy", true);
        Hawk.put("productServiceDestroy", true);
        view.onStopSync();
    }

    @Override
    public void onSyncMessage(int type, int percentage, String message) {


        SyncContent syncContent = getContentByType(type);
        if (syncContent != null) {
            syncContent.setPercentage(percentage);
            if (message.equalsIgnoreCase("end")) {
                allDownloads.put(type, true);
                checkEndSync();
            }
        }
        view.updatePercentage();
    }

    private void checkEndSync() {
        boolean bSycronyzed = true;
        Iterator<Integer> itr2 = allDownloads.keySet().iterator();
        while (itr2.hasNext()) {
            Integer key = itr2.next();
            boolean downloadOk = allDownloads.get(key);
            if (!downloadOk) {
                bSycronyzed = false;
                break;
            }
        }
        if (bSycronyzed) {
            final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
            ConfigurationApp configurationApp = realm.where(ConfigurationApp.class).findFirst();
            realm.beginTransaction();
            if (configurationApp == null) {
                configurationApp = new ConfigurationApp();
            }
            configurationApp.setSyncronized(true);
            realm.copyToRealmOrUpdate(configurationApp);
            realm.commitTransaction();
            view.onStopSync();
            view.gotoHome();
        }
    }

    private SyncContent getContentByType(int type) {
        getContents();
        SyncContent content = null;
        for (SyncContent syncContent : syncContents) {
            if (syncContent.getType() == type) {
                content = syncContent;
                break;
            }
        }
        return content;
    }

    @Override
    public ArrayList<SyncContent> getContents() {
        if (syncContents == null || syncContents.size() <= 0) {
            syncContents = new ArrayList<>();
            syncContents.add(new SyncContent("Contenuti", 0, SelfBoxConstants.ContentSyncType.CONTENTS));
            syncContents.add(new SyncContent("Anagrafiche", 0, SelfBoxConstants.ContentSyncType.ANAGRAFICHE));
            syncContents.add(new SyncContent("Logs", 0, SelfBoxConstants.ContentSyncType.LOGS));
            syncContents.add(new SyncContent("Informazioni personali", 0, SelfBoxConstants.ContentSyncType.PERSONAL));
            syncContents.add(new SyncContent("Catalogo prodotti", 0, SelfBoxConstants.ContentSyncType.PRODUCTS));
        }

        return syncContents;
    }


    private void getProduct() {
        Environment environment = SelfBoxApplicationImpl.appComponent.environment();
        environment.setBaseUrl(SelfBoxConstants.pathProduct);
        apiInteractor.getProduct("")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        try {
                            parseXML(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        Dbg.p("CALL ERRORE getProduct: " + throwable.getLocalizedMessage());

                    }
                });


    }

    private void parseXML(String xmlString) {
        XmlTaskParse parseTask = new XmlTaskParse(onSyncData);
        parseTask.execute(xmlString);

    }

    private OnSyncData onSyncData = new OnSyncData() {

        @Override
        public void onXmlParsed(ArrayList<Prodotto> products) {
            Dbg.p("onXmlParsed");
            Realm realm = Realm.getDefaultInstance();
            try {
                realm.beginTransaction();
                for (int i = 0; i < products.size(); i++) {
                    Prodotto prodotto = products.get(i);
                    Scheda scheda = new Scheda();
                    Risorsa risorsaScheda = prodotto.risorse.get(0);
                    scheda.published = risorsaScheda.published;
                    //  if (!risorsaScheda.uri.equalsIgnoreCase("/pdfxcode/")) {
                    scheda.setUri(risorsaScheda.uri);
                    //}
                    scheda.status = risorsaScheda.status;
                    scheda.filename = risorsaScheda.filename;
                    scheda.tipo = risorsaScheda.tipo;
                    ArrayList<Formulazione> formulazioni = prodotto.formulazioni;
                    for (int j = 0; j < formulazioni.size(); j++) {

                        Product product = new Product();
                        product.setNome(prodotto.nome);
                        product.setScheda(scheda);
                        product.denominazione_en = formulazioni.get(j).denominazione_en;
                        product.denominazione_it = formulazioni.get(j).denominazione_it;
                        product.rcp = formulazioni.get(j).risorse.get(0).uri;
                        product.setFilename(formulazioni.get(j).risorse.get(0).filename);
                        product.originatore = formulazioni.get(j).originatore;
                        product.noFCDL = formulazioni.get(j).noFCDL;
                        product.classeSnn = formulazioni.get(j).classeSSN;
                        product.setAic(formulazioni.get(j).aic);
                        product.categoria_farmacologica = prodotto.categoria_farmacologica;
                        if (prodotto.risorse != null) {
                            Risorsa risorsaProd = prodotto.risorse.get(0);
                            product.risorsa = new RisorsaDb();
                            product.risorsa.filename = risorsaProd.filename;
                            product.risorsa.published = risorsaProd.published;
                            product.risorsa.status = risorsaProd.status;
                            product.risorsa.published = risorsaProd.published;
                            // if (!risorsaProd.uri.equalsIgnoreCase("/pdfxcode/")) {
                            product.risorsa.uri = risorsaProd.uri;
                            //}

                        }
                        if (formulazioni.get(j).aic.equalsIgnoreCase("033551021")) {
                            Dbg.p("XML: " + formulazioni.get(j).risorse.get(0).uri);
                            Dbg.p("XML: " + product.rcp);
                            Dbg.p("XML: " + product.getScheda().getUri());

                        }
                        realm.copyToRealmOrUpdate(product);
                    }


                }
                realm.commitTransaction();
            } finally {
                realm.close();
            }
            startSyncProduct();

        }
    };

    private void startSyncProduct() {
        view.startProductService();
    }

    private void getAllMedicalData() {
        SyncContent syncContent = getContentByType(SelfBoxConstants.ContentSyncType.ANAGRAFICHE);
        int percentsgeRnd = getRandom(20, 50);
        if (syncContent != null) {
            syncContent.setPercentage(percentsgeRnd);
        }
        String isf = getRepcode();
        if (!isf.isEmpty()) {
            apiInteractor.getallMedical(isf)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<MedicalList>() {
                        @Override
                        public void call(MedicalList medialresponse) {

                            persistenceMedicalList(medialresponse);


                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                            Dbg.p("CALL ERRORE getAllMedicalData");

                        }
                    });

        } else {
            view.showCodeError("Errore nel caricamento dei dati");
        }
    }

    private void persistenceMedicalList(MedicalList medicalList) {
        SyncContent syncContent = getContentByType(SelfBoxConstants.ContentSyncType.ANAGRAFICHE);
        if (syncContent != null) {
            syncContent.setPercentage(100);
        }
        view.updatePercentage();
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        realm.beginTransaction();
        List<Medico> medici = medicalList.medici;
        for (int i = 0; i < medici.size(); i++) {
            realm.copyToRealmOrUpdate(medici.get(i));
        }
        List<Farmacia> farmacie = medicalList.farmacie;
        for (int i = 0; i < farmacie.size(); i++) {
            realm.copyToRealmOrUpdate(farmacie.get(i));
        }
        realm.commitTransaction();
        allDownloads.put(SelfBoxConstants.ContentSyncType.ANAGRAFICHE, true);
        checkEndSync();

    }


    private String getRepcode() {
        String repCode = "";
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        InfoApp appInfo = realm.where(InfoApp.class).findFirst();
        if (appInfo != null) {
            if (!appInfo.repCode.isEmpty()) {
                repCode = appInfo.repCode;
            }
        }
        return repCode;
    }

    private void getAllContents() {

        String isf = getRepcode();
        if (!isf.isEmpty()) {
            apiInteractor.getAllContents(isf)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<Folder>>() {
                        @Override
                        public void call(List<Folder> folders) {
                            persistenceContentList(folders);

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                            Dbg.p("CALL ERRORE getAllcontents");

                        }
                    });
        }

    }

    private void persistenceContentList(List<Folder> folders) {
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        realm.beginTransaction();
        for (int i = 0; i < folders.size(); i++) {
            realm.copyToRealmOrUpdate(folders.get(i));
        }
        realm.commitTransaction();
        Dbg.p("persistenceContentList OK");
        view.startContentsService();
    }

    private int getRandom(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }
}
