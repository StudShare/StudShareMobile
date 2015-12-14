package com.studshare.mobile.studshare.activity;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.other.CameraPhoto;
import com.studshare.mobile.studshare.other.ShowMessage;
import com.studshare.mobile.studshare.service.ConnectionManager;

import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AddPhotoNoteScreenActivity extends AppCompatActivity {

    private ImageView imageView;
    private CameraPhoto cp = new CameraPhoto();
    ConnectionManager connectionManager = new ConnectionManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addphotonote_screen);

        //Getting taken photo
        Bitmap bitmap = cp.getPhoto();

        this.imageView = (ImageView)this.findViewById(R.id.imageView1);

        String query = "INSERT INTO Note(idSiteUser, title, textContent, pictureContent) VALUES (2, 'aaa', 'brak', '" + bitmapToBase64(bitmap) + "')";
        connectionManager.SendUpdate(query);

        query = "SELECT pictureContent FROM Note WHERE idNote = 1";
        ResultSet rs = connectionManager.SendQuery(query);

        try {
            if (rs.next()) {
                Bitmap receivedPhoto = base64ToBitmap(rs.getString(1));
                imageView.setImageBitmap(receivedPhoto);
            }
        }
        catch (SQLException sqle) {
            return;
        }
        catch (Exception e) {
            return;
        }
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
