package com.docgenerici.selfbox.debug;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Giuseppe Sorce copyright Giuseppe Sorce
 */
public class DateApp {

    public static String getStamp() {
        String format = "dd-MM-yyyy HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(new Date());
    }

    public static String getStampFile() {
        String format = "dd_MM_yyyy_HH_mm_ss";

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(new Date());
    }
}
