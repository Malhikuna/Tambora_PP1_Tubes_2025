package logistik.controller.stok;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import logistik.App;
import logistik.model.StokBarang;
import logistik.service.StokQueue; // Atau StokService jika kamu punya

import java.sql.SQLException;
import java.time.LocalDateTime;

public class StokSaatIniController {

    @FXML
    private TextField searchField;

    @FXML
    private Button refreshButton;

    @FXML
    private TableView<StokBarang> stokTableView;

    @FXML
    private TableColumn<StokBarang, String> kodeBarangColumn;

    @FXML
    private TableColumn<StokBarang, String> namaBarangColumn;

    @FXML
    private TableColumn<StokBarang, Integer> jumlahColumn;

    @FXML
    private TableColumn<StokBarang, LocalDateTime> tanggalMasukColumn;

    @FXML
    private Label totalStokLabel;

    private StokQueue stokQueue; // Menggunakan StokQueue untuk mengambil data stok
    private ObservableList<StokBarang> masterDataStok = FXCollections.observableArrayList();
    private FilteredList<StokBarang> filteredDataStok;

    public StokSaatIniController() {
        try {
            this.stokQueue = new StokQueue();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error Kritis", "Gagal menginisialisasi service stok.");
        }
    }

    @FXML
    public void initialize() {
        // 1. Setup kolom tabel
        kodeBarangColumn.setCellValueFactory(new PropertyValueFactory<>("kodeBarang"));
        namaBarangColumn.setCellValueFactory(new PropertyValueFactory<>("namaBarang")); // Asumsi StokBarang punya getNamaBarang()
        jumlahColumn.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        tanggalMasukColumn.setCellValueFactory(new PropertyValueFactory<>("tanggalMasuk"));

        // 2. Setup filter dan bind ke tabel
        filteredDataStok = new FilteredList<>(masterDataStok, p -> true);
        stokTableView.setItems(filteredDataStok);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterData(newVal);
        });

        // 3. Setup listener untuk menampilkan total stok barang yang dipilih
        stokTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Jika ada baris dipilih, hitung total stok untuk kode barang tersebut
                String selectedKodeBarang = newSelection.getKodeBarang();
                int total = 0;
                for (StokBarang stok : masterDataStok) {
                    if (stok.getKodeBarang().equals(selectedKodeBarang)) {
                        total += stok.getJumlah();
                    }
                }
                totalStokLabel.setText(total + " unit (" + newSelection.getNamaBarang() + ")");
            } else {
                totalStokLabel.setText("-");
            }
        });

        // 4. Muat data awal
        loadDataStok();
    }

    private void loadDataStok() {
        if (stokQueue == null) return;
        try {
            // Asumsi ada metode getAllActiveStok() yang me-return semua batch stok yang jumlahnya > 0
            // dan sudah di-JOIN dengan tabel barang untuk mendapatkan namaBarang.
            masterDataStok.setAll(stokQueue.dapatkanSemuaStok());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal memuat data stok: " + e.getMessage());
        }
    }

    private void filterData(String keyword) {
        filteredDataStok.setPredicate(stok -> {
            if (keyword == null || keyword.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = keyword.toLowerCase();
            if (stok.getKodeBarang().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (stok.getNamaBarang() != null && stok.getNamaBarang().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            return false;
        });
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        loadDataStok();
        totalStokLabel.setText("-");
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