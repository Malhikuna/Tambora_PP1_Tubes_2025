package logistik.controller.stok;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import logistik.App;
import logistik.model.Barang;
import logistik.service.BarangService;
import logistik.service.StokService;
import logistik.util.ManajemenSesi;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BarangMasukController {

    @FXML
    private TextField kodeBarangField;

    @FXML
    private Button cekBarangButton;

    @FXML
    private Label namaBarangLabel;

    @FXML
    private Spinner<Integer> jumlahSpinner;

    @FXML
    private DatePicker tanggalMasukPicker;

    @FXML
    private Button setSekarangButton;

    @FXML
    private Button simpanButton;

    @FXML
    private Button resetButton;

    private BarangService barangService;
    private StokService stokService;
    private Barang barangTerpilih; // Untuk menyimpan referensi barang yang sudah dicek

    public BarangMasukController() {
        try {
            this.barangService = new BarangService();
            this.stokService = new StokService();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error Kritis", "Gagal menginisialisasi service: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        // Set tanggal default ke hari ini
        tanggalMasukPicker.setValue(LocalDate.now());
        // Awalnya, nonaktifkan tombol simpan sampai barang valid dipilih
        simpanButton.setDisable(true);
    }

    @FXML
    private void handleCekBarang() {
        String kodeBarang = kodeBarangField.getText().trim();
        if (kodeBarang.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Silakan masukkan kode barang terlebih dahulu.");
            return;
        }

        try {
            Barang barang = barangService.cariBarang(kodeBarang);
            if (barang != null) {
                this.barangTerpilih = barang;
                namaBarangLabel.setText(barang.getNama());
                simpanButton.setDisable(false); // Aktifkan tombol simpan
            } else {
                this.barangTerpilih = null;
                namaBarangLabel.setText("Barang tidak ditemukan!");
                simpanButton.setDisable(true); // Nonaktifkan lagi tombol simpan
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal mencari barang: " + e.getMessage());
        }
    }

    @FXML
    private void handleSetWaktuSekarang() {
        tanggalMasukPicker.setValue(LocalDate.now());
        // Di logika simpan, kita akan gabungkan dengan LocalTime.now()
    }

    @FXML
    private void handleSimpan() {
        // Validasi
        if (barangTerpilih == null) {
            showAlert(Alert.AlertType.WARNING, "Barang Belum Valid", "Silakan cek kode barang yang valid terlebih dahulu.");
            return;
        }

        Integer jumlah = jumlahSpinner.getValue();
        LocalDate tanggal = tanggalMasukPicker.getValue();

        if (jumlah == null || jumlah <= 0) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Jumlah masuk harus lebih dari nol.");
            return;
        }

        if (tanggal == null) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Tanggal masuk harus diisi.");
            return;
        }

        // Gabungkan tanggal dari DatePicker dengan waktu saat ini
        LocalDateTime tanggalMasuk = LocalDateTime.of(tanggal, LocalTime.now());

        // Panggil service untuk mencatat barang masuk
        try {
            String userId = ManajemenSesi.getUserYangLogin().getId().toString();
            stokService.catatBarang(
                    barangTerpilih.getKode(),
                    jumlah,
                    "Masuk",
                    tanggalMasuk,
                    userId
            );

            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Barang masuk sejumlah " + jumlah + " untuk '" + barangTerpilih.getNama() + "' berhasil dicatat.");
            // Reset form setelah berhasil
            handleReset();

        } catch (IllegalArgumentException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Gagal Menyimpan", e.getMessage());
        }
    }

    @FXML
    private void handleReset() {
        kodeBarangField.clear();
        namaBarangLabel.setText("-");
        jumlahSpinner.getValueFactory().setValue(1);
        tanggalMasukPicker.setValue(LocalDate.now());
        simpanButton.setDisable(true);
        barangTerpilih = null;
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