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
import com.studshare.mobile.studshare.service.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoNotePreviewScreenActivity extends AppCompatActivity {

    private ImageView imageView;
    private NotesList notesList = new NotesList();
    ConnectionManager connectionManager = new ConnectionManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.photonotepreview_screen);

        imageView = (ImageView)findViewById(R.id.photoPreview);

        try {
            loadPhoto(notesList.getChosenID());
        }
        catch (Exception e) {
            return;
        }
    }

    private void loadPhoto(int index) {
        String query = "SELECT pictureContent FROM Note WHERE idNote = " + notesList.getItem(index);

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

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

}
