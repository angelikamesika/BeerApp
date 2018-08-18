package com.example.angelika.beerapp.utils;

import android.content.Context;

import java.io.File;

/**
 * Created by Angelika on 02.08.2018.
 */

public class FileUtils {
    public static void createDirectory(String aPath) {
        File fileDir = new File(aPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }

    public static String basePath(Context context) {
        return context.getFilesDir() + "/beerapp";
    }
}
