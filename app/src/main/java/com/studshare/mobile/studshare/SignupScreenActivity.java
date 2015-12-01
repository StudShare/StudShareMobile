package com.studshare.mobile.studshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        //najpierw sprawdzic poprawnosc pol, potem wywolac funkcje z profile manager
        String login = txtLogin.getText().toString();
        String pass = txtPassword.getText().toString();
        String passr = txtPasswordRepeat.getText().toString();
        String email = txtEmail.getText().toString();

        if (login.trim().equals("") || pass.trim().equals("") || passr.trim().equals("") || email.trim().equals("")){
            Toast.makeText(getApplicationContext(), "Proszę uzupełnić wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.trim().equals(passr.trim())){
            Toast.makeText(getApplicationContext(), "Podane hasła nie są jednakowe", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.contains("@")){
            Toast.makeText(getApplicationContext(), "Niepoprawny adres e-mail", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean signupSuccessfully = profileManager.trySignup(getApplicationContext(), login.trim(), pass.trim(), email.trim());

        if (signupSuccessfully){
            Intent goToNextActivity = new Intent(getApplicationContext(), MainScreenActivity.class);
            startActivity(goToNextActivity);
        }
        else{   //bardzo ogolny komunikat, rownie dobrze moze byc blad polaczenia z serwerem lub nieprawidlowy zapis pliku profilu na urzadzeniu
            Toast.makeText(getApplicationContext(), "Podany login lub email jest już w użyciu", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
