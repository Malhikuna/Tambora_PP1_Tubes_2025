package logistik.controller.barang;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
// import javafx.stage.Stage; // Tidak lagi mengontrol Stage sendiri jika di-embed
import logistik.App;
import logistik.model.Barang;
import logistik.model.Kategori;
import logistik.service.BarangService;
import logistik.service.KategoriService;

import java.sql.SQLException;

public class BarangFormController {

    // Interface kecil untuk callback navigasi
    public interface NavigationHandler {
        void navigateToTableView();
    }

    @FXML
    private Label judulFormLabel;
    @FXML
    private TextField kodeBarangField;
    @FXML
    private TextField namaBarangField;
    @FXML
    private ComboBox<Kategori> kategoriComboBox;
    @FXML
    private TextField satuanField;
    @FXML
    private TextField hargaBeliField;
    @FXML
    private TextField hargaJualField;
    @FXML
    private Button simpanButton;

    private BarangService barangService;
    private KategoriService kategoriService;
    private Barang barangToEdit;
    private boolean isEditMode = false;

    private NavigationHandler navigationHandler; // Untuk navigasi setelah simpan/batal

    public BarangFormController() {
        try {
            this.barangService = new BarangService();
            this.kategoriService = new KategoriService();
        } catch (Exception e) {
            System.err.println("Error saat inisialisasi service di BarangFormController: " + e.getMessage());
            // Tampilkan error atau handle lebih lanjut jika service gagal dibuat
        }
    }

    @FXML
    public void initialize() {
        if (this.kategoriService != null) {
            loadKategoriComboBox();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error Kritis", "KategoriService tidak terinisialisasi.");
        }
        addNumericListener(hargaBeliField);
        addNumericListener(hargaJualField);
    }

    // Metode ini dipanggil oleh MainController untuk menyiapkan form
    public void initData(Barang barang) {
        this.barangToEdit = barang;

        if (barang != null) {
            isEditMode = true;
            judulFormLabel.setText("Edit Data Barang");
            simpanButton.setText("Simpan Perubahan");
            kodeBarangField.setText(barang.getKode());
            kodeBarangField.setEditable(false);
            namaBarangField.setText(barang.getNama());
            satuanField.setText(barang.getSatuan());
            hargaBeliField.setText(String.valueOf(barang.getHargaBeli()));
            hargaJualField.setText(String.valueOf(barang.getHargaJual()));

            if (kategoriComboBox.getItems() != null && !kategoriComboBox.getItems().isEmpty() && barang.getKategoriId() > 0) {
                for (Kategori k : kategoriComboBox.getItems()) {
                    if (k.getId() == barang.getKategoriId()) {
                        kategoriComboBox.setValue(k);
                        break;
                    }
                }
            }
        } else {
            isEditMode = false;
            judulFormLabel.setText("Tambah Data Barang Baru");
            simpanButton.setText("Tambah Barang");
            kodeBarangField.clear();
            namaBarangField.clear();
            kategoriComboBox.setValue(null);
            satuanField.clear();
            hargaBeliField.clear();
            hargaJualField.clear();
            kodeBarangField.setEditable(true);
        }
    }

    private void loadKategoriComboBox() {
        try {
            ObservableList<Kategori> daftarKategori = FXCollections.observableArrayList(kategoriService.tampilkanSemuaKategori());
            kategoriComboBox.setItems(daftarKategori);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal memuat data kategori: " + e.getMessage());
            e.printStackTrace();
        } catch (NullPointerException e) {
            showAlert(Alert.AlertType.ERROR, "Error Service", "KategoriService belum terinisialisasi.");
            e.printStackTrace();
        }
    }

    public void handleSimpanButtonAction(ActionEvent actionEvent) {
        if (barangService == null) {
            showAlert(Alert.AlertType.ERROR, "Error Kritis", "BarangService tidak terinisialisasi.");
            return;
        }
        if (!isValidInput()) {
            return;
        }

        String kode = kodeBarangField.getText().trim();
        String nama = namaBarangField.getText().trim();
        Kategori kategoriTerpilih = kategoriComboBox.getSelectionModel().getSelectedItem();
        String satuan = satuanField.getText().trim();
        double hargaBeli;
        double hargaJual;

        try {
            hargaBeli = Double.parseDouble(hargaBeliField.getText().trim());
            hargaJual = Double.parseDouble(hargaJualField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Format Angka Salah", "Harga beli dan harga jual harus berupa angka.");
            return;
        }

        if (kategoriTerpilih == null) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Lengkap", "Silakan pilih kategori barang.");
            return;
        }
        int kategoriId = kategoriTerpilih.getId();
        String namaKategori = kategoriTerpilih.getNama();

        boolean sukses = false;
        if (isEditMode && barangToEdit != null) {
            barangToEdit.setNama(nama);
            barangToEdit.setKategoriId(kategoriId);
            barangToEdit.setNamaKategori(namaKategori);
            barangToEdit.setSatuan(satuan);
            barangToEdit.setHargaBeli(hargaBeli);
            barangToEdit.setHargaJual(hargaJual);
            System.out.println(hargaBeli);
            System.out.println(hargaJual);
            try {
                barangService.updateBarang(barangToEdit);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data barang '" + barangToEdit.getNama() + "' berhasil diperbarui.");
                sukses = true;
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal memperbarui data barang: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Barang barangBaru = new Barang(kode, nama, kategoriId, satuan, namaKategori, hargaBeli, hargaJual);
            try {
                barangService.tambahBarang(barangBaru);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Barang baru '" + barangBaru.getNama() + "' berhasil ditambahkan.");
                sukses = true;
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal menambahkan barang baru: " + e.getMessage() +
                        (e.getMessage().toLowerCase().contains("duplicate entry") ? "\nKode barang mungkin sudah ada." : ""));
                e.printStackTrace();
            }
        }

        if (sukses && navigationHandler != null) {
            navigationHandler.navigateToTableView();
        }
    }

    public void handleBatalButtonAction(ActionEvent actionEvent) {
        if (navigationHandler != null) {
            navigationHandler.navigateToTableView();
        }
    }

    private boolean isValidInput() {
        StringBuilder errorMessage = new StringBuilder();
        if (kodeBarangField.getText() == null || kodeBarangField.getText().trim().isEmpty()) {
            errorMessage.append("Kode barang tidak boleh kosong!\n");
        }
        if (namaBarangField.getText() == null || namaBarangField.getText().trim().isEmpty()) {
            errorMessage.append("Nama barang tidak boleh kosong!\n");
        }
        if (kategoriComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage.append("Kategori harus dipilih!\n");
        }
        if (satuanField.getText() == null || satuanField.getText().trim().isEmpty()) {
            errorMessage.append("Satuan tidak boleh kosong!\n");
        }

        String hargaBeliStr = hargaBeliField.getText();
        if (hargaBeliStr == null || hargaBeliStr.trim().isEmpty()) {
            errorMessage.append("Harga beli tidak boleh kosong!\n");
        } else {
            try {
                double hb = Double.parseDouble(hargaBeliStr.trim());
                if (hb < 0) errorMessage.append("Harga beli tidak boleh negatif!\n");
            } catch (NumberFormatException e) {
                errorMessage.append("Harga beli harus berupa angka!\n");
            }
        }

        String hargaJualStr = hargaJualField.getText();
        if (hargaJualStr == null || hargaJualStr.trim().isEmpty()) {
            errorMessage.append("Harga jual tidak boleh kosong!\n");
        } else {
            try {
                double hj = Double.parseDouble(hargaJualStr.trim());
                if (hj < 0) errorMessage.append("Harga jual tidak boleh negatif!\n");
            } catch (NumberFormatException e) {
                errorMessage.append("Harga jual harus berupa angka!\n");
            }
        }

        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", errorMessage.toString());
            return false;
        }
        return true;
    }

    private void addNumericListener(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("\\d*([.]\\d{0,2})?")) {
                textField.setText(oldValue != null ? oldValue : "");
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Jika form ini adalah bagian dari contentPane, owner alert bisa jadi Stage utama.
        // Jika App.getPrimaryStage() ada:
        if (App.getPrimaryStage() != null) {
            alert.initOwner(App.getPrimaryStage());
        }
        alert.showAndWait();
    }
}
