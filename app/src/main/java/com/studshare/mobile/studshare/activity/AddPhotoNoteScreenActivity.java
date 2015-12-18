package com.studshare.mobile.studshare.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import android.content.Intent;
import android.graphics.Bitmap;

import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.other.CameraPhoto;
import com.studshare.mobile.studshare.other.ShowMessage;
import com.studshare.mobile.studshare.service.NoteManager;
import com.studshare.mobile.studshare.service.ProfileManager;

import java.io.ByteArrayOutputStream;


public class AddPhotoNoteScreenActivity extends AppCompatActivity {

    private EditText txtTitle;
    private EditText txtTags;
    private CameraPhoto cp = new CameraPhoto();
    NoteManager noteManager = new NoteManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addphotonote_screen);

        txtTitle = (EditText)this.findViewById(R.id.txtTitle);
        txtTags = (EditText)this.findViewById(R.id.txtTags);
    }

    public void tryAdd(View view) {

        String title = txtTitle.getText().toString();
        String tags = txtTags.getText().toString();

        if (title.trim().equals("") || tags.trim().equals("")) {
            ShowMessage.Show(getApplicationContext(), "Proszę uzupełnić wszystkie pola");
            return;
        }

        //Getting taken photo
        Bitmap bitmap = cp.getPhoto();

        ProfileManager.OperationStatus result = noteManager.add(title, bitmap);

        if (result == ProfileManager.OperationStatus.OtherError) {
            ShowMessage.Show(getApplicationContext(), "Wystąpił błąd dodawania zdjęcia do bazy. Proszę sprawdzić połączenie internetowe.");
        }

        Intent goToNextActivity = new Intent(getApplicationContext(), MainScreenActivity.class);
        startActivity(goToNextActivity);
    }

}
