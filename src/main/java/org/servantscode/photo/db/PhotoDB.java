package org.servantscode.photo.db;

import org.servantscode.commons.db.DBAccess;
import org.servantscode.commons.search.QueryBuilder;
import org.servantscode.commons.security.OrganizationContext;
import org.servantscode.photo.Photo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoDB extends DBAccess {

    public Photo getPhoto(String guid) {
        QueryBuilder query = selectAll().from("photos").where("guid=?", guid).inOrg();
        try (Connection conn = getConnection();
             PreparedStatement stmt = query.prepareStatement(conn);
             ResultSet rs = stmt.executeQuery()) {

            if(rs.next())
                return processResults(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve photo bytes.", e);
        }
        return null;
    }

    public Photo getPublicPhoto(String guid) {
        QueryBuilder query = selectAll().from("photos").where("public=?", true).where("guid=?", guid).inOrg();
        try (Connection conn = getConnection();
             PreparedStatement stmt = query.prepareStatement(conn);
             ResultSet rs = stmt.executeQuery()) {

            if(rs.next())
                return processResults(rs);

        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve photo bytes.", e);
        }
        return null;
    }

    public void savePhoto(Photo photo) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO photos(guid, public, filetype, bytes, org_id) values (?, ?, ?, ?, ?)")){

            stmt.setString(1, photo.getGuid());
            stmt.setBoolean(2, photo.isPublicPhoto());
            stmt.setString(3, photo.getFileType());
            stmt.setBinaryStream(4, photo.getBytes());
            stmt.setInt(5, OrganizationContext.orgId());

            if(!(stmt.executeUpdate() > 0))
                throw new RuntimeException("Could not store photo.");

        } catch (SQLException e) {
            throw new RuntimeException("Could not store photo.", e);
        }
    }

    public boolean deletePhoto(String guid) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM photos WHERE guid=? AND org_id=?")){

            stmt.setString(1, guid);
            stmt.setInt(2, OrganizationContext.orgId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete photo.", e);
        }
    }

    // ----- Private -----
    private Photo processResults(ResultSet rs) throws SQLException {
        Photo photo = new Photo();
        photo.setGuid(rs.getString("guid"));
        photo.setFileType(rs.getString("fileType"));
        photo.setBytes(rs.getBinaryStream("bytes"));
        photo.setPublicPhoto(rs.getBoolean("public"));
        return photo;
    }


}
