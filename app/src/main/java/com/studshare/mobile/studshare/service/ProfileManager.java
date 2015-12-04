package com.studshare.mobile.studshare.service;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileManager
{
    private String FILENAME = "profile";
    private String Login;
    private String Password;
    private ConnectionManager connectionManager = new ConnectionManager();

    public String loadProfile(Context context)
    {
        try {
            int n;

            FileInputStream fis = context.openFileInput(FILENAME);
            StringBuffer fileContent = new StringBuffer("");

            byte[] buffer = new byte[1024];

            while ((n = fis.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, n));
            }

            String[] profiledata = fileContent.toString().split("\n");
            Login = profiledata[0];
            Password = profiledata[1];

            return fileContent.toString();
        }
        catch (FileNotFoundException fnf) {
            return null;
        }
        catch (IOException ioe) {
            return null;
        }
    }

    public boolean saveProfile(Context context, String login, String password)
    {
        boolean savedSuccessfully = false;

        String data = login + "\n" + password;

        try
        {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();

            savedSuccessfully = true;

            return savedSuccessfully;
        }
        catch (FileNotFoundException fnf)
        {
            return savedSuccessfully;
        }
        catch (IOException ioe)
        {
            return savedSuccessfully;
        }
    }

    public boolean deleteProfile(Context context)
    {
        try{
            context.deleteFile(FILENAME);

            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean tryLogin()
    {
        try{
            String query = "SELECT * FROM users WHERE login='" + Login + "' AND password='" + Password + "'";
            ResultSet rs = connectionManager.SendQuery(query);

            if (rs.next()){
                return true;
            }

            return false;
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public boolean trySignup(Context context, String login, String password, String email)
    {
        try{
            String query = "SELECT * FROM users WHERE login='" + login + "' OR email='" + email + "'";
            ResultSet rs = connectionManager.SendQuery(query);

            if (rs.next()){
                return false;
            }
            else{   //mozna utworzyc konto

                query = "INSERT INTO users(login, password, email) VALUES('" + login + "', '" + password + "', '" + email + "')";
                //rs = connectionManager.SendQuery(query);
                int result = connectionManager.SendUpdate(query);

                if (result == 1) {
                    boolean savedSuccessfully = saveProfile(context, login, password);

                    if (savedSuccessfully){
                        Login = login;
                        Password = password;

                        return true;    //poprawnie utworzono konto i profil
                    }
                    else{
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
        }
        catch (SQLException e)
        {
            return false;
        }
    }
}
