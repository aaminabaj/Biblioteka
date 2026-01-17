package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import models.Korisnik;
import javafx.scene.control.Label;
import utils.KorisnikSession;
import utils.SceneUtil;

import java.util.Optional;

public class DashboardController {
    @FXML
    private Label lblWelcome;
    @FXML
    private Button btnKnjige;
    @FXML
    private Button btnClanovi;
    @FXML
    private Button btnPosudbe;
    @FXML
    private Button btnOdjava;

    @FXML
    public void initialize() {
        Korisnik k = KorisnikSession.getKorisnik();
        if (k != null) {
            lblWelcome.setText("Dobrodošli "
                    + k.getIme() + " "
                    + k.getPrezime());
        }

        // Navigacija
        btnKnjige.setOnAction(e -> SceneUtil.switchScene(btnKnjige, "knjige.fxml"));
        btnClanovi.setOnAction(e -> SceneUtil.switchScene(btnClanovi, "clanovi.fxml"));
        btnPosudbe.setOnAction(e -> SceneUtil.switchScene(btnPosudbe, "posudbe.fxml"));

        // Odjava
        btnOdjava.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Jeste li sigurni da se želite odjaviti?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                KorisnikSession.clear();
                SceneUtil.switchScene(btnOdjava, "login.fxml");
            }
        });
    }

}
