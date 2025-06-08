package logistik.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import logistik.App;
import logistik.model.User;
import logistik.util.SceneSwitcher;

import java.io.IOException;
import java.util.Optional;

public class MainController {
    @FXML
    private StackPane contentPane;

    @FXML
    private void handleLihatDataBarang() {
        loadView("/logistik/view/barang/BarangTableView.fxml");
    }

    @FXML
    private void handleTambahBarang() {
        loadView("/logistik/view/barang/BarangFormView.fxml");
    }

    @FXML
    private void handleBarangMasuk() {
        System.out.println("Tombol Barang Masuk diklik");
    }

    @FXML
    private void handleBarangKeluar() {
        System.out.println("Tombol Barang Keluar diklik");
    }

    @FXML
    private void handleStokSaatIni() {
        System.out.println("Tombol Stok Saat Ini diklik");
    }

    // --- Handler untuk Menu Transaksi ---
    @FXML
    private void handleRiwayatTransaksi() {
        loadView("/logistik/view/transaksi/TransaksiTableView.fxml");
    }

    @FXML
    private void handleCetakLaporan() {
        System.out.println("Tombol Cetak Laporan diklik");
    }

    @FXML
    public void handleKelolaSemuaPengguna(ActionEvent actionEvent) {
        loadView("/logistik/view/pengguna/KelolaPenggunaView.fxml");

    }

    @FXML
    public void handleProfilSaya(ActionEvent actionEvent) {
        loadView("/logistik/view/pengguna/ProfilSayaView.fxml");
    }

    // --- Handler untuk Logout ---
    @FXML
    private void handleLogout() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Logout");
        confirmAlert.setHeaderText("Anda yakin ingin logout?");
        if (contentPane.getScene() != null && contentPane.getScene().getWindow() != null) {
            confirmAlert.initOwner(contentPane.getScene().getWindow());
        }

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // 1. Dapatkan Stage saat ini dari komponen mana pun (misal, contentPane)
                Stage currentStage = (Stage) contentPane.getScene().getWindow();

                // 2. Panggil SceneSwitcher yang simpel
                SceneSwitcher.switchScene(currentStage, "/logistik/view/LoginView.fxml");
            } catch (IOException e) {
                System.err.println("Gagal kembali ke halaman login.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private Button KelolaSemuaPengguna;

    private User userSaatIni;

    // Metode helper untuk memuat FXML ke contentPane
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlPath));
            Parent view = loader.load();
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat view: " + fxmlPath + " Error: " + e.getMessage());
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat view: Path FXML " + fxmlPath + " tidak ditemukan atau null. Pastikan path benar dan file FXML ada.");
        }
    }

    public void initData(User user) {
        this.userSaatIni = user;
        setupUserAccess();
    }

    private void setupUserAccess() {
        if (userSaatIni == null) {
            System.err.println("Tidak ada user yang login, menyembunyikan semua menu admin.");
            return;
        }

        if ("Admin".equalsIgnoreCase(userSaatIni.getRole())) {
            KelolaSemuaPengguna.setVisible(true);
            KelolaSemuaPengguna.setManaged(true);
        }
    }
}
