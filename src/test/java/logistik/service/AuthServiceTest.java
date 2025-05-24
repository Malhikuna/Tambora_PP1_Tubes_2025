package logistik.service;

import logistik.Config.DatabaseConnection;
import logistik.Model.User;
import logistik.Service.AuthService;
import logistik.Service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthServiceTest {
    private static Connection conn;
    private UserService userService;
    private AuthService authService;

    @BeforeAll
    static void setupDatabase() {
        conn = DatabaseConnection.getConnection();
    }

    @BeforeEach
    void setUp() {
        userService = new UserService();
        authService = new AuthService();
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM users");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDown() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testLogin() {
        User user = new User("Malhikuna", "rahasia", "Hikmal Maulana", "Admin");

        userService.createUser(user);

        User loggedInUser = authService.login("Malhikuna", "rahasia");
        assertNotNull(loggedInUser);
        assertEquals("Malhikuna", loggedInUser.getUsername());
    }
}
