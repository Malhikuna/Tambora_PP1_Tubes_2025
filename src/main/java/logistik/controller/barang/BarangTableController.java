package logistik.controller.barang;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import logistik.model.Barang;

public class BarangTableController {

    @FXML private TableView<Barang> barangTableView;
    @FXML private TableColumn<Barang, String> kodeColumn;
    @FXML private TableColumn<Barang, String> namaColumn;
    @FXML private TableColumn<Barang, String> kategoriColumn;
    @FXML private TableColumn<Barang, String> satuanColumn;
    @FXML private TableColumn<Barang, Double> hargaJualColumn;
    @FXML private TableColumn<Barang, Double> hargaBeliColumn;
//    @FXML private TableColumn<Barang, Void> actionColumn;

    private final ObservableList<Barang> dataBarang = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Inisialisasi kolom dengan nama property dari class Barang
        kodeColumn.setCellValueFactory(new PropertyValueFactory<>("kode"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        kategoriColumn.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        satuanColumn.setCellValueFactory(new PropertyValueFactory<>("satuan"));
        hargaJualColumn.setCellValueFactory(new PropertyValueFactory<>("hargaJual"));
        hargaBeliColumn.setCellValueFactory(new PropertyValueFactory<>("hargaBeli"));

        // Tambahkan data contoh (bisa diambil dari database nantinya)
        dataBarang.addAll(
                new Barang("B001", "Pulpen", 1, "Pcs", 2500.0, 1500.0),
                new Barang("B002", "Kertas HVS", 2, "Rim", 35000.0, 28000.0)
        );

        // Tampilkan ke tabel
        barangTableView.setItems(dataBarang);
    }
}
