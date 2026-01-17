package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Control;

public class SceneUtil {

    // Navigacija izmedju razlicitih scena
    public static void switchScene(Control control, String fxml) {
        Stage stage = (Stage) control.getScene().getWindow();
        switchScene(stage, fxml, 1000, 600);
    }

    public static void switchScene(Stage stage, String fxml) {
        switchScene(stage, fxml, 1000, 600);
    }

    public static void switchScene(Stage stage, String fxml, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtil.class.getResource("/views/" + fxml));
            Scene scene = new Scene(loader.load(), width, height);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}