package com.docgenerici.selfbox.android.downloader;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.docgenerici.selfbox.debug.Dbg;

public class DownloaderDoc implements DownloadReceiver.Listener {
    private final ListenerDowloadDoc listener;
    private final DownloadManager downloadManager;

    private DownloadReceiver receiver = null;

    private long downloadId = -1;
    private int idDownload=0;

    public static DownloaderDoc newInstance(ListenerDowloadDoc listener) {
        Context context = listener.getContext();
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return new DownloaderDoc(downloadManager, listener);
    }

    DownloaderDoc(DownloadManager downloadManager, ListenerDowloadDoc listener) {
        this.downloadManager = downloadManager;
        this.listener = listener;
    }

    public void download(Uri uri, String directory, String filename) {
        if (!isDownloading()) {

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(true);
            request.setVisibleInDownloadsUi(false);
            request.setTitle("My Data Download");
            request.setDestinationInExternalFilesDir(listener.getContext(), directory, filename);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            downloadId = downloadManager.enqueue(request);
            register();
        }
    }


    public void download(Uri uri, String directory, String filename, int id) {
        if (!isDownloading()) {
            idDownload= id;
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(true);
            request.setVisibleInDownloadsUi(false);
            request.setTitle("My Data Download");
            request.setDestinationInExternalFilesDir(listener.getContext(), directory, filename);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            downloadId = downloadManager.enqueue(request);
            register();
        }
    }

    public boolean isDownloading() {
        return downloadId >= 0;
    }

    void register() {
        if (receiver == null && isDownloading()) {
            receiver = new DownloadReceiver(this);
            receiver.register(listener.getContext());
        }
    }

    @Override
    public void downloadComplete(long completedDownloadId) {
        if (downloadId == completedDownloadId) {
            DownloadManager.Query query = new DownloadManager.Query();

            query.setFilterById(downloadId);
            downloadId = -1;
            unregister();
            Cursor cursor = downloadManager.query(query);

            while (cursor.moveToNext()) {

                getFileInfo(cursor);
            }
            cursor.close();
        }
    }

    void unregister() {
        if (receiver != null) {
            receiver.unregister(listener.getContext());
        }
        receiver = null;
    }

    private void getFileInfo(Cursor cursor) {
        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
        if (status == DownloadManager.STATUS_SUCCESSFUL) {
            Long id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            Uri uri = downloadManager.getUriForDownloadedFile(id);
            String mimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
            listener.fileDownloaded(uri, mimeType, idDownload);
        }else{

            Dbg.p("STATUS: "+status);
                Dbg.p("STATUS ERRORE: ");
                int reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
                Log.d("selfbox", "Download not correct, status [" + status + "] reason [" + reason + "]");
                String reasonText="";
                switch(reason){
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
            listener.downloadError(reasonText, reason, idDownload);

        }
    }

    void cancel() {
        if (isDownloading()) {
            downloadManager.remove(downloadId);
            downloadId = -1;
            unregister();
        }
    }


}
