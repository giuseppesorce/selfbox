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

    public static String  getCategoryColor(String categoria_farmacologica) {
        Dbg.p("getCategoryColor categoria_farmacologica "+ categoria_farmacologica);
        if(categoria_farmacologica.contains("ltri prodotti terapeutici")){
            return "#c3ab36";
        }else if(categoria_farmacologica.toLowerCase().contains("ardiovascolari")){
            return "#b71b3b";
        }else if(categoria_farmacologica.toLowerCase().contains("ematologici")){

            return "#fdc461";
        }else if(categoria_farmacologica.toLowerCase().contains("gastroenterici")){
            return "#66c298";
        }else if(categoria_farmacologica.toLowerCase().contains("dermatologici")){

            return "#adb4da";
        }else if(categoria_farmacologica.toLowerCase().contains("antineoplastici")){
            return "#be9b6d";

        }else if(categoria_farmacologica.toLowerCase().contains("genito-urinari")){
            return "#6ec5aa";
        }else if(categoria_farmacologica.toLowerCase().contains("neurologici")){
            return "#243c96";

        }else if(categoria_farmacologica.toLowerCase().contains("organi di senso")){
            return "#00a78d";
        }else if(categoria_farmacologica.toLowerCase().contains("preparati ormonali")){
            return "#a7278d";

        }else if(categoria_farmacologica.toLowerCase().contains("respiratori")){
            return "#89bee5";

        }else if(categoria_farmacologica.toLowerCase().contains("sistema muscolo")){
            return "#00708a";
        }else if(categoria_farmacologica.toLowerCase().contains("antiinfettivi")){
            Dbg.p("antiifettivi ritorno");
            return "#fff68f";
        }
        return "#000000";
    }
}
