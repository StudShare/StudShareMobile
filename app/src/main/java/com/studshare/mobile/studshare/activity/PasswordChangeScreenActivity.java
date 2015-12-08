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

public class PasswordChangeScreenActivity extends AppCompatActivity {

    ProfileManager profileManager = new ProfileManager();

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

    public void tryChangePassword(View view) {
        String actualPassword = txtPassword.getText().toString();
        String newPassword = txtNewPassword.getText().toString();
        String newPasswordRepeat = txtNewPasswordRepeat.getText().toString();

        if (actualPassword.trim().equals("") || newPassword.trim().equals("") || newPasswordRepeat.trim().equals("")) {
            ShowMessage.Show(getApplicationContext(), "Proszę uzupełnić wszystkie pola");
            return;
        }
        else if (!newPassword.trim().equals(newPasswordRepeat)) {
            ShowMessage.Show(getApplicationContext(), "Podane hasła nie są takie same");
            return;
        }

        ProfileManager.OperationStatus loginStatus = profileManager.tryLogin(profileManager.getLogin(), actualPassword);

        try {
            if (loginStatus == ProfileManager.OperationStatus.Success) {
                ProfileManager.OperationStatus passwordChangeStatus = profileManager.changePassword(getApplicationContext(), newPassword);

                if (passwordChangeStatus == ProfileManager.OperationStatus.Success) {
                    goToProfileSettings(view);
                }
                else {
                    ShowMessage.Show(getApplicationContext(), "Nie udało się zmienić hasła");
                }
            }
            else if (loginStatus == ProfileManager.OperationStatus.IncorrectLogin || loginStatus == ProfileManager.OperationStatus.IncorrectPassword) {
                ShowMessage.Show(getApplicationContext(), "Podane hasło jest nieprawidłowe. Sprawdź, czy podane hasło nie zawiera błędów.");
            }
            else if (loginStatus == ProfileManager.OperationStatus.SQLError || loginStatus == ProfileManager.OperationStatus.NoInternetConnection) {
                ShowMessage.Show(getApplicationContext(), "Nie udało się nawiązać połączenia z serwerem. Sprawdź połączenie internetowe.");
            }
        }
        catch (Exception e) {
            ShowMessage.Show(getApplicationContext(), "Podane hasło jest nieprawidłowe lub nastąpił błąd połączenia z serwerem. Sprawdź połączenie internetowe oraz czy podane hasło nie zawiera błędów.");
        }
    }

    public void goToProfileSettings(View view) {
        Intent goToNextActivity = new Intent(getApplicationContext(), ProfileScreenActivity.class);
        startActivity(goToNextActivity);
    }
}