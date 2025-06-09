package logistik.service;

import logistik.model.User;
import logistik.util.HashUtil;

import java.sql.SQLException;

public class AuthService {
    public UserService userService;

    public AuthService() {
        try {
            this.userService = new UserService();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User login(String username, String password) throws SQLException {
        User user = userService.findUserByUsername(username);

        if (user != null && HashUtil.checkPassword(password, user.getPassword())) {
            System.out.println("Berhasil Login");
            return user;
        }

        return null;
    }

    public boolean konfirmasiPassword(String username, String password) {
        String passwordLama = userService.findUserByUsername(username).getPassword();

        if (passwordLama.equals(password)) {
            return true;
        }

        return false;
    }
}
