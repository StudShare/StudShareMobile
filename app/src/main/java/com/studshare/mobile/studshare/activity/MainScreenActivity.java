package com.studshare.mobile.studshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_screen);

        loadUserNotes();
    }

    private void loadUserNotes() {
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
                    } else {
                        imageId[index] = R.drawable.text;
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

                        if (noteManager.getNoteType(notesList.getItem(notesList.getChosenID())).equals("photo")) {
                            Intent goToNextActivity = new Intent(getApplicationContext(), PhotoNotePreviewScreenActivity.class);
                            startActivity(goToNextActivity);
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
