package com.docgenerici.selfbox.android.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2016.
 *         <p>......</p>
 */



public class StopReceiver extends BroadcastReceiver {
    private boolean stop=false;
    public static final String ACTION_STOP = "stop";

    @Override
    public void onReceive(Context context, Intent intent) {
        stop = true;
    }
}
