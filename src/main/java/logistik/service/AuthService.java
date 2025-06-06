package logistik.service;

import logistik.model.User;
import logistik.util.HashUtil;

import java.sql.SQLException;

public class AuthService {
    public User login(String username, String password) throws SQLException {
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
