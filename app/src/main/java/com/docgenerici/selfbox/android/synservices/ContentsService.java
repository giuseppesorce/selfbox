package com.docgenerici.selfbox.android.synservices;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.docgenerici.selfbox.android.downloader.DownloaderDoc;
import com.docgenerici.selfbox.android.downloader.ListenerDowloadDoc;
import com.docgenerici.selfbox.android.sync.StopReceiver;
import com.docgenerici.selfbox.config.SelfBoxConstants;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.contents.ContentBox;
import com.docgenerici.selfbox.models.contents.ContentEasy;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2016.
 *         <p>......</p>
 */

public class ContentsService extends IntentService{

    private ArrayList<ContentEasy> contentsEasy;
    private DownloaderDoc downloaderContent;
    private int counterContents;
    private boolean serviceDetroyed= false;
    private DownloaderDoc downloaderCover;
    private boolean bContentDownload= false;
    private boolean bContentCoverDownload= false;

    public ContentsService() {
        super("ContentsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        contentsEasy= new ArrayList<>();
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    RealmResults<ContentBox> realmResultsContentsBox = realm.where(ContentBox.class).findAll();
                    for (int i = 0; i < realmResultsContentsBox.size(); i++) {
                        ContentEasy contentEasy = new ContentEasy();
                        contentEasy.id= realmResultsContentsBox.get(i).id;
                        contentEasy.filename= realmResultsContentsBox.get(i).filename;
                        contentEasy.lastUpdate= realmResultsContentsBox.get(i).lastUpdate;
                        contentEasy.resourcePath= realmResultsContentsBox.get(i).resourcePath;
                        contentEasy.thumbnailCover= realmResultsContentsBox.get(i).thumbnailCover;
                        contentEasy.thumbnailPath= realmResultsContentsBox.get(i).thumbnailPath;
                        contentsEasy.add(contentEasy);
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        counterContents= 0;
        downloaderContent = DownloaderDoc.newInstance(listenerDownloadContent);
        downloaderCover = DownloaderDoc.newInstance(listenerDownloadContentCover);
        Dbg.p("CONTENUTI: "+contentsEasy.size());
        downloadContent();
    }



    private void downloadContent() {
        serviceDetroyed= Hawk.get("contentsServiceDestroy", false);
        if(serviceDetroyed){
            Dbg.p("serviceDetroyed non vado oltre");
            stopSelf();
        }else {
            if (counterContents < contentsEasy.size()) {
                if (counterContents > 0) {
                    sendUpdate(counterContents * 100 / contentsEasy.size());
                }
               bContentDownload= false;
                bContentCoverDownload= false;
                ContentEasy contentEasy = contentsEasy.get(counterContents);
                //CONTENT
                String pathContent = contentEasy.resourcePath + contentEasy.filename;
                Uri uriContents = Uri.parse(pathContent.replace(" ", "%20"));
                String filenameContent = contentEasy.lastUpdate + "___" + contentEasy.filename;
                File file = new File(getExternalFilesDir("contents"), filenameContent);
                if (file.exists()) {
                    //Dbg.p("FILE ESISTE: "+filenameContent);
                    updateContent(file.getAbsolutePath(), contentEasy.id);
                    bContentDownload= true;
                    checkContentDownload();
                } else {
                    downloaderContent.download(uriContents, "contents", filenameContent, contentEasy.id);
                }

                String pathCover = contentEasy.thumbnailPath+contentEasy.thumbnailCover;
                Uri uriContentCover = Uri.parse(pathCover.replace(" ", "%20"));
                String filenameContentCover = contentEasy.lastUpdate + "___" + contentEasy.thumbnailCover;
                File fileCover = new File(getExternalFilesDir("contents"), filenameContentCover);
                if(fileCover.exists()){
                    bContentCoverDownload= true;
                    checkContentDownload();
                }else{
                    downloaderCover.download(uriContentCover, "contents", filenameContentCover, contentEasy.id);
                }
            } else {
                Dbg.p("downloadContent finito: ");
              downloadFolderCover();
            }
        }
    }

    private void downloadFolderCover() {


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
            Dbg.p("FILE SCARICATO: " + uri+ "ID: "+id);
            updateContent(uri.toString(), id);
            bContentDownload= true;
            checkContentDownload();
        }

        @Override
        public void downloadError(String error, int errortype, int id) {
            Dbg.p("FILE ERRROR: "+id);

        }

        @Override
        public Context getContext() {
            return getApplicationContext();
        }
    };

    private void checkContentDownload() {
        if(bContentCoverDownload && bContentDownload){
            counterContents++;
            downloadContent();
        }
    }

    private ListenerDowloadDoc listenerDownloadContentCover= new ListenerDowloadDoc() {
        @Override
        public void fileDownloaded(Uri uri, String mimeType, int id) {
            Dbg.p("FILE SCARICATO: " + uri+ "ID: "+id);
            updateContentCover(uri.toString(), id);
            bContentCoverDownload= true;
            checkContentDownload();

        }

        @Override
        public void downloadError(String error, int errortype, int id) {
            Dbg.p("FILE ERRROR: "+id);

        }

        @Override
        public Context getContext() {
            return getApplicationContext();
        }
    };

    private void updateContentCover(final String uri, final int id) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    ContentBox model = realm.where(ContentBox.class).equalTo("id", id).findFirst();
                    if (model != null) {
                        model.setLocalthumbnailPath(uri);
                    }
                }
            });
        } finally {
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
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }


}
