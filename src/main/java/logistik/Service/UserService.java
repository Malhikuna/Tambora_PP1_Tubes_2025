

package logistik.Service;

import logistik.Config.DatabaseConnection;
import logistik.Model.User;
import logistik.Util.HashUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService {
    private Connection conn;

    public UserService() {
        conn = DatabaseConnection.getConnection();
    }

    // Create
    public void createUser(User user) {
        String hashedPassword = HashUtil.hashPassword(user.getPassword());
        String sql =
        """
        INSERT INTO users (id, username, password, name, role) 
        VALUES (?, ?, ?, ?, ?)
        """;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getId().toString());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, hashedPassword );
            stmt.setString(4, user.getName());
            stmt.setString(5, user.getRole());
            stmt.executeUpdate();
            System.out.println("User berhasil ditambahkan.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read
    public List<User> getAllUser() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                User user = new User();
                user.setId(UUID.fromString(rs.getString("id")));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(UUID.fromString(rs.getString("id")));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
                return user;
            } else {
                System.out.println("User tidak ditemukan.");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update
    public void updatePassword(UUID id, String newPassword) {
        System.out.println(newPassword);
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, id.toString());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Password berhasil diupdate.");
            } else {
                System.out.println("User dengan ID tersebut tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete
    public void hapusUser(UUID id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
            System.out.println("User berhasil dihapus.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
