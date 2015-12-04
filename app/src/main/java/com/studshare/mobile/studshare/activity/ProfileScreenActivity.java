package com.studshare.mobile.studshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.service.ProfileManager;
import com.studshare.mobile.studshare.other.CustomList;

public class ProfileScreenActivity extends AppCompatActivity {

    ListView list;
    String[] web = {
            "Zmień hasło",
            "Wyloguj"
    } ;
    Integer[] imageId = {
            R.drawable.password,
            R.drawable.logout
    };

    ProfileManager profileManager = new ProfileManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_screen);

        CustomList adapter = new CustomList(ProfileScreenActivity.this, web, imageId);

        list = (ListView)findViewById(android.R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 1:
                        doLogOut(view);
                }

            }
        });
    }

    public void goToMainScreen(View view) {
        Intent goToNextActivity = new Intent(getApplicationContext(), MainScreenActivity.class);
        startActivity(goToNextActivity);
    }

    public void doLogOut(View view) {
        boolean deleteSuccessfull = profileManager.deleteProfile(getApplicationContext());

        if (deleteSuccessfull)
        {
            Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goToNextActivity);
        }

    }
}