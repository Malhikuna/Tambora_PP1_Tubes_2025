package logistik.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logistik.model.User;
import logistik.service.AuthService;

import java.sql.SQLException;

public class Menu extends Application {
    private Stage primaryStage;
    private AuthService authService = new AuthService();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Aplikasi Logistik - Pencatatan Keluar Masuk Barang di Toko");
        tampilkanMenuLogin();
        primaryStage.show();
    }

    private void tampilkanMenuLogin() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #F9FAFC;");

        Label logo = new Label("📦");
        logo.setStyle("-fx-font-size: 48px;");

        Label title = new Label("Hai, Selamat Datang !");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);
        usernameField.setStyle(
                "-fx-background-radius: 25; " +
                        "-fx-border-radius: 25; " +
                        "-fx-padding: 10 15; " +
                        "-fx-border-color: #ccc; " +
                        "-fx-background-color: white;"
        );

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);
        passwordField.setStyle(
                "-fx-background-radius: 25; " +
                        "-fx-border-radius: 25; " +
                        "-fx-padding: 10 15; " +
                        "-fx-border-color: #ccc; " +
                        "-fx-background-color: white;"
        );

//        CheckBox rememberMe = new CheckBox("Ingat Saya");
//        Hyperlink forgotPassword = new Hyperlink("Forgot password?");
//        forgotPassword.setStyle("-fx-text-fill: #3498db;");

        HBox options = new HBox(10);
        options.setSpacing(30);
        options.setPadding(new Insets(10, 0, 10, 0));

        Button loginButton = new Button("LOGIN");
        loginButton.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #00BCD4;" +
                        "-fx-text-fill: black;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 25;" +
                        "-fx-font-weight: bold;"
        );

        loginButton.setOnAction(e -> {
            try {
                login(usernameField.getText(), passwordField.getText());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        layout.getChildren().addAll(logo, title, usernameField, passwordField, options, loginButton);
        layout.setSpacing(15);
        layout.setStyle("-fx-alignment: center; -fx-background-color: #f0f2f5;");

        Scene scene = new Scene(layout, 360, 450);
        primaryStage.setScene(scene);
    }

    private void login(String username, String password) throws SQLException {
        User user = authService.login(username, password);
        if (user != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login berhasil. Selamat datang, " + user.getUsername() + "!");
            alert.showAndWait();
            tampilkanMenuUtama();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username atau Password salah.");
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Fitur Registrasi belum Diimplementasikan.");
            alert.showAndWait();
        });

        backButton.setOnAction(e -> tampilkanMenuLogin());

        layout.getChildren().addAll(new Label("Form Register"), usernameField, passwordField, submitButton, backButton);
        primaryStage.setScene(new Scene(layout, 300, 250));
    }

    private void tampilkanMenuUtama() {
        VBox layout = new VBox(15); // lebih renggang
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER); // tengah vertikal dan horizontal
        layout.setStyle("-fx-background-color: #f0f4f8;"); // warna latar

        Label label = new Label("Menu Utama");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Button tambahBarang = new Button("➕ Tambah Barang");
        Button barangMasuk = new Button("📥 Catat Barang Masuk");
        Button barangKeluar = new Button("📤 Catat Barang Keluar");
        Button lihatBarang = new Button("📦 Lihat Daftar Barang");
        Button antrean = new Button("🕒 Lihat Antrean Transaksi");
        Button proses = new Button("⚙️ Proses Transaksi");
        Button logout = new Button("🚪 Logout");

        // Styling tombol seragam
        Button[] buttons = { tambahBarang, barangMasuk, barangKeluar, lihatBarang, antrean, proses, logout };
        for (Button button : buttons) {
            button.setPrefWidth(220);
            button.setStyle(
                    "-fx-font-size: 14px; " +
                            "-fx-background-color: #4CAF50; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-cursor: hand;"
            );

            // Tambahkan efek hover
            button.setOnMouseEntered(e -> button.setStyle(
                    "-fx-font-size: 14px; " +
                            "-fx-background-color: #45A049; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-cursor: hand;"
            ));
            button.setOnMouseExited(e -> button.setStyle(
                    "-fx-font-size: 14px; " +
                            "-fx-background-color: #4CAF50; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-cursor: hand;"
            ));
        }

        logout.setOnAction(e -> tampilkanMenuLogin());

        layout.getChildren().addAll(label, tambahBarang, barangMasuk, barangKeluar, lihatBarang, antrean, proses, logout);
        primaryStage.setScene(new Scene(layout, 350, 500));
    }
}
