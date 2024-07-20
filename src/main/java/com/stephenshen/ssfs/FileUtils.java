package com.stephenshen.ssfs;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.UUID;

/**
 * utils for file.
 * @author stephenshen
 * @date 2024/7/20 17:54:43
 */
public class FileUtils {

    static String DEFAULT_MIME_TYPE = "application/octet-stream";

    public static String getMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentType = fileNameMap.getContentTypeFor(fileName);
        return contentType == null ? DEFAULT_MIME_TYPE : contentType;
    }

    public static void init(String uploadPath) {
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (int i = 0; i < 256; i++) {
            String sub = String.format("%02x", i);
            File file = new File(uploadPath + "/" + sub);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    public static String getUUIDFile(String fileName) {
        return UUID.randomUUID() + getExt(fileName);
    }

    public static String getSubDir(String fileName) {
        return fileName.substring(0, 2);
    }

    public static String getExt(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }
}
