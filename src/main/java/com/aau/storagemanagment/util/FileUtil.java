package com.aau.storagemanagment.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf + 1);
    }

    public static String getCategory(File file) {
        String ext = getFileExtension(file).toLowerCase();
        switch (ext) {
            case "png":
            case "jpg":
            case "jpeg":
            case "gif":
                return "Images";
            case "pdf":
            case "doc":
            case "docx":
            case "txt":
                return "Documents";
            case "mp4":
            case "avi":
            case "mov":
                return "Videos";
            case "mp3":
            case "wav":
            case "mp4a":
                return "Audio";
            default:
                return ext.isEmpty() ? "Unknown" : ext.toUpperCase();
        }
    }

    public static String getFormattedSize(long size) {
        if (size < 1024) return size + " B";
        int z = (63 - Long.numberOfLeadingZeros(size)) / 10;
        return String.format("%.1f %sB", (double)size / (1L << (z*10)), " KMGTPE".charAt(z));
    }

    public static String getCreatedDate(File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(new Date(attr.creationTime().toMillis()));
        } catch (IOException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
}
