package logistik.Menu;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logistik.Model.User;
import logistik.Service.AuthService;

public class Menu extends Application {
    private Stage primaryStage;
    private AuthService authService = new AuthService();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Aplikasi Logistik");
        tampilkanMenuLogin();
        primaryStage.show();
    }

    private void tampilkanMenuLogin() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label label = new Label("Selamat Datang");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Button exitButton = new Button("Keluar");

        loginButton.setOnAction(e -> login(usernameField.getText(), passwordField.getText()));
        registerButton.setOnAction(e -> tampilkanRegister());
        exitButton.setOnAction(e -> primaryStage.close());

        layout.getChildren().addAll(label, usernameField, passwordField, loginButton, registerButton, exitButton);

        primaryStage.setScene(new Scene(layout, 300, 250));
    }

    private void login(String username, String password) {
        User user = authService.login(username, password);
        if (user != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login berhasil. Selamat datang, " + user.getUsername() + "!");
            alert.showAndWait();
            tampilkanMenuUtama();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username atau password salah.");
            alert.showAndWait();
        }
    }

    private void tampilkanRegister() {
        // Contoh tampilan pendaftaran pengguna
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button submitButton = new Button("Daftar");
        Button backButton = new Button("Kembali");

        submitButton.setOnAction(e -> {
            // Tambahkan logika register di sini
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Fitur registrasi belum diimplementasikan.");
            alert.showAndWait();
        });

        backButton.setOnAction(e -> tampilkanMenuLogin());

        layout.getChildren().addAll(new Label("Form Register"), usernameField, passwordField, submitButton, backButton);
        primaryStage.setScene(new Scene(layout, 300, 250));
    }

    private void tampilkanMenuUtama() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label label = new Label("Menu Utama");
        Button tambahBarang = new Button("Tambah Barang");
        Button barangMasuk = new Button("Catat Barang Masuk");
        Button barangKeluar = new Button("Catat Barang Keluar");
        Button lihatBarang = new Button("Lihat Daftar Barang");
        Button antrean = new Button("Lihat Antrean Transaksi");
        Button proses = new Button("Proses Transaksi");
        Button logout = new Button("Logout");

        logout.setOnAction(e -> tampilkanMenuLogin());

        layout.getChildren().addAll(label, tambahBarang, barangMasuk, barangKeluar, lihatBarang, antrean, proses, logout);
        primaryStage.setScene(new Scene(layout, 300, 400));
    }
}
