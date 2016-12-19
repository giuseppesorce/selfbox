package com.docgenerici.selfbox.android.async;

import com.artifex.mupdfdemo.AsyncTask;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.sync.OnSyncData;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.products.Formulazione;
import com.docgenerici.selfbox.models.products.Prodotto;
import com.docgenerici.selfbox.models.products.Product;
import com.docgenerici.selfbox.models.products.Risorsa;
import com.docgenerici.selfbox.models.products.RisorsaDb;
import com.docgenerici.selfbox.models.products.Scheda;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.realm.Realm;

/**
 * @uthor giuseppesorce
 */

public class XmlTaskParse extends AsyncTask<String, Void, Void> {
    private OnSyncData onSyncData;
    private Prodotto prodotto;
    private ArrayList<Prodotto> products;

    public XmlTaskParse(OnSyncData onSyncData) {
        this.onSyncData= onSyncData;
    }

    @Override
    protected Void doInBackground(String... params) {
        String yourXml = params[0];
        Document document = null;
       products = new ArrayList<>();
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
                                    risorsa.published = risorsaTag.getAttribute("published");
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
//        for (int i = 0; i < products.size(); i++) {
//            Prodotto prodotto= products.get(i);
//            Dbg.p("**********Prodotto["+i+"]= "+prodotto.nome);
//            Risorsa scheda= prodotto.risorse.get(0);
//            Dbg.p("Scheda: "+scheda.filename);
//            if(scheda.filename.length() < 1){
//                Dbg.p("ERRORE:_ "+prodotto.nome);
//            }
//        }
       onSyncData.onXmlParsed(products);

    }
}

