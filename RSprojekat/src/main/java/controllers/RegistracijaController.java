package controllers;

import dao.KorisnikDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Korisnik;
import utils.KorisnikSession;
import utils.PassUtil;
import utils.SceneUtil;
import javafx.scene.image.ImageView;

import java.awt.*;

public class RegistracijaController {
    @FXML private TextField txtIme;
    @FXML private TextField txtPrezime;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPasword;
    @FXML private PasswordField txtPasword2;
    @FXML private ImageView imgBack;

    @FXML
    public void initialize() {
        javafx.scene.image.Image img = new Image(getClass().getResourceAsStream("/images/back.png"));
        imgBack.setImage(img);
        imgBack.setOnMouseClicked(event -> {
            Stage stage = (Stage) imgBack.getScene().getWindow();
            SceneUtil.switchScene(stage, "login.fxml", 1000, 600);
        });
    }

    // Registracija
    @FXML
    public void handleRegister() {
        String ime = txtIme.getText().trim();
        String prezime = txtPrezime.getText().trim();
        String username = txtUsername.getText().trim();
        String password1 = txtPasword.getText();
        String password2 = txtPasword2.getText();

        if (ime.isEmpty() || prezime.isEmpty() || username.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            showError("Sva polja se moraju popuniti!");
            return;
        }

        if (!password1.equals(password2)) {
            showError("Lozinke se ne podudaraju!");
            return;
        }

        try {
            KorisnikDAO dao = new KorisnikDAO();

            if (dao.existsByUsername(username)) {
                showError("Korisnik sa ovim username već postoji!");
                return;
            }

            Korisnik k = new Korisnik();
            k.setIme(ime);
            k.setPrezime(prezime);
            k.setUsername(username);
            k.setPasswordHash(PassUtil.hashPassword(password1));

            dao.create(k);

            showInfo("Korisnik uspješno registrovan!");
            KorisnikSession.setKorisnik(k);

            SceneUtil.switchScene(txtIme, "dashboard.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Greška pri registraciji!");
        }
    }

    // Alert metode
    private void showError(String poruka) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(poruka);
        alert.showAndWait();
    }

    private void showInfo(String poruka) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(poruka);
        alert.showAndWait();
    }

}
