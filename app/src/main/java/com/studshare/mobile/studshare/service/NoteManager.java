package com.studshare.mobile.studshare.service;

import android.graphics.Bitmap;
import android.util.Base64;

import com.studshare.mobile.studshare.activity.Save_text;
import com.studshare.mobile.studshare.other.CameraPhoto;
import com.studshare.mobile.studshare.other.ShowMessage;

import java.io.Console;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.OutputStream;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class NoteManager {

    private final String NOTES_TABLE_NAME = "Note";
    private final String NOTE_TAG_TABLE_NAME = "Note_Tag";
    private final String TAG_TABLE_NAME = "Tag";
    private final String RATE_TABLE = "Rate";
    ConnectionManager connectionManager = new ConnectionManager();
    ProfileManager profileManager = new ProfileManager();
    TagManager tagManager = new TagManager();

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

    public ResultSet getNotesByQuery(String tagsToFind) {
        //
        // Getting notes which tags contain tagsToFind string.
        //

        String[] tagList = tagsToFind.split("\\s+");
        String query = "SELECT DISTINCT n.idNote, n.title, n.type FROM " + NOTES_TABLE_NAME + " n LEFT JOIN "
                + NOTE_TAG_TABLE_NAME + " nt ON n.idNote = nt.idNote LEFT JOIN " + TAG_TABLE_NAME + " t ON nt.idTag = t.idTag WHERE ";

        for (int i = 0; i < tagList.length; i++) {
            if (i == 0) {
                query += "t.value LIKE '%" + tagList[i] + "%'";
            }
            else {
                query += " OR t.value LIKE '%" + tagList[i] + "%'";
            }
        }

        return connectionManager.SendQuery(query);
    }

    public int getNumberOfNotesByQuery(String query) {
        ResultSet rsNotes = getNotesByQuery(query);

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

    private int addTags(String tags) {

        // Tags adding algorithm
        ResultSet noteIdrs = connectionManager.SendQuery("SELECT TOP 1 idNote FROM Note ORDER BY idNote DESC");

        try {
            if (noteIdrs.next()) {
                String[] tagList = tags.split("\\s+");

                for (int i = 0; i < tagList.length; i++) {
                    int tagId = tagManager.getIDByTagName(tagList[i]);
                    int temp_res = tagManager.addNoteTag(noteIdrs.getInt(1), tagId);
                }
            }
        }
        catch (SQLException sqle) {
            return -1;
        }

        return 1;
    }

    public ProfileManager.OperationStatus add(String title, Bitmap photo, String extension, String tags) {
        //
        // Adding photo to database
        //
        String query = "INSERT INTO " + NOTES_TABLE_NAME + "(idSiteUser, title, textContent, pictureContent, type) VALUES (" + profileManager.getUserID() + ", '" + title + "', '', '" + bitmapToBase64(photo) + "', '" + extension + "')";

        int result = connectionManager.SendUpdate(query);

        int tagsAddingResult = addTags(tags);

        if (result == 1) {
            return ProfileManager.OperationStatus.Success;
        }
        else {
            return ProfileManager.OperationStatus.OtherError;
        }
    }

    public ProfileManager.OperationStatus add2(String title,String noteContex, String extension, String tags) {
        //
        // Adding file to database
        //
        String query = "INSERT INTO " + NOTES_TABLE_NAME + "(idSiteUser, title, textContent, pictureContent, type) VALUES (" + profileManager.getUserID() + ", '" + title + "', '"+noteContex+"', NULL, '" + extension + "')";
        Save_text.type_note="photo";
        int result = connectionManager.SendUpdate(query);

        int tagsAddingResult = addTags(tags);

        if (result == 1) {
            return ProfileManager.OperationStatus.Success;
        }
        else {
            return ProfileManager.OperationStatus.OtherError;
        }
    }

    public ProfileManager.OperationStatus add3(String title, FileInputStream fileSend, String extension, File file, String tags) {
        //
        // Adding file to database
        //
        String query = "INSERT INTO " + NOTES_TABLE_NAME + "(idSiteUser, title, textContent, pictureContent, type) VALUES (" + profileManager.getUserID() + ", '" + title + "', '', '" + fileSend + "', '" + extension + "')";

        int result = connectionManager.SendUpdate2(file, profileManager.getUserID(), title, extension);

        int tagsAddingResult = addTags(tags);

        if (result == 1) {
            return ProfileManager.OperationStatus.Success;
        }
        else {
            return ProfileManager.OperationStatus.OtherError;
        }

    }

    //temp
    String savePath = "/mnt/emmc/dcim/note2.pdf";

    public void getFileData(int id) {

        byte[] fileBytes;
        String query;

        try {
            query = "select filecontent from " + NOTES_TABLE_NAME + " WHERE idNote=" + id;

            ResultSet rs = connectionManager.SendQuery(query);
            if (rs.next()) {
                fileBytes = rs.getBytes(1);
                OutputStream targetFile=  new FileOutputStream(
                        savePath);//+getNoteType(id));
                targetFile.write(fileBytes);
                targetFile.close();


                FileOutputStream stream = new FileOutputStream(savePath);
                try {
                    stream.write(fileBytes);
                } finally {
                    stream.close();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

            savePath = "/storage/sdcard/Download/note.pdf";
            try {
                query = "SELECT filecontent FROM " + NOTES_TABLE_NAME + " WHERE idNote=" + id;

                ResultSet rs = connectionManager.SendQuery(query);

                if (rs.next()) {
                    fileBytes = rs.getBytes(1);
                    OutputStream targetFile=  new FileOutputStream(savePath);
                    targetFile.write(fileBytes);
                    targetFile.close();


                    FileOutputStream stream = new FileOutputStream(savePath);
                    try {
                        stream.write(fileBytes);
                    } finally {
                        stream.close();
                    }
                }

            } catch (Exception e2) {
                e2.printStackTrace();
            }
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
        String query = "SELECT pictureContent FROM " + NOTES_TABLE_NAME + " WHERE idNote=" + id;

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

        // At first, removing all occurrences of this note in Note_Tag table.
        int tagClearResult = deleteTags(id);

        String query = "DELETE FROM " + NOTES_TABLE_NAME + " WHERE idNote=" + id;

        int result = connectionManager.SendUpdate(query);

        if (result == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    private int deleteTags(int idNote) {
        String tagsClearQuery = "DELETE FROM " + NOTE_TAG_TABLE_NAME + " WHERE idNote = " + idNote;

        return connectionManager.SendUpdate(tagsClearQuery);
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

    public void updateTags(int id, String tags) {
        // At first, removing all occurrences of this note in Note_Tag table.
        int tagClearResult = deleteTags(id);

        // Adding new tags
        String[] tagList = tags.split("\\s+");

        for (int i = 0; i < tagList.length; i++) {
            int tagId = tagManager.getIDByTagName(tagList[i]);
            int temp_res = tagManager.addNoteTag(id, tagId);
        }
    }

    public String getTagsByID(int id)
    {
        String query = "SELECT idTag FROM " + NOTE_TAG_TABLE_NAME + " WHERE idNote = " + id;

        ResultSet rs = connectionManager.SendQuery(query);

        try {
            String result = "";

            while (rs.next()) {
                int TagID = rs.getInt(1);

                String getTagNameQuery = "SELECT value FROM " + TAG_TABLE_NAME + " WHERE idTag = " + TagID;

                ResultSet rsTag = connectionManager.SendQuery(getTagNameQuery);

                while (rsTag.next()) {
                    result += rsTag.getString(1) + " ";
                }
            }

            return result;
        }
        catch (SQLException sqle) {
            return "SQL Error";
        }
    }

    public Boolean userHasNote(int idNote, int userID) {
        //
        //  Used in note preview activity. If this note belong to user then display edit and delete button. Otherwise display rate button.
        //

        String query = "SELECT * FROM " + NOTES_TABLE_NAME + " WHERE idNote = " + idNote + " AND idSiteUser = " + userID;

        ResultSet rs = connectionManager.SendQuery(query);

        try {
            while (rs.next()) {
                return Boolean.TRUE;
            }
        }
        catch (SQLException sqle) {
            return Boolean.FALSE;
        }

        return Boolean.FALSE;
    }

    public double getNoteRating(int idNote) {

        String query = "SELECT AVG(value) FROM " + RATE_TABLE + " WHERE idNote = " + idNote;

        ResultSet rs = connectionManager.SendQuery(query);

        try {
            while (rs.next()) {
                double result = rs.getDouble(1);

                if (result != 0)
                    return result;
                else
                    return -1;
            }
        }
        catch (SQLException sqle) {
            return -2;
        }

        return -1;
    }

    public int saveNoteRating(int idNote, int rating) {

        String query = "INSERT INTO " + RATE_TABLE + "(value, idNote) VALUES(" + rating + ", " + idNote + ")";

        return connectionManager.SendUpdate(query);
    }
}
