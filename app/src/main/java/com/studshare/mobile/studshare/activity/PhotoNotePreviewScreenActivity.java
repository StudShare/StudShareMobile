package com.studshare.mobile.studshare.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.other.NotesList;
import com.studshare.mobile.studshare.other.ShowMessage;
import com.studshare.mobile.studshare.service.NoteManager;


public class PhotoNotePreviewScreenActivity extends AppCompatActivity {

    private ImageView imageView;
    private NotesList notesList = new NotesList();
    NoteManager noteManager = new NoteManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.photonotepreview_screen);

        imageView = (ImageView)findViewById(R.id.photoPreview);

        loadPhoto(notesList.getChosenID());
    }

    private void loadPhoto(int index) {

        String receivedBitmap = noteManager.getNotePictureContent(notesList.getItem(index));

        if (receivedBitmap != null) {
            Bitmap receivedPhoto = base64ToBitmap(receivedBitmap);
            imageView.setImageBitmap(receivedPhoto);
        }
        else {
            ShowMessage.Show(getApplicationContext(), "Wystąpił błąd odczytu zdjęcia z bazy. Proszę sprawdzić połączenie internetowe.");
        }
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

}
