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

public class EmailChangeScreenActivity extends AppCompatActivity {

    ProfileManager profileManager = new ProfileManager();

    EditText txtPassword;
    EditText txtNewEmail;
    EditText txtNewEmailRepeat;
    Button butChangeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.emailchange_screen);

        txtPassword = (EditText) findViewById(R.id.txtActualPassword);
        txtNewEmail = (EditText) findViewById(R.id.txtNewEmail);
        txtNewEmailRepeat = (EditText) findViewById(R.id.txtRepeatNewEmail);
        butChangeEmail = (Button) findViewById(R.id.butChangeEmail);
    }

    public void tryChangeEmail(View view) {
        String actualPassword = txtPassword.getText().toString();
        String newEmail = txtNewEmail.getText().toString();
        String newEmailRepeat = txtNewEmailRepeat.getText().toString();

        if (actualPassword.trim().equals("") || newEmail.trim().equals("") || newEmailRepeat.trim().equals("")) {
            ShowMessage.Show(getApplicationContext(), "Proszę uzupełnić wszystkie pola");
            return;
        }
        else if (!newEmail.trim().equals(newEmailRepeat)) {
            ShowMessage.Show(getApplicationContext(), "Podane hasła nie są takie same");
            return;
        }

        ProfileManager.OperationStatus loginStatus = profileManager.tryLogin(profileManager.getLogin(), actualPassword);

        if (loginStatus == ProfileManager.OperationStatus.Success) {
            ProfileManager.OperationStatus emailChangeStatus = profileManager.changeEmail(newEmail);

            if (emailChangeStatus == ProfileManager.OperationStatus.Success) {
                goToProfileSettings(view);
            }
            else if (emailChangeStatus == ProfileManager.OperationStatus.LoginOrEmailInUse) {
                ShowMessage.Show(getApplicationContext(), "Podany adres e-mail jest już w użyciu");
            }
            else {
                ShowMessage.Show(getApplicationContext(), "Nie udało się zmienić adresu e-mail");
            }
        }
        else if (loginStatus == ProfileManager.OperationStatus.IncorrectLogin || loginStatus == ProfileManager.OperationStatus.IncorrectPassword) {
            ShowMessage.Show(getApplicationContext(), "Podane hasło jest nieprawidłowe. Sprawdź, czy podane hasło nie zawiera błędów.");
        }
        else if (loginStatus == ProfileManager.OperationStatus.SQLError || loginStatus == ProfileManager.OperationStatus.NoInternetConnection) {
            ShowMessage.Show(getApplicationContext(), "Nie udało się nawiązać połączenia z serwerem. Sprawdź połączenie internetowe.");
        }
    }

    public void goToProfileSettings(View view) {
        Intent goToNextActivity = new Intent(getApplicationContext(), ProfileScreenActivity.class);
        startActivity(goToNextActivity);
    }


}
