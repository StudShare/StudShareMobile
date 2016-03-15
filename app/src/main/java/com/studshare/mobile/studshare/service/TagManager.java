package com.studshare.mobile.studshare.service;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagManager {

    private final String TAG_TABLE_NAME = "Tag";
    private final String NOTE_TAG_TABLE_NAME = "Note_Tag";
    ConnectionManager connectionManager = new ConnectionManager();

    public int getIDByTagName(String name)
    {
        String query = "SELECT idTag FROM " + TAG_TABLE_NAME + " WHERE value = '" + name + "'";

        ResultSet rs = connectionManager.SendQuery(query);

        try {
            // Przypadek 1: Znaleziono podany tag w bazie danych
            if (rs.next()) {
                return rs.getInt(1);
            }
            // Przypadek 2: Nie znaleziono danego tagu w bazie danych, musi on zostac utworzony
            else
            {
                String newTagQuery = "INSERT INTO " + TAG_TABLE_NAME + "(value) VALUES('" + name + "')";
                int result = connectionManager.SendUpdate(newTagQuery);

                ResultSet newrs = connectionManager.SendQuery(query);


                try {
                    if (newrs.next()) {
                        return newrs.getInt(1);
                    }
                    else {
                        return -2;
                    }

                }
                catch (SQLException sqle) {
                    return -1;
                }
            }
        }
        catch (SQLException sqle)
        {
           return -1;
        }


    }

    public int addNoteTag(int idNote, int idTag)
    {
        String query = "INSERT INTO " + NOTE_TAG_TABLE_NAME + "(idNote, idTag) VALUES(" + idNote + ", " + idTag + ")";

        // 0 or -1 = error, 1 = success
        return connectionManager.SendUpdate(query);
    }
}
