package org.servantscode.photo.db;

import org.servantscode.commons.db.DBAccess;
import org.servantscode.photo.Photo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoDB extends DBAccess {

    public Photo getPhoto(String guid) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM photos WHERE guid=?")){

            stmt.setString(1, guid);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    Photo photo = new Photo();
                    photo.setGuid(rs.getString("guid"));
                    photo.setFileType(rs.getString("fileType"));
                    photo.setBytes(rs.getBinaryStream("bytes"));
                    return photo;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve photo bytes.", e);
        }
        return null;
    }

    public void savePhoto(Photo photo) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO photos(guid, filetype, bytes) values (?, ?, ?)")){

            stmt.setString(1, photo.getGuid());
            stmt.setString(2, photo.getFileType());
            stmt.setBinaryStream(3, photo.getBytes());

            if(!(stmt.executeUpdate() > 0))
                throw new RuntimeException("Could not store photo.");

        } catch (SQLException e) {
            throw new RuntimeException("Could not store photo.", e);
        }
    }

    public void deletePhoto(String guid) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM photos WHERE guid=?")){

            stmt.setString(1, guid);

            if(!(stmt.executeUpdate() > 0))
                throw new RuntimeException("Could not delete photo.");

        } catch (SQLException e) {
            throw new RuntimeException("Could not delete photo.", e);
        }
    }
}
