package com.studshare.mobile.studshare.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.io.File;
import java.io.FileInputStream;
import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.other.CameraPhoto;
import com.studshare.mobile.studshare.other.CustomList;

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
    private static final int CHOOSE_FILE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_screen);

        CustomList adapter = new CustomList(AddScreenActivity.this, web, imageId, R.layout.list_single_whitetext);

        list = (ListView)findViewById(android.R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 1) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                else if (id == 2) {
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("file/*");
                    Intent c = Intent.createChooser(chooseFile, "Wybierz plik");
                    startActivityForResult(c, CHOOSE_FILE);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        View view = findViewById(android.R.id.content);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            CameraPhoto cp = new CameraPhoto();
            cp.setPhoto(photo);

            Intent goToNextActivity = new Intent(view.getContext(), AddPhotoNoteScreenActivity.class);
            startActivity(goToNextActivity);
        }
        else if (requestCode == CHOOSE_FILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String filePath = uri.getPath();

            try {
                //
                // Picture taken from gallery or other place on the device need to be shrinked first.
                //
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, o);

                // The new size we want to scale to
                final int REQUIRED_SIZE = 1024;

                // Find the correct scale value. It should be the power of 2.
                int width_tmp = o.outWidth, height_tmp = o.outHeight;
                int scale = 1;
                while (true)
                {
                    if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                        break;
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale *= 2;
                }

                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);

                CameraPhoto cp = new CameraPhoto();
                cp.setPhoto(bitmap);
                //cp.setFilePath(filePath);

                Intent goToNextActivity = new Intent(view.getContext(), AddPhotoNoteScreenActivity.class);
                goToNextActivity.putExtra("extension", "photo");
                startActivity(goToNextActivity);
            }
            catch (Exception e)
            {
                // to nie obrazek


               // Uri fileUri = data.getData();
                String FilePath = data.getData().getPath();



                int dotposition= FilePath.lastIndexOf(".");
                String filename_Without_Ext = FilePath.substring(0,dotposition);
                String extTemp = FilePath.substring(dotposition + 1, FilePath.length());

                Intent goToNextActivity = new Intent(view.getContext(), AddPhotoNoteScreenActivity.class);
                goToNextActivity.putExtra("extension", extTemp);
                goToNextActivity.putExtra("fileToPass", FilePath);
                startActivity(goToNextActivity);
            }


        }
    }




    public void goToMainScreen(View view) {
        Intent goToNextActivity = new Intent(getApplicationContext(), MainScreenActivity.class);
        startActivity(goToNextActivity);
    }

}