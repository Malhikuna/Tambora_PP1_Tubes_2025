package logistik.Service;

import logistik.Model.User;
import logistik.Util.HashUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthService {
    public User login(String username, String password) {
        UserService userService = new UserService();
        User user = userService.findUserByUsername(username);

        if (user != null && HashUtil.checkPassword(password, user.getPassword())) {
            System.out.println("Berhasil Login");
            return user;
        }

        System.out.println("Username atau password salah");

        return null;
    }

    public void logout(User user) {

    }
}
