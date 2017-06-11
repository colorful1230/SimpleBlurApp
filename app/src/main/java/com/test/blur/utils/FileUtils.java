package com.test.blur.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Created by zhaolin on 17-6-11.
 */

public class FileUtils {

    private static final String FILE_NAME_PREFIX = "blur_";

    private static final String FILE_NAME_SUFFFIX = ".png";

    @Nullable
    public static String saveBitmap(Bitmap bitmap) {
        String saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .getAbsolutePath() + File.separator + "blur";
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(new Date());

        String name = saveDir + File.separator + FILE_NAME_PREFIX + date + FILE_NAME_SUFFFIX;
        File file = new File(name);
        if (file != null) {
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                outputStream.flush();
                return file.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (Exception e) {

                }
            }

        }
        return null;
    }
}
