package com.studshare.mobile.studshare.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.other.CameraPhoto;
import com.studshare.mobile.studshare.other.ShowMessage;


public class AddPhotoNoteScreenActivity extends AppCompatActivity {

    private ImageView imageView;
    private CameraPhoto cp = new CameraPhoto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addphotonote_screen);

        this.imageView = (ImageView)this.findViewById(R.id.imageView1);

        imageView.setImageBitmap(cp.getPhoto());
    }
}
