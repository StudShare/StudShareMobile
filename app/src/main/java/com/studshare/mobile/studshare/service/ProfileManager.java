package com.studshare.mobile.studshare.service;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileManager
{
    private final String UsersTableName = "SiteUser";
    private String FILENAME = "profile";
    private static String Login;
    private static String Password;
    private ConnectionManager connectionManager = new ConnectionManager();
    private PasswordMatcher passwordMatcher = new PasswordMatcher();

    public String getLogin() { return Login; }
    public void setLogin(String login) { Login = login; }
    public void setPassword(String newPassword) { Password = newPassword; }
    public String getPassword() { return Password; }

    public enum OperationStatus {
        Success,
        IncorrectPassword,
        IncorrectLogin,
        SQLError,
        NoInternetConnection,
        LoginOrEmailInUse,
        IOError,
        OtherError
    }

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

            Login = login;
            Password = password;

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

    public OperationStatus tryLogin(String Login, String Password)
    {
        try {
            String getSalt = "SELECT salt FROM " + UsersTableName + " WHERE login='" + Login + "'";
            ResultSet rsSalt = connectionManager.SendQuery(getSalt);
            String salt = "";

            if (rsSalt.next()) {
                salt = rsSalt.getString(1);
                String hash = passwordMatcher.getSecurePassword(Password.trim(), salt);

                String checkPassword = "SELECT hash FROM " + UsersTableName + " WHERE login='" + Login + "'";
                ResultSet rsHash = connectionManager.SendQuery(checkPassword);

                if (rsHash.next()) {
                    String downloadedHash = rsHash.getString(1);

                    if (downloadedHash.equals(hash)) {
                        return OperationStatus.Success;
                    }
                    else {
                        return OperationStatus.IncorrectPassword;
                    }
                }
                else {
                    return OperationStatus.IncorrectLogin;
                }
            }
            else {
                return OperationStatus.IncorrectLogin;
            }
        }
        catch (SQLException e) {
            return OperationStatus.SQLError;
        }
        catch (NullPointerException e) {
            return OperationStatus.NoInternetConnection;
        }
    }

    public OperationStatus trySignup(Context context, String login, String password, String email)
    {
        try {
            String query = "SELECT * FROM " + UsersTableName + " WHERE login='" + login + "' OR email='" + email + "'";
            ResultSet rs = connectionManager.SendQuery(query);

            if (rs.next()) {
                return OperationStatus.LoginOrEmailInUse;
            }
            else {
                try {
                    String salt = passwordMatcher.generateSalt();
                    String hash = passwordMatcher.getSecurePassword(password, salt);

                    query = "INSERT INTO " + UsersTableName + "(login, salt, hash, email) VALUES('" + login + "', '" + salt + "', '" + hash + "', '" + email + "')";
                    int result = connectionManager.SendUpdate(query);

                    if (result == 1) {
                        boolean savedSuccessfully = saveProfile(context, login, password);

                        if (savedSuccessfully) {
                            Login = login;
                            Password = password;

                            return OperationStatus.Success;
                        }
                        else {
                            return OperationStatus.IOError;
                        }
                    }
                    else {
                        return OperationStatus.SQLError;
                    }
                }
                catch (NullPointerException e) {
                    return OperationStatus.NoInternetConnection;
                }
                catch (Exception e) {
                    return OperationStatus.OtherError;
                }
            }
        }
        catch (SQLException e) {
            return OperationStatus.SQLError;
        }
    }

    public OperationStatus changePassword(Context context, String newPassword) {
        try {
            String getSalt = "SELECT salt FROM " + UsersTableName + " WHERE login='" + Login + "'";
            ResultSet rsSalt = connectionManager.SendQuery(getSalt);
            String salt = "";
            String hash = "";

            if (rsSalt.next()) {
                salt = rsSalt.getString(1);

                hash = passwordMatcher.getSecurePassword(newPassword, salt);

                String query = "UPDATE " + UsersTableName + " SET hash='" + hash + "' WHERE login='" + getLogin() + "'";
                int result = connectionManager.SendUpdate(query);

                if (result == 1) {
                    deleteProfile(context);
                    saveProfile(context, getLogin(), newPassword);

                    setPassword(newPassword);

                    return OperationStatus.Success;
                }
                else {
                    return OperationStatus.IncorrectLogin;
                }
            }
            else {
                return OperationStatus.IncorrectLogin;
            }
        }
        catch (SQLException e){
            return OperationStatus.SQLError;
        }
    }

    public OperationStatus changeEmail(String newEmail) {
        try {
            String checkEmailAvailability = "SELECT * FROM " + UsersTableName + " WHERE email='" + newEmail + "'";
            ResultSet rsEmail = connectionManager.SendQuery(checkEmailAvailability);

            if (rsEmail.next()) {
                return OperationStatus.LoginOrEmailInUse;
            }
            else {
                String query = "UPDATE " + UsersTableName + " SET email='" + newEmail + "' WHERE login='" + getLogin() + "'";
                int result = connectionManager.SendUpdate(query);

                if (result == 1) {
                    return OperationStatus.Success;
                } else {
                    return OperationStatus.IncorrectLogin;
                }
            }
        }
        catch (SQLException e) {
            return OperationStatus.SQLError;
        }
    }

    public int getUserID() {
        try {
            String getIDQuery = "SELECT idSiteUser FROM " + UsersTableName + " WHERE login='" + getLogin() + "'";

            ResultSet rsID = connectionManager.SendQuery(getIDQuery);

            if (rsID.next()) {
                return rsID.getInt(1);
            }
            else {
                return -3;
            }
        }
        catch (SQLException sqle) {
            return -1;
        }
        catch (Exception e) {
            return -2;
        }
    }
}
