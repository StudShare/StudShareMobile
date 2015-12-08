package com.studshare.mobile.studshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.studshare.mobile.studshare.R;
import com.studshare.mobile.studshare.other.ShowMessage;
import com.studshare.mobile.studshare.service.ProfileManager;

public class SignupScreenActivity extends AppCompatActivity {

    ProfileManager profileManager = new ProfileManager();

    EditText txtLogin;
    EditText txtPassword;
    EditText txtPasswordRepeat;
    EditText txtEmail;
    Button butSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup_screen);

        txtLogin = (EditText) findViewById(R.id.txtLogin);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtPasswordRepeat = (EditText) findViewById(R.id.txtPasswordRepeat);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        butSignup = (Button) findViewById(R.id.butSignup);
    }

    public void trySignup(View view)
    {
        String login = txtLogin.getText().toString();
        String pass = txtPassword.getText().toString();
        String passr = txtPasswordRepeat.getText().toString();
        String email = txtEmail.getText().toString();

        if (login.trim().equals("") || pass.trim().equals("") || passr.trim().equals("") || email.trim().equals("")) {
            ShowMessage.Show(getApplicationContext(), "Proszę uzupełnić wszystkie pola");
            return;
        }

        if (!pass.trim().equals(passr.trim())) {
            ShowMessage.Show(getApplicationContext(), "Podane hasła nie są jednakowe");
            return;
        }

        else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                ShowMessage.Show(getApplicationContext(), "Niepoprawny adres e-mail");
                return;
            }
        }

        ProfileManager.OperationStatus  signupStatus = profileManager.trySignup(getApplicationContext(), login.trim(), pass.trim(), email.trim());

        if (signupStatus == ProfileManager.OperationStatus.Success) {
            Intent goToNextActivity = new Intent(getApplicationContext(), MainScreenActivity.class);
            startActivity(goToNextActivity);
        }
        else if (signupStatus == ProfileManager.OperationStatus.LoginOrEmailInUse) {
            ShowMessage.Show(getApplicationContext(), "Podany login lub email jest już w użyciu");
        }
        else {
            ShowMessage.Show(getApplicationContext(), "Nie udało się nawiązać połączenia z serwerem. Sprawdź połączenie internetowe.");
        }
    }
}
