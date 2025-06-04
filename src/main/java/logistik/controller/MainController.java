package logistik.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import logistik.App;
import java.io.IOException;

public class MainController {
    @FXML
    private StackPane contentPane;

    // Event handler untuk tombol "Lihat Data Barang" di TitledPane Barang
    @FXML
    private void handleLihatDataBarang() {
        loadView("/logistik/view/barang/BarangTableView.fxml");
    }

    // Event handler untuk tombol "Tambah Barang" di TitledPane Barang
    @FXML
    private void handleTambahBarang() {
        loadView("/logistik/view/barang/BarangFormView.fxml");
    }

    @FXML
    private void handleEditBarang() {
        loadView("/logistik/view/barang/BarangFormView.fxml");
    }

    @FXML
    private void handleHapusBarang() {
        System.out.println("Tombol Hapus Barang diklik - biasanya aksi ini ada di tabel barang.");
        loadViewIntoContentPane("/logistik/view/barang/BarangTableView.fxml");
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
        System.out.println("Tombol Riwayat Transaksi diklik");
    }

    @FXML
    private void handleCetakLaporan() {
        System.out.println("Tombol Cetak Laporan diklik");
    }

    // --- Handler untuk Menu Pengguna ---
    @FXML
    private void handleUbahUsername() {
        System.out.println("Tombol Ubah Username diklik");
    }

    @FXML
    private void handleUbahPassword() {
        System.out.println("Tombol Ubah Password diklik");
    }

    // --- Handler untuk Logout ---
    @FXML
    private void handleLogout() {
        System.out.println("Tombol Logout diklik");
        // Logika untuk kembali ke halaman Login
        // Misalnya:
        // try {
        //     App.getInstance().showLoginScene(); // Asumsi ada metode ini di kelas App utama kamu
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

    // Metode helper untuk memuat FXML ke dalam contentPane
    public void loadViewIntoContentPane(String fxmlPath) {
        try {
            // Pastikan path ke FXML benar, dimulai dari root resources
            // Jika App.java ada di logistik.App, maka getResource akan mencari relatif dari sana.
            // Jika fxmlPath sudah absolut dari root resources (diawali '/'), maka App.class.getResource(fxmlPath)
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlPath));
            Parent view = loader.load();
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            // Idealnya tampilkan pesan error ke pengguna di UI
            System.err.println("Gagal memuat view: " + fxmlPath + " Error: " + e.getMessage());
            // Mungkin tampilkan dialog error
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat view: Path FXML " + fxmlPath + " tidak ditemukan atau null. Pastikan path benar dan file FXML ada.");
        }
    }


    // Metode helper untuk memuat FXML ke contentPane
    private void loadView(String fxmlPath) {
        try {
            Pane view = FXMLLoader.load(App.class.getResource(fxmlPath));
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            // Tampilkan pesan error ke pengguna atau log
            System.err.println("Gagal memuat view: " + fxmlPath);
        }
    }

    // Tambahkan metode lain untuk menu lain nanti...
}
