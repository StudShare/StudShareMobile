package com.studshare.mobile.studshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.other.ShowMessage;
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

        if (login.trim().equals("") || pass.trim().equals("")) {
            ShowMessage.Show(getApplicationContext(), "Proszę uzupełnić wszystkie pola");
            return;
        }

        ProfileManager.OperationStatus loginStatus = profileManager.tryLogin(login, pass);

        try {
            if (loginStatus == ProfileManager.OperationStatus.Success) {

                boolean savedSuccessfully = profileManager.saveProfile(getApplicationContext(), txtLogin.getText().toString(), txtPassword.getText().toString());

                if (savedSuccessfully) {
                    Intent goToNextActivity = new Intent(getApplicationContext(), MainScreenActivity.class);
                    startActivity(goToNextActivity);
                }
                else {
                    ShowMessage.Show(getApplicationContext(), "Nie udało się utworzyć profilu");
                }
            }
            else if (loginStatus == ProfileManager.OperationStatus.IncorrectLogin || loginStatus == ProfileManager.OperationStatus.IncorrectPassword) {
                ShowMessage.Show(getApplicationContext(), "Niepoprawne dane logowania");
            }
            else if (loginStatus == ProfileManager.OperationStatus.SQLError || loginStatus == ProfileManager.OperationStatus.NoInternetConnection) {
                ShowMessage.Show(getApplicationContext(), "Nie udało się nawiązać połączenia z serwerem. Sprawdź połączenie internetowe");
            }
        }
        catch (Exception e) {
            ShowMessage.Show(getApplicationContext(), "Błąd połączenia z bazą");
        }
    }
}
