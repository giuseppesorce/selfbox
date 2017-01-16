package com.docgenerici.selfbox.android.synservices;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.docgenerici.selfbox.android.downloader.DownloaderDoc;
import com.docgenerici.selfbox.android.downloader.ListenerDowloadDoc;
import com.docgenerici.selfbox.android.utils.SelfBoxUtils;
import com.docgenerici.selfbox.config.SelfBoxConstants;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.SyncDataCheck;
import com.docgenerici.selfbox.models.products.Product;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2016.
 *         <p>......</p>
 */

public class ProductSyncService extends IntentService {

    private RealmResults<Product> realmResultsProdduct;
    private int counterScheda;
    private int counterProduct;

    private DownloaderDoc downloaderScheda;
    private DownloaderDoc downloaderProduct;
    private boolean downLoadScheda;
    private boolean donwloadedProduct;
    private Realm realm;

    private String aicCode;
    private ArrayList<String> allaicCodes;
    private String pathSchedaDownload;
    private String schedaPublished;
    private String schedaFilename;
    private String productPublished;
    private String productFilename;
    private String pathProductDownload;
    private int totalPercentage;

    public ProductSyncService() {
        super("ProductSyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        allaicCodes = new ArrayList<String>();
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    realmResultsProdduct = realm.where(Product.class).findAll();
                    for (int i = 0; i < realmResultsProdduct.size(); i++) {
                        allaicCodes.add(realmResultsProdduct.get(i).getAic());
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        totalPercentage= allaicCodes.size() *2;
        counterScheda = 0;
        downloaderScheda = DownloaderDoc.newInstance(listenerDownloadScheda);
        downloaderProduct = DownloaderDoc.newInstance(listenerDownloadProduct);
        downloadScheda();
    }

    private void downloadScheda() {
        if (counterScheda < allaicCodes.size()) {
            float percentage = counterScheda * 100 / totalPercentage;
            sendUpdate(percentage);
            aicCode = allaicCodes.get(counterScheda);
            pathSchedaDownload = "";

            Realm realm = null;
            try {
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //TODO prendere la url da qui
                        Product nowProduct = realm.where(Product.class).equalTo("aic", aicCode).findFirst();
                        if (nowProduct.getScheda() != null && nowProduct.getScheda().getUri() != null && !nowProduct.getScheda().getUri().isEmpty() && nowProduct.getScheda().getUri().length() > 5) {
                            pathSchedaDownload = SelfBoxConstants.pathProduct + nowProduct.getScheda().getUri();
                        }

                        schedaPublished = nowProduct.getScheda().published;
                        schedaFilename = nowProduct.getScheda().filename;
                        Dbg.p("pathSchedaDownload: " + pathSchedaDownload);
                        Dbg.p("schedaFilename: " + schedaFilename);


                    }
                });
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }


            if (pathSchedaDownload.length() > 5) {
                Uri uriScheda = Uri.parse(pathSchedaDownload.replace(" ", "%20"));
                String filenameScheda = SelfBoxUtils.dateConvertNumber(schedaPublished) + "___" + "scheda___" + schedaFilename.replace("%20", "");
                File file = new File(getExternalFilesDir("products"), filenameScheda);
                if (file.exists()) {
                    Dbg.p("FILE ESISTE");
                    updateScheda(file.getAbsolutePath());
                    counterScheda++;
                    Dbg.p("Vado al prossimo: " + counterScheda);
                    downloadScheda();
                } else {
                    downloaderScheda.download(uriScheda, "products", filenameScheda);
                }
            } else {
                counterScheda++;
                downloadScheda();
            }

        } else {
            counterScheda= allaicCodes.size();
            counterProduct = 0;
            donwloadProductPdf();
        }
    }


    private void donwloadProductPdf() {

        if (counterProduct < allaicCodes.size()) {


            float percentage = (counterScheda + counterProduct) * 100 / (totalPercentage);
            sendUpdate(percentage);
            aicCode = allaicCodes.get(counterProduct);
            pathProductDownload = "";
            Realm realm = null;
            try {
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //TODO prendere la url da qui
                        Product nowProduct = realm.where(Product.class).equalTo("aic", aicCode).findFirst();
                        if (nowProduct.rcp != null && !nowProduct.getFilename().isEmpty()) {
                            pathProductDownload = SelfBoxConstants.pathProduct + nowProduct.rcp;
                        }
                        productPublished = nowProduct.risorsa.published;
                        productFilename = nowProduct.getFilename();
                        Dbg.p("pathProductDownload: " + pathProductDownload);
                        Dbg.p("productFilename: " + productFilename);

                    }
                });
            } catch (Exception ex) {

            } finally {
                if (realm != null) {
                    realm.close();
                }
            }


            if (pathProductDownload.length() > 5) {

                Uri uriProduct = Uri.parse(pathProductDownload.replace(" ", "%20"));
                String filenameProduct = SelfBoxUtils.dateConvertNumber(productPublished) + "___" + "prod___" + productFilename.replace("%20", "");
                File file = new File(getExternalFilesDir("products"), filenameProduct);

                if (file.exists()) {
                    counterProduct++;
                    updateProductUri(file.getAbsolutePath());
                    donwloadProductPdf();
                } else {
                    downloaderProduct.download(uriProduct, "products", filenameProduct);
                }

            } else {
                counterProduct++;
                updateProductUri("error_not_found");
                donwloadProductPdf();
            }

        } else

        {
            updateProducSyncData();
            Dbg.p("FINITO");
            stopSelf();
            sendUpdate(100);
            sendUpdate("end");
        }

    }

    private void updateProducSyncData() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    SyncDataCheck model = realm.where(SyncDataCheck.class).equalTo("id", 1).findFirst();
                    if (model != null) {
                        model.products = 1;
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }


    private void sendUpdate(String message) {

        Intent myIntent = new Intent("sync");
        myIntent.putExtra("type", SelfBoxConstants.ContentSyncType.PRODUCTS);
        myIntent.putExtra("percentage", 100);
        myIntent.putExtra("message", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(myIntent);
    }

    private void sendUpdate(float percentage) {
        Intent myIntent = new Intent("sync");
        myIntent.putExtra("type", SelfBoxConstants.ContentSyncType.PRODUCTS);
        myIntent.putExtra("percentage", Math.round(percentage));
        myIntent.putExtra("message", "percentage");
        LocalBroadcastManager.getInstance(this).sendBroadcast(myIntent);
    }


    private void updateScheda(final String urischeda) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Product prod = realm.where(Product.class).equalTo("aic", aicCode).findFirst();
                    if (prod != null) {

                        prod.getScheda().uriPdf = (urischeda);
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }

    }

    private void updateProductUri(final String uriproduct) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    Product model = realm.where(Product.class).equalTo("aic", aicCode).findFirst();
                    if (model != null) {
                        model.setUriPdf(uriproduct);
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    private ListenerDowloadDoc listenerDownloadScheda = new ListenerDowloadDoc() {
        @Override
        public void fileDownloaded(Uri uri, String mimeType, int id) {
            Dbg.p("FILE SCHEDA SCARICATO: " + uri);
            updateScheda(uri.toString());
            counterScheda++;
            Dbg.p("Vado al prossimo: " + counterScheda);
            downloadScheda();


        }

        @Override
        public void downloadError(String error, int errortype, int id) {
            updateScheda("error_" + errortype);
            counterScheda++;
           downloadScheda();
        }

        @Override
        public Context getContext() {
            return getApplicationContext();
        }
    };


    private ListenerDowloadDoc listenerDownloadProduct = new ListenerDowloadDoc() {
        @Override
        public void fileDownloaded(Uri uri, String mimeType, int id) {
            Dbg.p("FILE PRODOTTO SCARICATO: " + uri);
            updateProductUri(uri.toString());
            counterProduct++;
            donwloadProductPdf();
        }

        @Override
        public void downloadError(String error, int errortype, int id) {
            updateProductUri("error_" + errortype);
            counterProduct++;
            donwloadProductPdf();
        }

        @Override
        public Context getContext() {
            return getApplicationContext();
        }
    };


}
