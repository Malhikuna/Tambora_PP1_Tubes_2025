package logistik.service;

import logistik.config.DatabaseConnection;
import logistik.model.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private static Connection conn;
    private UserService userService;
    private AuthService authService;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    @BeforeEach
    void setUp() throws SQLException {
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
    void testCreateUser() {
        User user = new User("Admin", "123", "Admin", "Admin");

        userService.createUser(user);

        System.out.println("Username: " + user.getUsername());
        System.out.println("Password: " + user.getPassword());
        System.out.println("Nama: " + user.getName());
        System.out.println("Admin: " + user.getRole());
        assertEquals("Admin", user.getUsername());
    }

    /*
    @Test
    void testUpdatePassword() {
        User user = new User("Malhikuna", "rahasia", "Hikmal Maulana", "Admin");

        userService.createUser(user);

        UUID userId = user.getId();

        userService.updatePassword(userId, "newpassword");

        User updatedUser = userService.findUserByUsername(user.getUsername());

        assertEquals("newpassword", updatedUser.getPassword());

        System.out.println("New Password: " + updatedUser.getPassword());
    }
    */

    /*
    @Test
    void testHapusUser() {
        User user = new User("Malhikuna", "rahasia", "Hikmal Maulana", "Admin");

        userService.createUser(user);

        List<User> users = userService.getAllUser();
        UUID userId = users.getFirst().getId();

        userService.hapusUser(userId);

        List<User> remainingUsers = userService.getAllUser();
        assertEquals(0, remainingUsers.size());
    }
    */
}
