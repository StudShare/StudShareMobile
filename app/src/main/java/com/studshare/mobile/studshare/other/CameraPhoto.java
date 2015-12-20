package com.studshare.mobile.studshare.other;

import android.graphics.Bitmap;

public class CameraPhoto {

    private static Bitmap Photo;
    private static String FilePath;

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public Bitmap getPhoto() {
        return Photo;
    }

    public void setPhoto(Bitmap photo) {
        Photo = photo;
    }
}
