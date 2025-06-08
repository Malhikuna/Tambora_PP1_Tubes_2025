package logistik.controller.pengguna;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import logistik.App;
import logistik.model.User;
import logistik.service.AuthService; // Dibutuhkan untuk verifikasi & hash password
import logistik.service.UserService;
import logistik.util.ManajemenSesi;

import java.sql.SQLException;
//import logistik.util.SessionManager;

public class ProfilSayaController {

    @FXML
    private TextField namaField;

    @FXML
    private TextField usernameField;

    @FXML
    private Button simpanProfilButton;

    @FXML
    private PasswordField passwordLamaField;

    @FXML
    private PasswordField passwordBaruField;

    @FXML
    private PasswordField konfirmasiPasswordField;

    @FXML
    private Button ubahPasswordButton;

    private UserService userService;
    private AuthService authService;
    private User userSaatIni;

    public ProfilSayaController() {
        try {
            this.userService = new UserService();
            this.authService = new AuthService();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error Kritis", "Gagal menginisialisasi service.");
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        this.userSaatIni = ManajemenSesi.getUserYangLogin();


        if (this.userSaatIni != null) {
            namaField.setText(userSaatIni.getName());
            usernameField.setText(userSaatIni.getUsername());
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Tidak dapat memuat data pengguna. Silakan login ulang.");
            simpanProfilButton.setDisable(true);
            ubahPasswordButton.setDisable(true);
        }
    }

    @FXML
    private void handleSimpanProfil() {
        String namaBaru = namaField.getText().trim();
        String usernameBaru = usernameField.getText().trim();

        if (namaBaru.isEmpty() || usernameBaru.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Nama dan Username tidak boleh kosong.");
            return;
        }

        userSaatIni.setName(namaBaru);
        userSaatIni.setUsername(usernameBaru);

        try {
            userService.updateUserProfile(userSaatIni);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Profil berhasil diperbarui.");


        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal memperbarui profil: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUbahPassword() {
        String passLama = passwordLamaField.getText();
        String passBaru = passwordBaruField.getText();
        String konfirmasiPass = konfirmasiPasswordField.getText();

        // Validasi dasar
        if (passLama.isEmpty() || passBaru.isEmpty() || konfirmasiPass.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Semua field password harus diisi.");
            return;
        }
        if (!passBaru.equals(konfirmasiPass)) {
            showAlert(Alert.AlertType.ERROR, "Password Tidak Cocok", "Password baru dan konfirmasi password tidak sama.");
            return;
        }

        try {
            // 1. Verifikasi password lama
            boolean isPasswordLamaBenar = authService.konfirmasiPassword(userSaatIni.getUsername(), passLama);

            if (isPasswordLamaBenar) {
                // 2. Jika benar, panggil service untuk update password
                userService.updatePassword(userSaatIni.getId(), passBaru); // Buat metode ini di UserService
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Password berhasil diubah.");

                passwordLamaField.clear();
                passwordBaruField.clear();
                konfirmasiPasswordField.clear();
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Password lama yang Anda masukkan salah.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error Database", "Terjadi kesalahan saat mengubah password: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (App.getPrimaryStage() != null) alert.initOwner(App.getPrimaryStage());
        alert.showAndWait();
    }
}
