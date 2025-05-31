package logistik;

import logistik.model.User;
import logistik.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class ConnectionTest {
    @BeforeAll
    static void beforeAll() {
        try {
            Driver mySQLDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mySQLDriver);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void testConnection() {
        String jbdcURL = "jdbc:mysql://localhost:3306/tubes_pp1";
        String username = "root";
        String password = "";

        try(Connection connection = DriverManager.getConnection(jbdcURL, username, password);) {
            System.out.println("Berhasil konek ke database");
        } catch (SQLException exception) {
            Assertions.fail(exception);
        }
    }

    @Test
    void testConnection2() throws SQLException {
        User user = new User("Malhikuna", "rahasia", "Hikmal Maulana", "Admin");

        UserService userService = new UserService();

        userService.createUser(user);



    }
}
