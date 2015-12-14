package com.studshare.mobile.studshare.other;

import android.graphics.Bitmap;

public class CameraPhoto {

    private static Bitmap Photo;

    public Bitmap getPhoto() {
        return Photo;
    }

    public void setPhoto(Bitmap photo) {
        Photo = photo;
    }
}
