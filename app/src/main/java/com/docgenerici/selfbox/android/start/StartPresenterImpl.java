package com.docgenerici.selfbox.android.start;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.async.XmlTaskParse;
import com.docgenerici.selfbox.android.utils.SelfBoxUtils;
import com.docgenerici.selfbox.comm.ApiInteractor;
import com.docgenerici.selfbox.comm.storage.Environment;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.LoginResponse;
import com.docgenerici.selfbox.models.MedicalList;
import com.docgenerici.selfbox.models.contents.Folder;
import com.docgenerici.selfbox.models.farmacia.Farmacia;
import com.docgenerici.selfbox.models.medico.Medico;
import com.docgenerici.selfbox.models.persistence.InfoApp;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class StartPresenterImpl implements StartPresenter {
    private final ApiInteractor apiInteractor;
    private StartView view;

    public StartPresenterImpl(ApiInteractor loginInteractor) {
        this.apiInteractor = loginInteractor;
    }

    @Override
    public void setView(BaseView view) {
        if (!(view instanceof StartView)) {
            throw new IllegalArgumentException("View must extend StartPresenter.View");
        }
        this.view = (StartView) view;
    }

    @Override
    public void chekActivation() {
        if (hereActivation()) {
            view.showProgressToSend();
            getAllMedicalData();
        } else {
            view.showActivationInput();
        }
    }

    @Override
    public void setActivation(String isfCode) {
        if (isfCode == null || isfCode.length() < 5) {
            view.showCodeError("Errore nel caricamento dei dati");
        } else {
            view.showProgressToSend();
            String appVer = SelfBoxUtils.getApplicationVersionName(SelfBoxApplicationImpl.appComponent.context());
            String devId = SelfBoxUtils.getDeviceId(SelfBoxApplicationImpl.appComponent.context());
            apiInteractor.login(appVer, devId, isfCode, isfCode)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<LoginResponse>() {
                        @Override
                        public void call(LoginResponse loginResponse) {

                            if (loginResponse.result) {
                                final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
                                realm.beginTransaction();
                                InfoApp infoApp = new InfoApp();
                                infoApp.line = loginResponse.line;
                                infoApp.name = loginResponse.name;
                                infoApp.repCode = loginResponse.repCode;
                                infoApp.result = loginResponse.result;
                                infoApp.selfBoxContentDownloadUrl = loginResponse.selfBoxContentDownloadUrl;
                                infoApp.selfBoxIsfDrugstoreDownloadUrl = loginResponse.selfBoxIsfDrugstoreDownloadUrl;
                                infoApp.selfBoxProductDownloadUrl = loginResponse.selfBoxProductDownloadUrl;
                                realm.copyToRealmOrUpdate(infoApp);
                                realm.commitTransaction();
                                getAllMedicalData();
                            } else {
                                view.showCodeError("Errore nel caricamento dei dati");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                            Dbg.p("CALL ERRORE");

                        }
                    });
        }
    }


    private void getAllMedicalData() {

        String isf = getRepcode();
        if (!isf.isEmpty()) {

            apiInteractor.getallMedical(isf)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<MedicalList>() {
                        @Override
                        public void call(MedicalList medialresponse) {

                            persistenceMedicalList(medialresponse);

                            view.gotoHome();

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                            Dbg.p("CALL ERRORE getAllMedicalData");

                        }
                    });

        }else{
            view.showCodeError("Errore nel caricamento dei dati");
        }
    }

    private void persistenceMedicalList(MedicalList medicalList) {

        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        realm.beginTransaction();
        List<Medico> medici = medicalList.medici;
        for (int i = 0; i < medici.size(); i++) {
            realm.copyToRealmOrUpdate(medici.get(i));
        }
        List<Farmacia> farmacie= medicalList.farmacie;
        for (int i = 0; i < farmacie.size(); i++) {
            realm.copyToRealmOrUpdate(farmacie.get(i));
        }
        realm.commitTransaction();
    }

    private void getAllContents() {

        String isf = "77750";
        apiInteractor.getAllContents(isf)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Folder>>() {
                    @Override
                    public void call(List<Folder> folders) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        Dbg.p("CALL ERRORE getAllcontents");

                    }
                });


    }

    private void getProduct() {


        Environment environment = SelfBoxApplicationImpl.appComponent.environment();
        environment.setBaseUrl("http://www.docgenerici.it/app/app.php");


        apiInteractor.getProduct("20161010")
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

        XmlTaskParse parseTask = new XmlTaskParse();
        parseTask.execute(xmlString);

    }


    private boolean hereActivation() {
        boolean activated = false;
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        InfoApp appInfo = realm.where(InfoApp.class).findFirst();
        if (appInfo != null) {
            if (!appInfo.repCode.isEmpty()) {
                Dbg.p("appInfo.repCode: " + appInfo.repCode);
                activated = true;
            }
        }
        Dbg.p("activated: " + activated);
        return activated;
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
}
