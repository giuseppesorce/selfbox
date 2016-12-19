package com.docgenerici.selfbox.android.downloader;

import android.content.Context;
import android.net.Uri;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2016.
 *         <p>......</p>
 */

public interface ListenerDowloadDoc {
   void fileDownloaded(Uri uri, String mimeType, int id);
    void downloadError(String error, int errortype, int id);
    Context getContext();
}