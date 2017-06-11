package com.test.blur.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhaolin on 17-6-11.
 */

public class FileUtils {

    private static final String FILE_NAME_PREFIX = "blur_";

    private static final String FILE_NAME_SUFFIX = ".png";

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

        String name = saveDir + File.separator + FILE_NAME_PREFIX + date + FILE_NAME_SUFFIX;
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
