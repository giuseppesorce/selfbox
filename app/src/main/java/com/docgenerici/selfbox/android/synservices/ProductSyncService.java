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
    private int counterProducts;

    private DownloaderDoc downloaderScheda;
    private DownloaderDoc downloaderProduct;
    private boolean downLoadScheda;
    private boolean donwloadedProduct;
    private boolean bSchedaDownload;
    private boolean bProductDownload;
    private Realm realm;

    private String aicCode;
    private ArrayList<String> allaicCodes;
    private String pathSchedaDownload;
    private String schedaPublished;
    private String schedaFilename;
    private String productPublished;
    private String productFilename;
    private String pathProductDownload;

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

        counterProducts = 0;
        downloaderScheda = DownloaderDoc.newInstance(listenerDownloadScheda);
        downloaderProduct = DownloaderDoc.newInstance(listenerDownloadProduct);
        downloadContent();
    }

    private void downloadContent() {
        if (counterProducts<  allaicCodes.size()) {
            float percentage = counterProducts * 100 / allaicCodes.size();
            sendUpdate(percentage);
            aicCode = allaicCodes.get(counterProducts);
            pathSchedaDownload = "";
            pathProductDownload = "";
            Realm realm = null;
            try {
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        Product nowProduct = realm.where(Product.class).equalTo("aic", aicCode).findFirst();
                        if (nowProduct.getScheda() != null && nowProduct.getScheda().getUri() != null && !nowProduct.getScheda().getUri().isEmpty() && nowProduct.getScheda().getUri().length() > 5) {
                           pathSchedaDownload = SelfBoxConstants.pathProduct + nowProduct.getScheda().getUri();
                        }
                        if (nowProduct.rcp != null && !nowProduct.getFilename().isEmpty() ) {
                            pathProductDownload = SelfBoxConstants.pathProduct + nowProduct.rcp;
                        }

                        schedaPublished = nowProduct.getScheda().published;
                        schedaFilename = nowProduct.getScheda().filename;
                        productPublished = nowProduct.risorsa.published;
                        productFilename = nowProduct.getFilename();
                        Dbg.p("pathSchedaDownload: "+pathSchedaDownload);
                        Dbg.p("schedaFilename: "+schedaFilename);
                        Dbg.p("pathProductDownload: "+pathProductDownload);
                        Dbg.p("productFilename: "+productFilename);

                    }
                });
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }

            bSchedaDownload = false;
            bProductDownload = false;
            if (pathSchedaDownload.length() > 5) {
                Uri uriScheda = Uri.parse(pathSchedaDownload.replace(" ", "%20"));
                String filenameScheda = SelfBoxUtils.dateConvertNumber(schedaPublished) + "___" + "scheda___" + schedaFilename;
                File file = new File(getExternalFilesDir("products"), filenameScheda);
                if (file.exists()) {
                    bSchedaDownload = true;
                    Dbg.p("FILE ESISTE");
                    updateScheda(file.getAbsolutePath());
                } else {
                    downloaderScheda.download(uriScheda, "products", filenameScheda);
                }


            } else {
                bSchedaDownload = true;
            }
            if (pathProductDownload.length() > 5) {

                Uri uriProduct = Uri.parse(pathProductDownload.replace(" ", "%20"));
                           String filenameProduct = SelfBoxUtils.dateConvertNumber(productPublished) + "___" + "prod___" + productFilename;
                File file = new File(getExternalFilesDir("products"), filenameProduct);

                if (file.exists()) {
                    bProductDownload = true;
                    updateProductUri(file.getAbsolutePath());
                } else {
                    downloaderProduct.download(uriProduct, "products", filenameProduct);
                }

            } else {
                bProductDownload = true;
            }


            if (bSchedaDownload && bProductDownload) {
                Dbg.p("Salto al prossimo: senza dati");
                counterProducts++;
                downloadContent();
            }
        } else {
            Dbg.p("FINITO");
            stopSelf();
            sendUpdate(100);
            sendUpdate("end");
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
        Dbg.p("updateScheda");
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
    //    Dbg.p("updateProductUri: "+uriproduct);
        if(aicCode.equalsIgnoreCase("033551021")){
            Dbg.p("updateProductUri: "+uriproduct);
        }
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
             bSchedaDownload = true;
            updateScheda(uri.toString());
            checkUpdated();

        }

        @Override
        public void downloadError(String error, int errortype, int id) {
            bSchedaDownload = true;
            updateScheda("error_" + errortype);
            checkUpdated();
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

            bProductDownload = true;
            updateProductUri( uri.toString());
            checkUpdated();
        }

        @Override
        public void downloadError(String error, int errortype, int id) {
            bProductDownload = true;
            updateProductUri("error_" + errortype);
            checkUpdated();
        }

        @Override
        public Context getContext() {
            return getApplicationContext();
        }
    };

    private void checkUpdated() {
        if (bSchedaDownload && bProductDownload) {
            counterProducts++;
            Dbg.p("Vado al prossimo: " + counterProducts);
            downloadContent();

        }

    }


}
