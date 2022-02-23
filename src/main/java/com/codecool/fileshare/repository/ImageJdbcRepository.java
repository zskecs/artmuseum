package com.codecool.fileshare.repository;

import org.springframework.stereotype.Component;
import java.util.UUID;
import javax.sql.DataSource;
import java.sql.*;


@Component("jdbc")
public class ImageJdbcRepository implements ImageRepository {

    DatabaseManager databaseManager = new DatabaseManager();

    private DataSource dataSource = databaseManager.connect();

    public ImageJdbcRepository() throws SQLException {
    }

    public String getUUID(String content, String category) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT id from image where content = ? and category = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setBytes(1, content.getBytes());
            ps.setString(2, category);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String storeImage(String category, String content) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO image(category, content, extension) VALUES(?, ?, ?)";
            //String sql= "INSERT INTO image(category, content, extension) VALUES(?, decode(?, 'base64'), ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, category);
            ps.setBytes(2, content.getBytes());
            ps.setString(3, getExtension(content));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getUUID(content, category);
    }

    public String getExtension(String content) {
        String extension = "";
        switch (content.charAt(0)) {
            case '/':
                extension = "jpg";
                break;
            case 'i':
                extension = "png";
                break;
            case 'R':
                extension = "gif";
                break;
            case 'U':
                extension = "webp";
        }
        return extension;
    }

    @Override
    public String readImage(String uuid) {
        try (Connection connection = dataSource.getConnection()) {
            //String sql = "SELECT encode(content, 'base64') FROM image WHERE id = ?";
            String sql = "SELECT content from image where image.id = ?::uuid";
            PreparedStatement ps = connection.prepareStatement(sql);
            //ps.setString(1, uuid);
            ps.setObject(1, UUID.fromString(uuid));
            ResultSet rs = ps.executeQuery();
            rs.next();
            //return rs.getString(1);
            byte[] bytes= rs.getBytes(1);
            return new String(bytes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
