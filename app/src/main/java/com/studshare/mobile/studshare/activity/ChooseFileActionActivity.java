package com.studshare.mobile.studshare.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.io.File;
import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.other.CustomList;
import com.studshare.mobile.studshare.other.NotesList;
import com.studshare.mobile.studshare.other.ShowMessage;
import com.studshare.mobile.studshare.service.NoteManager;

public class ChooseFileActionActivity extends AppCompatActivity {

    ListView list;
    String[] web;
    Integer[] imageId;

    Boolean noteBelongToUser;
    String listFilter;

    NoteManager noteManager = new NoteManager();
    private NotesList notesList = new NotesList();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.choose_file_action_layout);

        // Adding elements to lists
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listFilter = extras.getString("LIST_FILTER");
            noteBelongToUser = extras.getBoolean("USERS_NOTE");
        }

        if (noteBelongToUser) {
            web = new String[] { "Wyświetl plik", "Edytuj", "Usuń" };
            imageId = new Integer[] { R.drawable.open, R.drawable.edit2, R.drawable.delete2 };
        }
        else {
            web = new String[] { "Wyświetl plik", "Oceń" };
            imageId = new Integer[] { R.drawable.open, R.drawable.rate2 };
        }

        CustomList adapter = new CustomList(ChooseFileActionActivity.this, web, imageId, R.layout.list_single_whitetext);

        list = (ListView)findViewById(android.R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {

                    String savePath = "/mnt/emmc/dcim/note2.pdf";
                    File file = new File(savePath);

                    if(!file.exists())      //to check ADAM
                        savePath = "/storage/sdcard/Download/note.pdf";

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setDataAndType(Uri.parse(savePath),"*/*");
                    startActivity(i);
                }
                else if (id == 1 && noteBelongToUser) {
                    Intent goToNextActivity = new Intent(getApplicationContext(), EditPhotoNoteScreenActivity.class);
                    startActivity(goToNextActivity);
                }
                else if (id == 1 && !noteBelongToUser) {
                    Intent goToNextActivity = new Intent(getApplicationContext(), RateScreenActivity.class);
                    goToNextActivity.putExtra("IDNOTE", notesList.getItem(notesList.getChosenID()));
                    goToNextActivity.putExtra("LIST_FILTER", listFilter);
                    startActivity(goToNextActivity);
                }
                else  if (id == 2 && noteBelongToUser) {
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
                                        ShowMessage.Show(getApplicationContext(), "Wystąpił błąd usuwania pliku z bazy. Proszę sprawdzić połączenie internetowe.");
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
            }
        });
    }

    public void goToMainScreen(View view) {
        Intent goToNextActivity = new Intent(getApplicationContext(), MainScreenActivity.class);
        startActivity(goToNextActivity);
    }
}
