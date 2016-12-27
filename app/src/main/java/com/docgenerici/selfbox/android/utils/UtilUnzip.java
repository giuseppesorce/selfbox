package com.docgenerici.selfbox.android.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2016.
 *         <p>......</p>
 */

public class UtilUnzip {

    public static void unzip(File filetoUnzip) {
        String zipName = "data.zip";

        try {
            FileInputStream fis = new FileInputStream(zipName);
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(fis));
            ZipEntry entry;

            //
            // Read each entry from the ZipInputStream until no
            // more entry found indicated by a null return value
            // of the getNextEntry() method.
            //
            try {
                while ((entry = zis.getNextEntry()) != null) {
                    System.out.println("Unzipping: " + entry.getName());

                    int size;
                    byte[] buffer = new byte[2048];

                    FileOutputStream fos =
                            new FileOutputStream(entry.getName());
                    BufferedOutputStream bos =
                            new BufferedOutputStream(fos, buffer.length);

                    while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, size);
                    }
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}