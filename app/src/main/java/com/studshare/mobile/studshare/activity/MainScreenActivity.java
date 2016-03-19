package com.studshare.mobile.studshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.net.Uri;
import java.io.File;
import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.other.CustomList;
import com.studshare.mobile.studshare.other.NotesList;
import com.studshare.mobile.studshare.service.NoteManager;
import com.studshare.mobile.studshare.service.ProfileManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MainScreenActivity extends AppCompatActivity {

    ListView list;
    ProfileManager profileManager = new ProfileManager();
    NoteManager noteManager = new NoteManager();
    NotesList notesList = new NotesList();
    EditText txtSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_screen);

        txtSearch = (EditText)this.findViewById(R.id.txtSearch);

        loadUserNotes(null);
    }

    public void loadNotesByQuery(View view) {
        // Used to load list of notes depend on specified text in search box.

        String tags = txtSearch.getText().toString();

        int numberOfNotes = noteManager.getNumberOfNotesByQuery(tags);

        if (numberOfNotes > 0) {
            int index = 0;
            String[] names = new String[numberOfNotes];
            Integer[] imageId = new Integer[numberOfNotes];

            notesList.setList(new int[numberOfNotes]);

            ResultSet rsFoundNotes = noteManager.getNotesByQuery(tags);

            try {
                while (rsFoundNotes.next()) {
                    notesList.add(index, rsFoundNotes.getInt(1));
                    names[index] = rsFoundNotes.getString(2);    //Get note title

                    if (rsFoundNotes.getString(3).equals("photo")) {
                        imageId[index] = R.drawable.camera;
                    } else if (rsFoundNotes.getString(3).equals("text")) {
                        imageId[index] = R.drawable.text;
                    }
                    else {
                        imageId[index] = R.drawable.file2;
                    }

                    index++;
                }

                CustomList adapter = new CustomList(MainScreenActivity.this, names, imageId, R.layout.list_single_bluetext);

                list = (ListView) findViewById(android.R.id.list);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Open preview window
                        notesList.setChosenID((int)id);

                        if (noteManager.getNoteType(notesList.getItem(notesList.getChosenID())).equals("photo"))
                        {
                            Intent goToNextActivity = new Intent(getApplicationContext(), PhotoNotePreviewScreenActivity.class);
                            goToNextActivity.putExtra("SHOW_TOOLBAR", Boolean.FALSE);
                            startActivity(goToNextActivity);
                        }

                        // !!! TUTAJ DODAC PODGLAD NOTATKI TESKTOWEJ
                        else if(noteManager.getNoteType(notesList.getItem(notesList.getChosenID())).equals("text"))
                        {
                            Intent goToNextActivity = new Intent(getApplicationContext(), Sample.class);
                            startActivity(goToNextActivity);
                        }

                        else
                        {
                            noteManager.getFileData(notesList.getItem(notesList.getChosenID()));

                            String savePath = "/mnt/emmc/dcim/note2.pdf";

                            String videoUrl = savePath;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setDataAndType(Uri.parse(videoUrl),"*");
                            startActivity(i);
                        }
                    }
                });
            }
            catch (SQLException sqle) {
                return;
            }
            catch (Exception e) {
                return;
            }
        }
    }

    public void loadUserNotes(View view) {
        int numberOfNotes = noteManager.getNumberOfUserNotes();

        if (numberOfNotes > 0) {
            int index = 0;
            String[] names = new String[numberOfNotes];
            Integer[] imageId = new Integer[numberOfNotes];

            notesList.setList(new int[numberOfNotes]);

            ResultSet rsUserNotes = noteManager.getAllUserNotes();

            try {
                while (rsUserNotes.next()) {
                    notesList.add(index, rsUserNotes.getInt(1));
                    names[index] = rsUserNotes.getString(2);    //Get note title

                    if (rsUserNotes.getString(3).equals("photo")) {
                        imageId[index] = R.drawable.camera;
                    } else if (rsUserNotes.getString(3).equals("text")) {
                        imageId[index] = R.drawable.text;
                    }
                    else {
                        imageId[index] = R.drawable.file2;
                    }

                    index++;
                }

                CustomList adapter = new CustomList(MainScreenActivity.this, names, imageId, R.layout.list_single_bluetext);

                list = (ListView) findViewById(android.R.id.list);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Open preview window
                        notesList.setChosenID((int)id);

                        if (noteManager.getNoteType(notesList.getItem(notesList.getChosenID())).equals("photo"))
                        {

                            Intent goToNextActivity = new Intent(getApplicationContext(), PhotoNotePreviewScreenActivity.class);
                            startActivity(goToNextActivity);
                        }

                        else if(noteManager.getNoteType(notesList.getItem(notesList.getChosenID())).equals("text"))
                        {
                            Intent goToNextActivity = new Intent(getApplicationContext(), Sample.class);
                            startActivity(goToNextActivity);
                        }

                        else
                        {
                            noteManager.getFileData(notesList.getItem(notesList.getChosenID()));

                            String savePath = "/mnt/emmc/dcim/note2.pdf";

                            String videoUrl = savePath;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setDataAndType(Uri.parse(videoUrl),"*/*");
                            startActivity(i);
                        }
                    }
                });
            }
            catch (SQLException sqle) {
                return;
            }
            catch (Exception e) {
                return;
            }
        }
    }

    public void goToProfileSettings(View view) {
        Intent goToNextActivity = new Intent(getApplicationContext(), ProfileScreenActivity.class);
        startActivity(goToNextActivity);
    }

    public void goToAddScreen(View view) {
        Intent goToNextActivity = new Intent(getApplicationContext(), AddScreenActivity.class);
        startActivity(goToNextActivity);
    }
}
