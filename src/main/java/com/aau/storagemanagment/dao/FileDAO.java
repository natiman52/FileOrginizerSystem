package com.aau.storagemanagment.dao;

import com.aau.storagemanagment.db.DatabaseConnection;
import com.aau.storagemanagment.model.FileModel;
import com.aau.storagemanagment.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {
    private Connection connection;

    public FileDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<FileModel> getFiles(User user) {
        List<FileModel> files = new ArrayList<>();
        String query;
        if ("ADMIN".equals(user.getRole())) {
            query = "SELECT * FROM file_model";
        } else {
            query = "SELECT * FROM file_model WHERE owner_id = ?";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            if (!"ADMIN".equals(user.getRole())) {
                pstmt.setInt(1, user.getId());
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                files.add(new FileModel(
                        rs.getString("filepath"),
                        rs.getString("name"),
                        rs.getLong("size"),
                        rs.getString("created_date"),
                        rs.getString("category"),
                        rs.getInt("owner_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }

    public void addFile(FileModel file) {
        String query = "INSERT INTO file_model (filepath, name, size, created_date, category, owner_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, file.getFilepath());
            pstmt.setString(2, file.getName());
            pstmt.setLong(3, file.getSize());
            pstmt.setString(4, file.getCreatedDate());
            pstmt.setString(5, file.getCategory());
            pstmt.setInt(6, file.getOwnerId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFileName(String filepath, String newName) {
        String query = "UPDATE file_model SET name = ? WHERE filepath = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, filepath);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String filepath) {
        String query = "DELETE FROM file_model WHERE filepath = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, filepath);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
