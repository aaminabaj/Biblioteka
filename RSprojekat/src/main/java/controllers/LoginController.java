package controllers;

import javafx.scene.control.*;
import dao.KorisnikDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Korisnik;
import utils.KorisnikSession;
import utils.SceneUtil;

import java.awt.*;

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    // Prijava
    @FXML
    public void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Username i šifra se moraju unijeti");
            alert.showAndWait();
            return;
        }

        try {
            Korisnik korisnik = KorisnikDAO.login(username, password);

            if (korisnik != null) {
                KorisnikSession.setKorisnik(korisnik);

                SceneUtil.switchScene(txtUsername, "dashboard.fxml");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(null);
                alert.setHeaderText(null);
                alert.setContentText("Pogrešan username ili šifra");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Greška pri pokušaju prijave");
            alert.showAndWait();
        }
    }

    // Preusmjeravanje na register
    @FXML
    public void openRegister() {
        try {
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            SceneUtil.switchScene(stage, "registracija.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Greška pri otvaranju registracije");
            alert.showAndWait();
        }
    }


}
