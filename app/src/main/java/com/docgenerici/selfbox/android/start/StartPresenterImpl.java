package com.docgenerici.selfbox.android.start;

import android.os.AsyncTask;

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
import com.docgenerici.selfbox.models.persistence.InfoApp;
import com.docgenerici.selfbox.models.products.Formulazione;
import com.docgenerici.selfbox.models.products.Prodotto;
import com.docgenerici.selfbox.models.products.Risorsa;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
    private Prodotto prodotto;

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
            view.gotoHome();
        } else {
            view.showActivationInput();
        }
    }

    @Override
    public void setActivation(String isfCode) {
        if (isfCode == null || isfCode.length() < 5) {
            view.showCodeError();
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

                            if(loginResponse.result) {
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
                            }else{

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
        if(!isf.isEmpty()) {

            apiInteractor.getallMedical(isf)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<MedicalList>() {
                        @Override
                        public void call(MedicalList loginResponse) {

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                            Dbg.p("CALL ERRORE getAllMedicalData");

                        }
                    });

        }
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

    class ParseXMLTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String yourXml = params[0];
            Document document = null;
            ArrayList<Prodotto> products = new ArrayList<>();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder db = factory.newDocumentBuilder();
                document = db.parse(new StringBufferInputStream(yourXml));
            } catch (ParserConfigurationException e) {
                Dbg.p("ParserConfigurationException: " + e.getLocalizedMessage());

            } catch (IOException e) {
                Dbg.p("IOException: " + e.getLocalizedMessage());
            } catch (SAXException e) {
                Dbg.p("SAXException: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
            if (document != null) {
                NodeList nodeList = document.getElementsByTagName("prodotto");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    prodotto = new Prodotto();
                    Element e = (Element) nodeList.item(i);
                    String noFCDL = e.getAttribute("noFCDL");
                    String originatore = e.getAttribute("originatore");
                    String categoria_farmacologica = e.getAttribute("categoria_farmacologica");
                    String nome = e.getAttribute("nome");
                    prodotto.noFCDL = noFCDL;
                    prodotto.originatore = originatore;
                    prodotto.categoria_farmacologica = categoria_farmacologica;
                    prodotto.nome = nome;
                    NodeList nodeChilds = e.getChildNodes();


                    for (int j = 0; j < nodeChilds.getLength(); j++) {
                        if (nodeChilds.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            if (nodeChilds.item(j).getNodeName().equalsIgnoreCase("elenco_formulazioni")) {

                                if (prodotto.formulazioni == null) {
                                    prodotto.formulazioni = new ArrayList<>();
                                }
                                NodeList forumulazioniTags = nodeChilds.item(j).getChildNodes();
                                for (int f = 0; f < forumulazioniTags.getLength(); f++) {
                                    if (forumulazioniTags.item(f).getNodeType() == Node.ELEMENT_NODE) {
                                        Element formulazioneElement = (Element) forumulazioniTags.item(f);
                                        Formulazione formulazione = new Formulazione();
                                        prodotto.formulazioni.add(formulazione);
                                        formulazione.aic = formulazioneElement.getAttribute("aic");
                                        formulazione.prezzo = formulazioneElement.getAttribute("prezzo");
                                        formulazione.published = formulazioneElement.getAttribute("published");
                                        formulazione.codeProgressivo = formulazioneElement.getAttribute("codeProgressivo");
                                        formulazione.noFCDL = formulazioneElement.getAttribute("noFCDL");
                                        formulazione.originatore = formulazioneElement.getAttribute("originatore");
                                        formulazione.regimeDispensazione = formulazioneElement.getAttribute("regimeDispensazione");
                                        formulazione.classeSSN = formulazioneElement.getAttribute("classeSSN");
                                        formulazione.classeSSN = formulazioneElement.getAttribute("classeSSN");
                                        if (formulazioneElement.hasChildNodes()) {
                                            NodeList formulazioneElementChild = formulazioneElement.getChildNodes();
                                            for (int z = 0; z < formulazioneElementChild.getLength(); z++) {
                                                if (formulazioneElementChild.item(z).getNodeType() == Node.ELEMENT_NODE) {
                                                    Element denominazione = (Element) formulazioneElementChild.item(z);
                                                    if (denominazione.getTagName().equalsIgnoreCase("elenco_denominazioni")) {
                                                        NodeList listaDemoninazioni = denominazione.getChildNodes();
                                                        for (int d = 0; d < listaDemoninazioni.getLength(); d++) {
                                                            if (listaDemoninazioni.item(d).getNodeName().equalsIgnoreCase("denominazione")) {


                                                                if (((Element) listaDemoninazioni.item(d)).getAttribute("lang").equalsIgnoreCase("it")) {
                                                                    formulazione.denominazione_it = getTextNodeValue(((Node) listaDemoninazioni.item(d)));
                                                                }
                                                                if (((Element) listaDemoninazioni.item(d)).getAttribute("lang").equalsIgnoreCase("en")) {

                                                                    formulazione.denominazione_en = getTextNodeValue(((Node) listaDemoninazioni.item(d)));
                                                                }

                                                            }
                                                        }
                                                    } else if (denominazione.getTagName().equalsIgnoreCase("elenco_risorse")) {
                                                        if (formulazione.risorse == null) {
                                                            formulazione.risorse = new ArrayList<>();

                                                        }
                                                        NodeList childRisorse = denominazione.getChildNodes();
                                                        for (int g = 0; g < childRisorse.getLength(); g++) {
                                                            if (childRisorse.item(g).getNodeName().equalsIgnoreCase("risorsa")) {
                                                                Risorsa risorsa = new Risorsa();
                                                                Element elementRisorsa = (Element) childRisorse.item(g);
                                                                risorsa.filename = elementRisorsa.getAttribute("filename");
                                                                risorsa.published = elementRisorsa.getAttribute("published");
                                                                risorsa.status = elementRisorsa.getAttribute("status");
                                                                risorsa.uri = elementRisorsa.getAttribute("uri");
                                                                risorsa.id = elementRisorsa.getAttribute("id");
                                                                risorsa.tipo = elementRisorsa.getAttribute("tipo");
                                                                formulazione.risorse.add(risorsa);
                                                            }

                                                        }

                                                    }


                                                }


                                            }
                                        }

                                    }

                                }


                            } else if (nodeChilds.item(j).getNodeName().equalsIgnoreCase("elenco_risorse")) {
                                if (prodotto.risorse == null) {
                                    prodotto.risorse = new ArrayList<>();
                                }
                                NodeList risorseTags = nodeChilds.item(j).getChildNodes();
                                for (int k = 0; k < risorseTags.getLength(); k++) {
                                    if (risorseTags.item(k).getNodeName().equalsIgnoreCase("risorsa")) {

                                        Risorsa risorsa = new Risorsa();
                                        Element risorsaTag = (Element) risorseTags.item(k);
                                        risorsa.filename = risorsaTag.getAttribute("filename");
                                        risorsa.status = risorsaTag.getAttribute("status");
                                        risorsa.uri = risorsaTag.getAttribute("uri");
                                        risorsa.tipo = risorsaTag.getAttribute("tipo");
                                        prodotto.risorse.add(risorsa);

                                    }
                                }


                            }


                        }

                    }

                    products.add(prodotto);

                }
            }


            return null;
        }

        public String getValue(Element item, String name) {
            NodeList nodes = item.getElementsByTagName(name);
            return getTextNodeValue(nodes.item(0));
        }

        private final String getTextNodeValue(Node node) {
            Node child;
            if (node != null) {
                if (node.hasChildNodes()) {
                    child = node.getFirstChild();
                    while (child != null) {
                        if (child.getNodeType() == Node.TEXT_NODE) {
                            return child.getNodeValue();
                        }
                        child = child.getNextSibling();
                    }
                }
            }
            return "";
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //do something after parsing is done
        }
    }

    private boolean hereActivation() {
        boolean activated= false;
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        InfoApp appInfo = realm.where(InfoApp.class).findFirst();
        if(appInfo !=null){
            if(!appInfo.repCode.isEmpty()){
                Dbg.p("appInfo.repCode: "+appInfo.repCode);
                activated= true;
            }
        }
        Dbg.p("activated: "+activated);
        return activated;
    }

    private String getRepcode(){
        String repCode="";
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        InfoApp appInfo = realm.where(InfoApp.class).findFirst();
        if(appInfo !=null){
            if(!appInfo.repCode.isEmpty()){
                repCode= appInfo.repCode;
            }
        }
        return repCode;
    }
}
