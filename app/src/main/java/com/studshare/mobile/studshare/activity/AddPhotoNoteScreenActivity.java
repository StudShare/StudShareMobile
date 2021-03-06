package com.studshare.mobile.studshare.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.os.Environment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.other.CameraPhoto;
import com.studshare.mobile.studshare.other.ShowMessage;
import com.studshare.mobile.studshare.service.NoteManager;
import com.studshare.mobile.studshare.service.ProfileManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;


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

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            ext  = extras.getString("extension");
            pathToFile  = extras.getString("fileToPass");
        }
    }

    String ext=Save_text.type_note;
    String pathToFile="null";

    public void tryAdd(View view) {

        String title = txtTitle.getText().toString();
        String tags = txtTags.getText().toString();

        if (title.trim().equals("") || tags.trim().equals("")) {
            ShowMessage.Show(getApplicationContext(), "Proszę uzupełnić wszystkie pola");
            return;
        }
        ProfileManager.OperationStatus result;
        //Getting taken photo
        Bitmap bitmap = cp.getPhoto();

        if (ext.equals("photo"))
            result = noteManager.add(title, bitmap, ext, tags);

        else if(!ext.equals("pdf")){
            FileInputStream fis = null;
            try
            {
                File file = new File(pathToFile);
                fis = new FileInputStream(file);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            String textt= Save_text.getCont();
            result = noteManager.add2(title, textt, ext, txtTags.getText().toString());

        }
        else {

            //przygotowanie pliku do przesyłu binarnego

            FileInputStream fis = null;
            File file = null;
            try
            {
                file = new File(pathToFile);
                fis = new FileInputStream(file);



            } catch (Exception e2) {
                e2.printStackTrace();
            }
            result = noteManager.add3(title, fis, ext, file, txtTags.getText().toString());

        }

        if (result == ProfileManager.OperationStatus.OtherError) {
            ShowMessage.Show(getApplicationContext(), "Wystąpił błąd dodawania zdjęcia do bazy. Proszę sprawdzić połączenie internetowe.");
        }

        Intent goToNextActivity = new Intent(getApplicationContext(), MainScreenActivity.class);
        startActivity(goToNextActivity);
    }

    public void getFilesFromDir(File filesFromSD) {

        File listAllFiles[] = filesFromSD.listFiles();

        if (listAllFiles != null && listAllFiles.length > 0) {
            for (File currentFile : listAllFiles) {
                if (currentFile.isDirectory()) {
                    getFilesFromDir(currentFile);
                } else {
                    if (currentFile.getName().endsWith("")) {
                        // File absolute path
                        Log.e("GGGGFile path", currentFile.getAbsolutePath());
                        // File Name
                        Log.e("GGGGFile path", currentFile.getName());

                    }
                }
            }
        }
    }

}
