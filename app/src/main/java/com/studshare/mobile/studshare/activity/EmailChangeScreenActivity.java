package com.studshare.mobile.studshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.studshare.mobile.studshare.R;

public class EmailChangeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.emailchange_screen);
    }

    public void goToProfileSettings(View view) {
        Intent goToNextActivity = new Intent(getApplicationContext(), ProfileScreenActivity.class);
        startActivity(goToNextActivity);
    }
}
