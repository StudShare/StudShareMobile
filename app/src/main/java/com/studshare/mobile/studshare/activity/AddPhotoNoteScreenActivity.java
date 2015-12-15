package com.studshare.mobile.studshare.activity;

import android.graphics.BitmapFactory;
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
import com.studshare.mobile.studshare.service.ConnectionManager;
import com.studshare.mobile.studshare.service.ProfileManager;

import java.io.ByteArrayOutputStream;


public class AddPhotoNoteScreenActivity extends AppCompatActivity {

    //private ImageView imageView;
    private EditText txtTitle;
    private EditText txtTags;
    private CameraPhoto cp = new CameraPhoto();
    ConnectionManager connectionManager = new ConnectionManager();
    ProfileManager profileManager = new ProfileManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addphotonote_screen);

        txtTitle = (EditText)this.findViewById(R.id.txtTitle);
        txtTags = (EditText)this.findViewById(R.id.txtTags);

        //this.imageView = (ImageView)this.findViewById(R.id.imageView1);


        /* Pobieranie
        query = "SELECT pictureContent FROM Note WHERE idNote = 1";
        ResultSet rs = connectionManager.SendQuery(query);

        try {
            if (rs.next()) {
                Bitmap receivedPhoto = base64ToBitmap(rs.getString(1));
                //imageView.setImageBitmap(receivedPhoto);
            }
        }
        catch (SQLException sqle) {
            return;
        }
        catch (Exception e) {
            return;
        }*/
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

        String query = "INSERT INTO Note(idSiteUser, title, textContent, pictureContent) VALUES (" + profileManager.getUserID() + ", '" + title + "', '', '" + bitmapToBase64(bitmap) + "')";
        int result = connectionManager.SendUpdate(query);

        if (result == -1) {
            ShowMessage.Show(getApplicationContext(), "Wystąpił błąd dodawania zdjęcia do bazy. Proszę sprawdzić połączenie internetowe.");
        }

        Intent goToNextActivity = new Intent(getApplicationContext(), MainScreenActivity.class);
        startActivity(goToNextActivity);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
}