package com.studshare.mobile.studshare.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.other.CameraPhoto;
import com.studshare.mobile.studshare.other.CustomList;
import com.studshare.mobile.studshare.service.ProfileManager;

public class AddScreenActivity extends AppCompatActivity {

    ListView list;
    String[] web = {
            "Notatka tekstowa",
            "Zrób i prześlij zdjęcie",
            "Wybierz plik"
    } ;
    Integer[] imageId = {
            R.drawable.text,
            R.drawable.camera,
            R.drawable.file
    };

    private static final int CAMERA_REQUEST = 1888;

    ProfileManager profileManager = new ProfileManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_screen);

        CustomList adapter = new CustomList(AddScreenActivity.this, web, imageId);

        list = (ListView)findViewById(android.R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 1) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            CameraPhoto cp = new CameraPhoto();
            cp.setPhoto(photo);

            View view = findViewById(android.R.id.content);

            Intent goToNextActivity = new Intent(view.getContext(), AddPhotoNoteScreenActivity.class);
            startActivity(goToNextActivity);
        }
    }

    public void goToMainScreen(View view) {
        Intent goToNextActivity = new Intent(getApplicationContext(), MainScreenActivity.class);
        startActivity(goToNextActivity);
    }

}