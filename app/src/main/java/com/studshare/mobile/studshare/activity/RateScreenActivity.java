package com.studshare.mobile.studshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.other.ShowMessage;
import com.studshare.mobile.studshare.service.NoteManager;

public class RateScreenActivity extends AppCompatActivity {

    NoteManager noteManager = new NoteManager();

    int Rate = 1;
    int idNote = -1;
    Button butSave;
    ImageButton[] stars = new ImageButton[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rate_screen);

        butSave = (Button) findViewById(R.id.butSave);
        stars[1] = (ImageButton) findViewById(R.id.butStar1);
        stars[2] = (ImageButton) findViewById(R.id.butStar2);
        stars[3] = (ImageButton) findViewById(R.id.butStar3);
        stars[4] = (ImageButton) findViewById(R.id.butStar4);
        stars[5] = (ImageButton) findViewById(R.id.butStar5);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idNote = extras.getInt("IDNOTE");
        }
    }

    public void starTapped(View view) {
        switch (view.getId()) {
            case R.id.butStar1:
                Rate = 1;
                break;
            case R.id.butStar2:
                Rate = 2;
                break;
            case R.id.butStar3:
                Rate = 3;
                break;
            case R.id.butStar4:
                Rate = 4;
                break;
            case R.id.butStar5:
                Rate = 5;
                break;
        }

        for (int i = 1; i<stars.length; i++) {

            if (i <= Rate) {
                stars[i].setImageResource(R.drawable.full_star);
            }
            else {
                stars[i].setImageResource(R.drawable.empty_star);
            }
        }
    }

    public void doRate(View view) {
        int result = noteManager.saveNoteRating(idNote, Rate);

        if (result != 1) {
            ShowMessage.Show(getApplicationContext(), "Nie udało się dodać oceny. Spróbuj później.");
        }

        Intent goToNextActivity = new Intent(getApplicationContext(), MainScreenActivity.class);
        startActivity(goToNextActivity);
    }

}
