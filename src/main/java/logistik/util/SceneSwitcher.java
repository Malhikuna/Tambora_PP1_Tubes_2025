package logistik.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logistik.App;
import logistik.controller.MainController;
import logistik.model.User;

import java.io.IOException;

public class SceneSwitcher {

        public static void switchScene(Stage stage, String fxmlPath, User user) throws IOException {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlPath));

            Parent root = loader.load();

            MainController mainController = loader.getController();

            mainController.initData(user);

            stage.setScene(new Scene(root));

            stage.setTitle("Sistem Logistik - " + user.getName());
        }
    }
