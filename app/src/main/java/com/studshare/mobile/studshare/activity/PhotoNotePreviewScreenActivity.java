package com.studshare.mobile.studshare.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.other.NotesList;
import com.studshare.mobile.studshare.other.ShowMessage;
import com.studshare.mobile.studshare.service.NoteManager;
import com.studshare.mobile.studshare.service.ProfileManager;


public class PhotoNotePreviewScreenActivity extends AppCompatActivity {

    private ImageView imageView;
    private NotesList notesList = new NotesList();
    NoteManager noteManager = new NoteManager();
    ProfileManager profileManager = new ProfileManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.photonotepreview_screen);

        imageView = (ImageView)findViewById(R.id.photoPreview);

        loadPhoto(notesList.getChosenID());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Boolean showToolbar = extras.getBoolean("SHOW_TOOLBAR");

            if (!showToolbar) {
                Boolean noteBelongToUser = noteManager.userHasNote(notesList.getItem(notesList.getChosenID()), profileManager.getUserID());

                if (!noteBelongToUser) {
                    LinearLayout rate = (LinearLayout)findViewById(R.id.s5);
                    LinearLayout delete = (LinearLayout)findViewById(R.id.s4);
                    LinearLayout edit = (LinearLayout)findViewById(R.id.s3);

                    rate.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);
                    edit.setVisibility(View.GONE);
                }
            }
        }
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

    public void doDelete(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        boolean deleteSuccessful = noteManager.delete(notesList.getItem(notesList.getChosenID()));

                        if (deleteSuccessful)
                        {
                            Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(goToNextActivity);
                        }
                        else {
                            ShowMessage.Show(getApplicationContext(), "Wystąpił błąd usuwania zdjęcia z bazy. Proszę sprawdzić połączenie internetowe.");
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Czy na pewno chcesz usunąć tę notatkę?").setPositiveButton("Tak", dialogClickListener).setNegativeButton("Nie", dialogClickListener).show();
    }

    public void doEdit(View view) {
        Intent goToNextActivity = new Intent(getApplicationContext(), EditPhotoNoteScreenActivity.class);
        startActivity(goToNextActivity);
    }

    public void doRate(View view) {
        Intent goToNextActivity = new Intent(getApplicationContext(), RateScreenActivity.class);
        goToNextActivity.putExtra("IDNOTE", notesList.getItem(notesList.getChosenID()));
        startActivity(goToNextActivity);
    }

}
