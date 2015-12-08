package com.studshare.mobile.studshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.service.ProfileManager;
import com.studshare.mobile.studshare.service.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordChangeScreenActivity extends AppCompatActivity {

    ProfileManager profileManager = new ProfileManager();
    ConnectionManager connectionManager = new ConnectionManager();

    EditText txtPassword;
    EditText txtNewPassword;
    EditText txtNewPasswordRepeat;
    Button butChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.passwordchange_screen);

        txtPassword = (EditText) findViewById(R.id.txtActualPassword);
        txtNewPassword = (EditText) findViewById(R.id.txtNewPassword);
        txtNewPasswordRepeat = (EditText) findViewById(R.id.txtRepeatNewPassword);
        butChangePassword = (Button) findViewById(R.id.butChangePassword);
    }

    public void tryChangePassword(View view){
        String actualPassword = txtPassword.getText().toString();
        String newPassword = txtNewPassword.getText().toString();
        String newPasswordRepeat = txtNewPasswordRepeat.getText().toString();

        if (actualPassword.trim().equals("") || newPassword.trim().equals("") || newPasswordRepeat.trim().equals("")){
            Toast.makeText(getApplicationContext(), "Proszę uzupełnić wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!newPassword.trim().equals(newPasswordRepeat)){
            Toast.makeText(getApplicationContext(), "Podane hasła nie są takie same!", Toast.LENGTH_SHORT).show();
            return;
        }

        ProfileManager.OperationStatus loginStatus = profileManager.tryLogin(profileManager.getLogin(), actualPassword);

        try {
            if (loginStatus == ProfileManager.OperationStatus.Success) {
                boolean passwordChanged = profileManager.changePassword(getApplicationContext(), newPassword);

                if (passwordChanged) {
                    goToProfileSettings(view);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Nie udało się zmienić hasła.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else if (loginStatus == ProfileManager.OperationStatus.IncorrectLogin || loginStatus == ProfileManager.OperationStatus.IncorrectPassword) {
                Toast.makeText(getApplicationContext(), "Podane hasło jest nieprawidłowe. Sprawdź, czy podane hasło nie zawiera błędów.", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (loginStatus == ProfileManager.OperationStatus.SQLError || loginStatus == ProfileManager.OperationStatus.NoInternetConnection) {
                Toast.makeText(getApplicationContext(), "Nie udało się nawiązać połączenia z serwerem. Sprawdź połączenie internetowe.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Podane hasło jest nieprawidłowe lub nastąpił błąd połączenia z serwerem. Sprawdź połączenie internetowe oraz czy podane hasło nie zawiera błędów.", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    public void goToProfileSettings(View view) {
        Intent goToNextActivity = new Intent(getApplicationContext(), ProfileScreenActivity.class);
        startActivity(goToNextActivity);
    }
}