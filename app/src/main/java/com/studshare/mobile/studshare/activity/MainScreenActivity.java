package com.studshare.mobile.studshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
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
    ImageButton butSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_screen);

        txtSearch = (EditText)this.findViewById(R.id.txtSearch);
        butSearch = (ImageButton)this.findViewById(R.id.butSearch);

        Bundle extras = getIntent().getExtras();

        // Specify which data we populate the list
        if (extras != null) {
            txtSearch.setText(extras.getString("LIST_FILTER"));
            loadNotesByQuery(null);
        }
        else {
            loadUserNotes(null);
        }
    }

    private void addNoteToList(int idNote, int index, String[] names, String title) {

        String rating = "";
        double noteRating = noteManager.getNoteRating(idNote);

        if (noteRating < 0)
            rating = "brak ocen";
        else
            rating = String.valueOf(noteRating);

        notesList.add(index, idNote);
        names[index] = title + "   (" + rating + ")";    //Get note title
    }

    public void loadNotesByQuery(View view) {
        // Used to load list of notes depend on specified text in search box.

        String tags = txtSearch.getText().toString();

        if (tags.equals(""))
            return;

        int numberOfNotes = noteManager.getNumberOfNotesByQuery(tags);

        if (numberOfNotes > 0) {
            int index = 0;
            String[] names = new String[numberOfNotes];
            Integer[] imageId = new Integer[numberOfNotes];

            notesList.setList(new int[numberOfNotes]);

            ResultSet rsFoundNotes = noteManager.getNotesByQuery(tags);

            try {
                while (rsFoundNotes.next()) {

                    addNoteToList(rsFoundNotes.getInt(1), index, names, rsFoundNotes.getString(2));

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
                            goToNextActivity.putExtra("LIST_FILTER", txtSearch.getText().toString());
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

                            Intent goToNextActivity = new Intent(getApplicationContext(), ChooseFileActionActivity.class);
                            goToNextActivity.putExtra("USERS_NOTE", noteManager.userHasNote(notesList.getItem(notesList.getChosenID()), profileManager.getUserID()));
                            goToNextActivity.putExtra("LIST_FILTER", txtSearch.getText().toString());
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

                    addNoteToList(rsUserNotes.getInt(1), index, names, rsUserNotes.getString(2));

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

                            Intent goToNextActivity = new Intent(getApplicationContext(), ChooseFileActionActivity.class);
                            goToNextActivity.putExtra("USERS_NOTE", noteManager.userHasNote(notesList.getItem(notesList.getChosenID()), profileManager.getUserID()));
                            goToNextActivity.putExtra("LIST_FILTER", txtSearch.getText().toString());
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
