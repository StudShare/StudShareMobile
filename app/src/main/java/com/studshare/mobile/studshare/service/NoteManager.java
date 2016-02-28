package com.studshare.mobile.studshare.service;

import android.graphics.Bitmap;
import android.util.Base64;

import com.studshare.mobile.studshare.other.CameraPhoto;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.OutputStream;

public class NoteManager {

    private final String NOTES_TABLE_NAME = "Note";
    ConnectionManager connectionManager = new ConnectionManager();
    ProfileManager profileManager = new ProfileManager();


    private String bitmapToBase64(Bitmap bitmap) {
        try {
            CameraPhoto cp = new CameraPhoto();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        catch (Exception e) {
            return null;
        }
    }

    public ResultSet getAllUserNotes() {
        //
        // Getting all user's notes without their content (used for listing)
        //
        String getAllUserNotesQuery = "SELECT idNote, title, type FROM " + NOTES_TABLE_NAME + " WHERE idSiteUser=" + profileManager.getUserID();

        return connectionManager.SendQuery(getAllUserNotesQuery);
    }

    public int getNumberOfUserNotes() {
        //
        // Proper SELECT COUNT query for some reason returned null.
        // Need to check number of rows returned from query manually.
        //
        ResultSet rsNotes = getAllUserNotes();

        if (rsNotes == null)
            return -2;

        try {
             int numberOfNotes = 0;

             while (rsNotes.next()) {
                 numberOfNotes++;
             }

             return numberOfNotes;

        } catch (SQLException sqle) {
            return -1;
        }
    }

    public ProfileManager.OperationStatus add(String title, Bitmap photo, String extension) {
        //
        // Adding photo to database
        //
        String query = "INSERT INTO " + NOTES_TABLE_NAME + "(idSiteUser, title, textContent, pictureContent2, type) VALUES (" + profileManager.getUserID() + ", '" + title + "', '', '" + bitmapToBase64(photo) + "', '" + extension + "')";

        int result = connectionManager.SendUpdate(query);

        if (result == 1) {
            return ProfileManager.OperationStatus.Success;
        }
        else {
            return ProfileManager.OperationStatus.OtherError;
        }
    }

    public ProfileManager.OperationStatus add2(String title, FileInputStream fileSend, String extension) {
        //
        // Adding file to database
        //
        String query = "INSERT INTO " + NOTES_TABLE_NAME + "(idSiteUser, title, textContent, pictureContent, type) VALUES (" + profileManager.getUserID() + ", '" + title + "', '', '" + fileSend + "', '" + extension + "')";

        int result = connectionManager.SendUpdate(query);

        if (result == 1) {
            return ProfileManager.OperationStatus.Success;
        }
        else {
            return ProfileManager.OperationStatus.OtherError;
        }
    }

    public void getFileData(int id) {

        byte[] fileBytes;
        String query;
        try {
            query =
                    "select data from " + NOTES_TABLE_NAME + " WHERE idNote=" + id;
            //Statement state = conn.createStatement();
            ResultSet rs = connectionManager.SendQuery(query);
            if (rs.next()) {
                fileBytes = rs.getBytes(1);
                OutputStream targetFile=  new FileOutputStream(
                        "note."+getNoteType(id));
                targetFile.write(fileBytes);
                targetFile.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getNoteTitle(int id) {
        String query = "SELECT title FROM " + NOTES_TABLE_NAME + " WHERE idNote=" + id;

        ResultSet result = connectionManager.SendQuery(query);

        try {
            if (result.next()) {
                return result.getString(1);
            } else {
                return null;
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    public String getNoteTextContent(int id) {
        String query = "SELECT textContent FROM " + NOTES_TABLE_NAME + " WHERE idNote=" + id;

        ResultSet result = connectionManager.SendQuery(query);

        try {
            if (result.next()) {
                return result.getString(1);
            } else {
                return null;
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    public String getNotePictureContent(int id) {
        String query = "SELECT pictureContent2 FROM " + NOTES_TABLE_NAME + " WHERE idNote=" + id;

        ResultSet result = connectionManager.SendQuery(query);

        try {
            if (result.next()) {
                return result.getString(1);
            } else {
                return null;
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    public String getNoteType(int id) {
        String query = "SELECT type FROM " + NOTES_TABLE_NAME + " WHERE idNote=" + id;

        ResultSet result = connectionManager.SendQuery(query);

        try {
            if (result.next()) {
                return result.getString(1);
            } else {
                return null;
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    public boolean delete(int id) {
        String query = "DELETE FROM " + NOTES_TABLE_NAME + " WHERE idNote=" + id;

        int result = connectionManager.SendUpdate(query);

        if (result == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean updateTitle(int id, String newTitle) {
        String query = "UPDATE " + NOTES_TABLE_NAME + " SET title='" + newTitle + "' WHERE idNote=" + id;

        int result = connectionManager.SendUpdate(query);

        if (result == 1) {
            return true;
        }
        else {
            return false;
        }
    }
}
