package logistik;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverClass {

    @Test
    void testRegister() {

        try {
            Driver mySqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mySqlDriver);
        } catch (SQLException e) {
            Assertions.fail(e.getMessage());
        }
    }
}
