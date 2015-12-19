package com.studshare.mobile.studshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.other.NotesList;
import com.studshare.mobile.studshare.other.ShowMessage;
import com.studshare.mobile.studshare.service.NoteManager;


public class EditPhotoNoteScreenActivity extends AppCompatActivity {

    private EditText txtTitle;
    private EditText txtTags;
    NoteManager noteManager = new NoteManager();
    NotesList notesList = new NotesList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.editphotonotetitle_screen);

        txtTitle = (EditText)this.findViewById(R.id.txtTitle);
        txtTags = (EditText)this.findViewById(R.id.txtTags);

        //wczytac tytul i tagi
        txtTitle.setText(noteManager.getNoteTitle(notesList.getItem(notesList.getChosenID())));
    }

    public void trySave(View view) {

        String title = txtTitle.getText().toString();
        String tags = txtTags.getText().toString();

        if (title.trim().equals("") || tags.trim().equals("")) {
            ShowMessage.Show(getApplicationContext(), "Proszę uzupełnić wszystkie pola");
            return;
        }

        boolean result = noteManager.updateTitle(notesList.getItem(notesList.getChosenID()), title);

        if (result)
        {
            Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goToNextActivity);
        }
        else {
            ShowMessage.Show(getApplicationContext(), "Wystąpił błąd edycji zdjęcia. Proszę sprawdzić połączenie internetowe.");
        }
    }

}
