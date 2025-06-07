package logistik.controller.transaksi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import logistik.App; // Untuk owner Alert
import logistik.model.Transaksi;
import logistik.service.TransaksiService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RiwayatTransaksiController {

    @FXML
    private DatePicker dariTanggalPicker;

    @FXML
    private DatePicker sampaiTanggalPicker;

    @FXML
    private TextField searchField;

    @FXML
    private Button filterButton;

    @FXML
    private Button resetButton;

    @FXML
    private TableView<Transaksi> transaksiTableView;

    @FXML
    private TableColumn<Transaksi, LocalDateTime> tanggalColumn;

    @FXML
    private TableColumn<Transaksi, String> kodeBarangColumn;

    @FXML
    private TableColumn<Transaksi, String> namaBarangColumn;

    @FXML
    private TableColumn<Transaksi, String> jenisColumn;

    @FXML
    private TableColumn<Transaksi, Integer> jumlahColumn;

    // Jika kamu memutuskan untuk menambahkan kolom user, aktifkan ini
    // @FXML
    // private TableColumn<Transaksi, String> userColumn;

    @FXML
    private Button cetakLaporanButton;

    private TransaksiService transaksiService;
    private ObservableList<Transaksi> masterDataTransaksi = FXCollections.observableArrayList();
    private FilteredList<Transaksi> filteredDataTransaksi;

    public RiwayatTransaksiController() {
        try {
            this.transaksiService = new TransaksiService();
        } catch (Exception e) {
            System.err.println("Error kritis saat inisialisasi TransaksiService: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Kritis", "Gagal menginisialisasi service transaksi. Fitur tidak akan berfungsi.");
        }
    }

    @FXML
    public void initialize() {
        // 1. Setup kolom tabel untuk mapping ke properti di model Transaksi
        // Pastikan nama properti di "" cocok dengan getter di Transaksi.java
        tanggalColumn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        kodeBarangColumn.setCellValueFactory(new PropertyValueFactory<>("kodeBarang"));
        namaBarangColumn.setCellValueFactory(new PropertyValueFactory<>("namaBarang")); // Mengambil dari field tambahan
        jenisColumn.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        jumlahColumn.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        // userColumn.setCellValueFactory(new PropertyValueFactory<>("namaPengguna")); // Jika ada

        // 2. Siapkan FilteredList untuk filter data
        filteredDataTransaksi = new FilteredList<>(masterDataTransaksi, p -> true);

        // 3. Bind FilteredList ke TableView
        transaksiTableView.setItems(filteredDataTransaksi);

        // 4. Muat semua data transaksi saat pertama kali view dibuka
        loadDataTransaksi();
    }

    // Metode untuk memuat data dari service ke master list
    private void loadDataTransaksi() {
        if (transaksiService == null) {
            showAlert(Alert.AlertType.ERROR, "Error Service", "TransaksiService tidak tersedia.");
            return;
        }
        try {
            masterDataTransaksi.setAll(transaksiService.tampilkanSemuaTransakasi()); // Ganti nama metode jika perlu
            // Reset filter ke kondisi default saat data dimuat ulang
            handleFilterAction(null);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal memuat riwayat transaksi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleFilterAction(ActionEvent event) {
        LocalDate dariTanggal = dariTanggalPicker.getValue();
        LocalDate sampaiTanggal = sampaiTanggalPicker.getValue();
        String keyword = searchField.getText();

        filteredDataTransaksi.setPredicate(transaksi -> {
            boolean cocokTanggal = true;
            boolean cocokKeyword = true;

            // --- Filter Tanggal ---
            // Ambil tanggal dari objek Transaksi (yang tipenya LocalDateTime)
            LocalDate tanggalTransaksi = transaksi.getTanggal().toLocalDate();

            if (dariTanggal != null && sampaiTanggal != null) {
                // Jika kedua tanggal diisi, cek apakah tanggal transaksi ada di dalam rentang
                cocokTanggal = !tanggalTransaksi.isBefore(dariTanggal) && !tanggalTransaksi.isAfter(sampaiTanggal);
            } else if (dariTanggal != null) {
                // Jika hanya tanggal awal diisi
                cocokTanggal = !tanggalTransaksi.isBefore(dariTanggal);
            } else if (sampaiTanggal != null) {
                // Jika hanya tanggal akhir diisi
                cocokTanggal = !tanggalTransaksi.isAfter(sampaiTanggal);
            }

            // --- Filter Keyword ---
            if (keyword != null && !keyword.trim().isEmpty()) {
                String lowerCaseKeyword = keyword.toLowerCase();
                // Cek apakah keyword ada di kode barang ATAU nama barang
                cocokKeyword = transaksi.getKodeBarang().toLowerCase().contains(lowerCaseKeyword);
            }

            // Gabungkan hasil filter: harus cocok tanggal DAN cocok keyword
            return cocokTanggal && cocokKeyword;
        });
    }

    @FXML
    void handleResetAction(ActionEvent event) {
        // Kosongkan semua field filter
        dariTanggalPicker.setValue(null);
        sampaiTanggalPicker.setValue(null);
        searchField.clear();

        // Terapkan filter lagi (yang sekarang kosong) untuk menampilkan semua data
        filteredDataTransaksi.setPredicate(p -> true);
    }

    @FXML
    void handleCetakLaporanAction(ActionEvent event) {
        System.out.println("Tombol Cetak Laporan diklik.");
        // TODO: Implementasi logika untuk mencetak atau ekspor data.
        // Data yang akan dicetak bisa diambil dari `filteredDataTransaksi`
        // agar sesuai dengan apa yang sedang ditampilkan di tabel.

        ObservableList<Transaksi> dataUntukDicetak = transaksiTableView.getItems(); // Ambil data yang sedang tampil

        if (dataUntukDicetak.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Info", "Tidak ada data untuk dicetak.");
            return;
        }

        System.out.println("Mencetak " + dataUntukDicetak.size() + " baris data...");
        // Di sini nanti kamu bisa panggil library report seperti JasperReports
        // atau tulis ke file CSV/PDF secara manual.

        showAlert(Alert.AlertType.INFORMATION, "Fitur Dalam Pengembangan", "Fitur cetak laporan sedang dalam tahap pengembangan!");
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