package com.studshare.mobile.studshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    ProfileManager profileManager = new ProfileManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findProfile(findViewById(android.R.id.content));
    }

    private void findProfile(View view)
    {
        String profile = profileManager.loadProfile(getApplicationContext());

        if (profile == null)
            setContentView(R.layout.welcome_screen);
        else{
            boolean loggedSuccessfully = profileManager.tryLogin();

            if (loggedSuccessfully) {
                Intent goToNextActivity = new Intent(getApplicationContext(), MainScreenActivity.class);
                startActivity(goToNextActivity);
            }
            else    //nie udalo sie zalogowac danymi przechowywanymi w urzadzeniu (np. haslo zostalo zmienione od czasu poprzedniego logowania)
            {
                setContentView(R.layout.welcome_screen);
            }
        }
    }

    public void doLogin(View view) {
        Intent goToNextActivity = new Intent(getApplicationContext(), LoginScreenActivity.class);
        startActivity(goToNextActivity);
    }

    public void doSignUp(View view) {
        Intent goToNextActivity = new Intent(getApplicationContext(), SignupScreenActivity.class);
        startActivity(goToNextActivity);
    }
}
