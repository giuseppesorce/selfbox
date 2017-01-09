package com.docgenerici.selfbox.android.synservices;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.docgenerici.selfbox.android.downloader.DownloaderDoc;
import com.docgenerici.selfbox.android.downloader.ListenerDowloadDoc;
import com.docgenerici.selfbox.config.SelfBoxConstants;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.SyncDataCheck;
import com.docgenerici.selfbox.models.contents.ContentBox;
import com.docgenerici.selfbox.models.contents.ContentEasy;
import com.docgenerici.selfbox.models.contents.Folder;
import com.orhanobut.hawk.Hawk;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2016.
 *         <p>......</p>
 */

public class ContentsService extends IntentService {

    private ArrayList<ContentEasy> contentsEasy;
    private ArrayList<ContentEasy> folderEasy;
    private ArrayList<ContentEasy> coverEasy;
    private DownloaderDoc downloaderContent;
    private int counterContents;
    private int counterFolders;
    private boolean serviceDetroyed = false;
    private DownloaderDoc downloaderCover;
    private boolean bContentDownload = false;
    private boolean bContentCoverDownload = false;
    private DownloaderDoc downloaderFolderCover;
    private int donwloadCoverCount;
    private int counterCovers;
    private int totalPercentage;

    public ContentsService() {
        super("ContentsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        contentsEasy = new ArrayList<>();
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    RealmResults<ContentBox> realmResultsContentsBox = realm.where(ContentBox.class).findAll();
                    for (int i = 0; i < realmResultsContentsBox.size(); i++) {
                        ContentEasy contentEasy = new ContentEasy();
                        contentEasy.id = realmResultsContentsBox.get(i).id;
                        contentEasy.filename = realmResultsContentsBox.get(i).filename;
                        contentEasy.name = realmResultsContentsBox.get(i).name;
                        contentEasy.lastUpdate = realmResultsContentsBox.get(i).lastUpdate;
                        contentEasy.resourcePath = realmResultsContentsBox.get(i).resourcePath;
                        contentEasy.thumbnailCover = realmResultsContentsBox.get(i).thumbnailCover;
                        contentEasy.thumbnailPath = realmResultsContentsBox.get(i).thumbnailPath;
                        contentsEasy.add(contentEasy);
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        counterContents = 0;
        totalPercentage= contentsEasy.size() *3;
        downloaderContent = DownloaderDoc.newInstance(listenerDownloadContent);
        downloaderCover = DownloaderDoc.newInstance(listenerDownloadContentCover);
        downloaderFolderCover = DownloaderDoc.newInstance(listenerDownloadFolderCover);
        Dbg.p("CONTENUTI: " + contentsEasy.size());
        downloadContent();
    }


    private void downloadContent() {
        serviceDetroyed = Hawk.get("contentsServiceDestroy", false);
        if (serviceDetroyed) {
            Dbg.p("serviceDetroyed non vado oltre");
            stopSelf();
        } else {
            if (counterContents < contentsEasy.size()) {
                if (counterContents > 0) {
                    sendUpdate((counterContents) * 100 / totalPercentage);
                }
                bContentDownload = false;
                ContentEasy contentEasy = contentsEasy.get(counterContents);
                //CONTENT
                if (contentEasy.name.startsWith("Nuovo1")) {
                    Dbg.p("NUOVO scarico nuovo: " + contentEasy.name);
                }

                String pathContent = normalizePath(contentEasy.resourcePath) + contentEasy.filename;

                Uri uriContents = Uri.parse(pathContent.replace(" ", "%20"));
                String filenameContent = normalizename( contentEasy.lastUpdate + "___" + contentEasy.filename);
                if (contentEasy.name.startsWith("Nuovo1")) {
                    Dbg.p("NUOVO uriContents : " + contentEasy.name + " u:" + uriContents.toString());
                    Dbg.p("NUOVO filenameContent:" + contentEasy.name + " f:" + filenameContent);
                }
                File file = new File(getExternalFilesDir("contents"), filenameContent);
                if (file.exists()) {
                    //Dbg.p("FILE ESISTE: "+filenameContent);
                    Dbg.p("NUOVO esiste : " + contentEasy.name + " u:" + uriContents.toString());
                    updateContent(file.getAbsolutePath(), contentEasy.id);
                    bContentDownload = true;
                    unzipFile(file);
                    checkContentDownload();
                    if (contentEasy.name.startsWith("Nuovo1")) {
                        Dbg.p("NUOVO esiste :" + contentEasy.name);
                        Dbg.p("NUOVO filenameContent:" + contentEasy.name + " f:" + filenameContent);
                    }
                } else {

                    if (contentEasy.name.startsWith("Nuovo1")) {
                        Dbg.p("NUOVO parte il download " + contentEasy.name);

                    }
                    downloaderContent.download(uriContents, "contents", filenameContent, contentEasy.id);
                }


            } else {
                Dbg.p("FINITO FILES");
                Dbg.p("downloadContent finito: ");
                counterFolders = 0;
                startDownloadFoldersCover();
            }
        }
    }


    private void unzipFile(File file) {


        if (file.exists() && file.getAbsolutePath().contains(".zip")) {
            Dbg.p("File name" + file.getName());
            try {
                ZipFile zipFile = new ZipFile(file.getAbsolutePath());
                if (zipFile.isValidZipFile()) {
                    String folder = file.getAbsolutePath().replace(file.getName(), "") + file.getName().replace(".zip", "");
                    Dbg.p("folder: " + folder);
                    zipFile.extractAll(folder);
                    Dbg.p("OK : " + file.getAbsolutePath());
                } else {
                    Dbg.p("ERRORE: unzip");
                }

            } catch (ZipException e) {
                e.printStackTrace();
                Dbg.p("ERRORE: " + e.getLocalizedMessage());
            }


        } else {
            // Toast.makeText(this, "NON Esiste", Toast.LENGTH_SHORT).show();
        }
    }

    private void startDownloadFoldersCover() {
        Dbg.p("FOLDER startDownloadFoldersCover");
        folderEasy = new ArrayList<>();
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    RealmResults<Folder> realmResultsContentsBox = realm.where(Folder.class).findAll();
                    for (int i = 0; i < realmResultsContentsBox.size(); i++) {
                        ContentEasy contentEasy = new ContentEasy();
                        contentEasy.id = realmResultsContentsBox.get(i).id;
                        contentEasy.filename = realmResultsContentsBox.get(i).cover;
                        contentEasy.lastUpdate = realmResultsContentsBox.get(i).creationDate;
                        contentEasy.thumbnailCover = realmResultsContentsBox.get(i).cover;
                        contentEasy.thumbnailPath = realmResultsContentsBox.get(i).path;
                        folderEasy.add(contentEasy);
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        downloadFolderCover();


    }

    private void downloadFolderCover() {
        Dbg.p("FOLDER downloadFolderCover");
        if (counterFolders < folderEasy.size()) {
            Dbg.p("FOLDER CONTENUTI: DONWLOAD FOLDERCOVER");
            if (counterFolders > 0) {
                sendUpdate((counterContents + (totalPercentage/3)) * 100 / totalPercentage);
            }
            ContentEasy contentEasy = folderEasy.get(counterFolders);
            //CONTENT
            String pathContentCover = normalizePath(contentEasy.thumbnailPath) + contentEasy.filename;
            Uri uriContentsFolderCover = Uri.parse(pathContentCover.replace(" ", "%20"));
            String filenameContent = contentEasy.lastUpdate + "___" + contentEasy.filename;
            filenameContent= normalizename(filenameContent);



            File file = new File(getExternalFilesDir("contents"), filenameContent);
            Dbg.p("FOLDER: pathContentCover: " + pathContentCover);
            Dbg.p("FOLDER: filenameContent: " + filenameContent);

            if (file.exists()) {
                Dbg.p("FOLDER: esiste");
                updateFolderCover(file.getAbsolutePath(), contentEasy.id);
                counterFolders++;
                downloadFolderCover();
            } else {
                Dbg.p("FOLDER: scarico: uriContentsFolderCover: "+uriContentsFolderCover+ " filenameContent: "+filenameContent);
                downloaderFolderCover.download(uriContentsFolderCover, "contents", filenameContent, contentEasy.id);
            }


        } else {

            Dbg.p("CONTENUTI FINITO COVER");
            counterCovers = 0;
            startDownloadCover();
        }

    }

    private String normalizename(String filenameContent) {
        String newname= filenameContent.replace(".JPG", ".jpg").replace(" ", "").toLowerCase();
        return  newname;
    }

    private void startDownloadCover() {

        Dbg.p("startDownloadCover");
        coverEasy = new ArrayList<>();
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    RealmResults<ContentBox> realmResultsContentsBox = realm.where(ContentBox.class).findAll();
                    for (int i = 0; i < realmResultsContentsBox.size(); i++) {
                        ContentEasy contentEasy = new ContentEasy();
                        contentEasy.id = realmResultsContentsBox.get(i).id;
                        contentEasy.lastUpdate = realmResultsContentsBox.get(i).creationDate;
                        contentEasy.thumbnailCover = realmResultsContentsBox.get(i).thumbnailCover;
                        contentEasy.thumbnailPath = realmResultsContentsBox.get(i).thumbnailPath;
                        if( contentEasy.id==9){
                            Dbg.p("PRINTDATA: contentEasy.thumbnailCover: "+contentEasy.thumbnailCover);
                            Dbg.p("PRINTDATA: contentEasy.thumbnailCover: "+contentEasy.thumbnailPath);
                        }
                        coverEasy.add(contentEasy);

                    }
                }
            });
        } catch (Exception ex){
            Dbg.p("PRINTDATA ERRORE startDownloadCover");
        }finally {
            if (realm != null) {
                realm.close();
            }
        }
        downloadCovers();
    }

    private void downloadCovers() {
        Dbg.p("CONTENUTI downloadCovers");
        if (counterCovers < coverEasy.size()) {
            if (counterCovers > 0) {
                sendUpdate((counterCovers+ totalPercentage/3+ totalPercentage/3) * 100 / totalPercentage);
            }

            ContentEasy contentEasy = coverEasy.get(counterCovers);
            String pathCover = normalizePath(contentEasy.thumbnailPath) + contentEasy.thumbnailCover;
            Uri uriContentCover = Uri.parse(pathCover.replace(" ", "%20"));

            String filenameContentCover =  normalizename(contentEasy.lastUpdate + "___" + contentEasy.thumbnailCover);
            File fileCover = new File(getExternalFilesDir("contents"), filenameContentCover);
            if(contentEasy.id==9) {
                Dbg.p("PRINTDATA: COVER: id: " + contentEasy.id);
                Dbg.p("PRINTDATA: COVER: pathCover: " + pathCover);
                Dbg.p("PRINTDATA: COVER: uriContentCover: " + uriContentCover);
                Dbg.p("PRINTDATA: COVER: filenameContentCover: " + filenameContentCover);
                Dbg.p("PRINTDATA: COVER: fileCover: " + fileCover.toString());
            }

            if (fileCover.exists()) {
                updateContentCover(fileCover.getAbsolutePath(), contentEasy.id);
                counterCovers++;
                downloadCovers();
            } else {
                if(contentEasy.id==9) {
                    Dbg.p("PRINTDATA: COVER scarico l' id 9");
                }
                downloaderCover.download(uriContentCover, "contents", filenameContentCover, contentEasy.id);
            }
        } else {
            updateContentSyncData();
            stopSelf();
            sendUpdate(100);
            sendUpdate("end");
            Dbg.p("FINITO TUTTO");
        }

    }

    private String normalizePath(String thumbnailPath) {
        if (thumbnailPath.substring(thumbnailPath.length() - 1, thumbnailPath.length()).equalsIgnoreCase("/")) {
            return thumbnailPath;
        } else {
            return thumbnailPath + "/";
        }
    }

    private void updateContentSyncData() {
        Dbg.p("SYNCDATA modifico contenuti");

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    SyncDataCheck model = realm.where(SyncDataCheck.class).equalTo("id", 1).findFirst();
                    if (model != null) {
                        model.contents = 1;

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
        myIntent.putExtra("type", SelfBoxConstants.ContentSyncType.CONTENTS);
        myIntent.putExtra("percentage", 100);
        myIntent.putExtra("message", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(myIntent);

    }

    private void sendUpdate(float percentage) {
        Intent myIntent = new Intent("sync");
        myIntent.putExtra("type", SelfBoxConstants.ContentSyncType.CONTENTS);
        myIntent.putExtra("percentage", Math.round(percentage));
        myIntent.putExtra("message", "percentage");
        LocalBroadcastManager.getInstance(this).sendBroadcast(myIntent);
    }


    private ListenerDowloadDoc listenerDownloadContent = new ListenerDowloadDoc() {
        @Override
        public void fileDownloaded(Uri uri, String mimeType, int id) {
            updateContent(uri.toString(), id);
            bContentDownload = true;
            if (uri.toString().contains(".zip")) {
                unzipFile(new File(uri.toString()));
            }
            checkContentDownload();
        }

        @Override
        public void downloadError(String error, int errortype, int id) {
            Dbg.p("ERROR CONTENTS DOWNLOAD: " + id+ "errortype "+errortype+ " error: "+error);
            updateContent("error_" + error, id);
            bContentDownload = true;
            checkContentDownload();

        }

        @Override
        public Context getContext() {
            return getApplicationContext();
        }
    };

    private void checkContentDownload() {
        counterContents++;
        downloadContent();

    }

    private ListenerDowloadDoc listenerDownloadContentCover = new ListenerDowloadDoc() {
        @Override
        public void fileDownloaded(Uri uri, String mimeType, int id) {
          //  Dbg.p("COVER FILE SCARICATO: " + uri + " ID: " + id);
            updateContentCover(uri.toString(), id);
            if(id==9){
                Dbg.p("PRINTDATA: COVER ID9  SCARICATO : "+uri+ " id: "+id);

            }
            counterCovers++;
            downloadCovers();

        }

        @Override
        public void downloadError(String error, int errortype, int id) {
            Dbg.p("COVER ERRROR: " + id);
            updateContentCover("error_" + error, id);
            counterCovers++;
            downloadCovers();


        }

        @Override
        public Context getContext() {
            return getApplicationContext();
        }
    };


    private ListenerDowloadDoc listenerDownloadFolderCover = new ListenerDowloadDoc() {
        @Override
        public void fileDownloaded(Uri uri, String mimeType, int id) {
            Dbg.p("FOLDER: FILE SCARICATO: " + uri + "ID: " + id);
            updateFolderCover(uri.toString(), id);
            counterFolders++;
            downloadFolderCover();
        }

        @Override
        public void downloadError(String error, int errortype, int id) {

            Dbg.p("ERROR CONTENTS FOLDERDOWNLOAD: " + id+ "errortype "+errortype+ " error: "+error);
            updateFolderCover("error_" + errortype, id);
            counterFolders++;
            downloadFolderCover();

        }

        @Override
        public Context getContext() {
            return getApplicationContext();
        }
    };

    private void updateFolderCover(final String uri, final int id) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    Folder model = realm.where(Folder.class).equalTo("id", id).findFirst();
                    if (model != null) {
                        model.setCoverUri(uri);
                    }
                }
            });
        } catch (Exception error){
            Dbg.p("ERROR UPDATE COVER IMAGE FOLDER");
        } finally{
            if (realm != null) {
                realm.close();
            }
        }
    }

    private void updateContentCover(final String uri, final int id) {
        Realm realm = null;
        if(uri==null){
            Dbg.p("PRINTDATA: errore uri è null");
        }
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    ContentBox model = realm.where(ContentBox.class).equalTo("id", id).findFirst();
                    if(id==9){
                        Dbg.p("PRINTDATA:  UPDATE ID 9: "+uri);
                    }
                    if (model != null) {
                        model.setLocalthumbnailPath(uri);
                    }
                }
            });
        }catch (Exception ex){
            Dbg.p("COVER ERRORE updateContentCover");
        }
        finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    private void updateContent(final String uri, final int id) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    ContentBox model = realm.where(ContentBox.class).equalTo("id", id).findFirst();
                    if (model != null) {
                        model.setLocalfilePath(uri);
                    }
                }
            });
        } catch (Exception ex) {

                Dbg.p("ERRORE updateContent uri "+uri+ " id: "+id);

        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
}
