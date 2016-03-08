package com.studshare.mobile.studshare.service;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.File;
import java.io.FileInputStream;

public class ConnectionManager {
    String ip = "eos.inf.ug.edu.pl";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "azuk";
    String un = "azuk";
    String password = "224623";

    @SuppressLint("NewApi")
    public Connection createConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;

        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERROR! ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR! ", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR! ", e.getMessage());
        }

        return conn;
    }

    public ResultSet SendQuery(String query)
    {
        ResultSet rs = null;

        try {
            Connection connection = createConnection();

            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(query);

            //connection.close();

            return rs;
        }
        catch (Exception ex)
        {
            return rs;
        }
    }

    public int SendUpdate(String query)
    {
        int rowsAffected = -1;

        try {
            Connection connection = createConnection();

            Statement stmt = connection.createStatement();
            rowsAffected = stmt.executeUpdate(query);

            //connection.close();

            return rowsAffected;
        }
        catch (Exception ex)
        {
            return rowsAffected;
        }
    }



    public int SendUpdate2(File file, int user, String title, String ext)
    {
        //dodawanie binarne za pomocą prepareStatement

       // int rowsAffected = -1;
        int len;
        String query;
        PreparedStatement pstmt;

        try {
            Connection connection = createConnection();

            FileInputStream fis = new FileInputStream(file);
            len = (int)file.length();
            query = ("insert into Note(idSiteUser, title, textContent, filecontent, type)  VALUES(?,?,?,?,?)");
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, user);
            pstmt.setString(2, title);
            pstmt.setString(3, null);
            //method to insert a stream of bytes
            pstmt.setBinaryStream(4, fis, len);
            pstmt.setString(5, ext);
            pstmt.executeUpdate();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }
}