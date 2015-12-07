package com.studshare.mobile.studshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.service.ConnectionManager;
import com.studshare.mobile.studshare.service.ProfileManager;

import java.sql.ResultSet;

public class LoginScreenActivity extends AppCompatActivity {

    Button butLogin;
    EditText txtLogin;
    EditText txtPassword;

    ProfileManager profileManager = new ProfileManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        txtLogin = (EditText) findViewById(R.id.txtLogin);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        butLogin = (Button) findViewById(R.id.butLogin);
    }


    public void tryLogin(View view)
    {
        String login = txtLogin.getText().toString();
        String pass = txtPassword.getText().toString();

        if (login.trim().equals("") || pass.trim().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Proszę uzupełnić pola", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean loggedSuccessfully = profileManager.tryLogin(login, pass);

        try {
            if (loggedSuccessfully) {

                boolean savedSuccessfully = profileManager.saveProfile(getApplicationContext(), txtLogin.getText().toString(), txtPassword.getText().toString());

                if (savedSuccessfully) {
                    Intent goToNextActivity = new Intent(getApplicationContext(), MainScreenActivity.class);
                    startActivity(goToNextActivity);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Nie udało się utworzyć profilu", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Niepoprawne dane logowania", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
        }
    }
}
