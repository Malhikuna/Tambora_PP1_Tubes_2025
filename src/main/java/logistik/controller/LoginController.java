package logistik.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logistik.model.User;
import logistik.service.UserService;
import logistik.service.AuthService;
import logistik.util.SceneSwitcher;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logistik.model.User;
import logistik.service.AuthService;
import logistik.util.SceneSwitcher;

import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() throws SQLException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username dan password harus diisi!");
            return;
        }

        User user = authService.login(username, password);
        if (user != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login berhasil. Selamat datang, " + user.getUsername() + "!");
            alert.showAndWait();

            // Pindah ke MainView.fxml
            SceneSwitcher.switchScene((Stage) usernameField.getScene().getWindow(), "/logistik/view/MainView.fxml");

        } else {
            errorLabel.setText("Username atau password salah.");
        }
    }
}