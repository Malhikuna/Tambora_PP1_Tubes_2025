package logistik.controller.barang;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logistik.App;
import logistik.model.Barang;
import logistik.service.BarangService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class BarangTableController {

    @FXML private TextField searchField;
    @FXML private TableView<Barang> barangTableView;
    @FXML private TableColumn<Barang, String> kodeColumn;
    @FXML private TableColumn<Barang, String> namaColumn;
    @FXML private TableColumn<Barang, String> kategoriColumn;
    @FXML private TableColumn<Barang, String> satuanColumn;
    @FXML private TableColumn<Barang, Double> hargaJualColumn;
    @FXML private TableColumn<Barang, Double> hargaBeliColumn;
    @FXML private Button editButton;
    @FXML private Button hapusButton;
    @FXML
    private Button refreshButton;

    private BarangService barangService;

    private ObservableList<Barang> masterDataBarang = FXCollections.observableArrayList();
    private FilteredList<Barang> filteredDataBarang;

    public BarangTableController() {
        try {
            this.barangService = new BarangService();
        } catch (Exception e) { // Tangkap Exception umum jika konstruktor service ada masalah
            System.err.println("Error kritis saat inisialisasi BarangService: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Kritis", "Gagal menginisialisasi service barang. Fitur mungkin tidak berfungsi.");
        }
    }

    @FXML
    public void initialize() {
        // Inisialisasi kolom dengan nama property dari class Barang
        kodeColumn.setCellValueFactory(new PropertyValueFactory<>("kode"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        kategoriColumn.setCellValueFactory(new PropertyValueFactory<>("namaKategori"));
        satuanColumn.setCellValueFactory(new PropertyValueFactory<>("satuan"));
        hargaJualColumn.setCellValueFactory(new PropertyValueFactory<>("hargaJual"));
        hargaBeliColumn.setCellValueFactory(new PropertyValueFactory<>("hargaBeli"));

        // 2. Siapkan FilteredList untuk pencarian
        filteredDataBarang = new FilteredList<>(masterDataBarang, p -> true);

        // 3. Bind FilteredList ke TableView
        barangTableView.setItems(filteredDataBarang);

        // 4. Tambahkan listener ke searchField untuk memfilter data secara live
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredDataBarang.setPredicate(barang -> {
                if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                    return true; // Tampilkan semua jika filter kosong
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Filter berdasarkan nama barang (bisa ditambahkan filter untuk kolom lain)
                return barang.getNama().toLowerCase().contains(lowerCaseFilter) ||
                        barang.getKode().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // 5. Atur kondisi enable/disable tombol edit dan hapus berdasarkan item yang dipilih
        editButton.setDisable(true);
        hapusButton.setDisable(true);

        barangTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean itemIsSelected = (newSelection != null);
            editButton.setDisable(!itemIsSelected);
            hapusButton.setDisable(!itemIsSelected);
        });

        // 6. Muat data barang awal ke tabel
        loadDataBarang();
    }

    private void loadDataBarang() {
        if (barangService == null) { // Antisipasi jika service gagal diinisialisasi
            showAlert(Alert.AlertType.ERROR, "Error Service", "BarangService tidak tersedia. Tidak bisa memuat data.");
            return;
        }
        try {
            masterDataBarang.setAll(barangService.tampilkanSemuaBarang());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal memuat data barang: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTambahBarangAction() {
        System.out.println("Tombol Tambah Barang diklik");
        showBarangFormDialog(null); // null menandakan mode tambah baru
    }

    @FXML
    private void handleEditBarangAction() {
        Barang barangTerpilih = barangTableView.getSelectionModel().getSelectedItem();
        if (barangTerpilih != null) {
            System.out.println("Tombol Edit Barang diklik untuk: " + barangTerpilih.getKode());
            showBarangFormDialog(barangTerpilih); // Mengirim objek barang yang akan diedit
        } else {
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Pilihan", "Silakan pilih barang yang ingin diedit.");
        }
    }

    @FXML
    private void handleHapusBarangAction() {
        Barang barangTerpilih = barangTableView.getSelectionModel().getSelectedItem();
        if (barangTerpilih != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Konfirmasi Hapus");
            confirmAlert.setHeaderText("Hapus Barang: " + barangTerpilih.getNama() + " (" + barangTerpilih.getKode() + ")?");
            confirmAlert.setContentText("Apakah Anda yakin ingin menghapus barang ini?");
            if (App.getPrimaryStage() != null) confirmAlert.initOwner(App.getPrimaryStage());


            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    barangService.hapusBarang(barangTerpilih.getKode());
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "Barang '" + barangTerpilih.getNama() + "' berhasil dihapus.");
                    loadDataBarang(); // Muat ulang data tabel
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal menghapus barang: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            // Seharusnya tidak akan terpanggil
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Pilihan", "Silakan pilih barang yang ingin dihapus.");
        }
    }

    @FXML
    private void handleRefreshAction() {
        System.out.println("Tombol Refresh diklik");
        searchField.clear(); // Bersihkan field pencarian
        loadDataBarang();    // Muat ulang semua data dari database
    }

    private void showBarangFormDialog(Barang barangToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/logistik/view/barang/BarangFormView.fxml"));
            Parent formRoot = loader.load();

            BarangFormController formController = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(barangToEdit == null ? "Tambah Barang Baru" : "Edit Data Barang");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            // Set owner stage
            if (App.getPrimaryStage() != null) {
                dialogStage.initOwner(App.getPrimaryStage());
            } else if (barangTableView.getScene() != null && barangTableView.getScene().getWindow() != null) {
                dialogStage.initOwner(barangTableView.getScene().getWindow());
            }

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error UI", "Gagal memuat form data barang: " + e.getMessage());
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
