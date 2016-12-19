package com.docgenerici.selfbox.android.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;

import com.docgenerici.selfbox.debug.Dbg;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static long dateConvertNumber(final String dateStr){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date convertedDate = null;
        try {
            convertedDate = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         if(convertedDate !=null){
            return  convertedDate.getTime();
        }else{
            return  new Date().getTime();
        }
    }
}
