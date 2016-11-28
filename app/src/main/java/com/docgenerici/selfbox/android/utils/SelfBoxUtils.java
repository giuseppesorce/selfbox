package com.docgenerici.selfbox.android.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;

/**
 * @uthor giuseppesorce
 */

public class SelfBoxUtils {


    public static String getDeviceId(Context context) {

        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return  android_id;

    }
    public static String getApplicationVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException ex) {} catch(Exception e){}
        return "";
    }
}
