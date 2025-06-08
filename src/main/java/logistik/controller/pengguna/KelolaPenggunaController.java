package logistik.controller.pengguna;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logistik.App;
import logistik.model.User;
import logistik.service.UserService;

public class KelolaPenggunaController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<User> userTableView;

    @FXML
    private TableColumn<User, String> namaColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private Button editButton;

    @FXML
    private Button hapusButton;

    private UserService userService;
    private ObservableList<User> masterDataUser = FXCollections.observableArrayList();
    private FilteredList<User> filteredDataUser;

    public KelolaPenggunaController() {
        try {
            this.userService = new UserService();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error Kritis", "Gagal menginisialisasi UserService.");
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Setup kolom tabel
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Setup filter
        filteredDataUser = new FilteredList<>(masterDataUser, p -> true);
        userTableView.setItems(filteredDataUser);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredDataUser.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return user.getName().toLowerCase().contains(lowerCaseFilter) ||
                        user.getUsername().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Atur tombol edit/hapus
        editButton.setDisable(true);
        hapusButton.setDisable(true);
        userTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            editButton.setDisable(!isSelected);
            hapusButton.setDisable(!isSelected);
        });

        loadDataUser();
    }

    private void loadDataUser() {
        masterDataUser.setAll(userService.getAllUser());
    }

    @FXML
    private void handleTambahUser() {
        showUserFormDialog(null);
    }

    @FXML
    private void handleEditUser() {
        User userTerpilih = userTableView.getSelectionModel().getSelectedItem();
        if (userTerpilih != null) {
            showUserFormDialog(userTerpilih);
        }
    }

    @FXML
    private void handleHapusUser() {
        User userTerpilih = userTableView.getSelectionModel().getSelectedItem();
        if (userTerpilih != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin menghapus user " + userTerpilih.getUsername() + "?", ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText("Konfirmasi Hapus");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    userService.hapusUser(userTerpilih.getId()); // Asumsi ada metode ini di UserService
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "User berhasil dihapus.");
                    loadDataUser();
                }
            });
        }
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        loadDataUser();
    }

    // Metode ini butuh FXML & Controller baru untuk form user (misal: UserFormView.fxml & UserFormController)
    private void showUserFormDialog(User userToEdit) {
        // Logikanya akan SAMA PERSIS dengan showBarangFormDialog di BarangTableController
        // Dia akan load FXML form user, membuat stage baru, dan menunggunya ditutup.
        System.out.println("Membuka form untuk: " + (userToEdit == null ? "User Baru" : userToEdit.getUsername()));
        showAlert(Alert.AlertType.INFORMATION, "Fitur Dalam Pengembangan", "Form untuk menambah/edit user sedang dibuat.");
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