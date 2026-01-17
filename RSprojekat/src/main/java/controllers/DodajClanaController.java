package controllers;

import dao.ClanDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Clan;
import utils.SceneUtil;

import java.time.LocalDate;

public class DodajClanaController {

    @FXML
    private TextField txtIme;
    @FXML
    private TextField txtPrezime;
    @FXML
    private TextField txtBrojClanskeKarte;
    @FXML
    private TextField txtMail;
    @FXML
    private DatePicker dtmDatumPridruzivanja;
    @FXML
    private Button btnDodajClana;
    @FXML
    private ImageView imgBack;

    private final ClanDAO clanDAO = new ClanDAO();

    @FXML
    public void initialize() {
        Image img = new Image(getClass().getResourceAsStream("/images/back.png"));
        imgBack.setImage(img);
        imgBack.setOnMouseClicked(event -> {
            Stage stage = (Stage) imgBack.getScene().getWindow();
            SceneUtil.switchScene(stage, "clanovi.fxml", 1000, 600);
        });
        btnDodajClana.setOnAction(e -> dodajClana());
    }

    // Dodavanje clana - sva polja moraju biti popunjena + broj clanske mora biti unikatan
    private void dodajClana() {
        try {
            String ime = txtIme.getText().trim();
            String prezime = txtPrezime.getText().trim();
            String brojClanske = txtBrojClanskeKarte.getText().trim();
            String mail = txtMail.getText().trim();
            LocalDate datum = dtmDatumPridruzivanja.getValue();

            if (ime.isEmpty() || prezime.isEmpty() || brojClanske.isEmpty() || mail.isEmpty() || datum == null) {
                showAlert("Sva polja se moraju popuniti!");
                return;
            }

            if (clanDAO.existsByBrojClanske(brojClanske)) {
                showAlert("Član sa ovim brojem članske karte već postoji!");
                return;
            }

            Clan clan = new Clan();
            clan.setIme(ime);
            clan.setPrezime(prezime);
            clan.setBrojClanske(brojClanske);
            clan.setMail(mail);
            clan.setDatumPridruzivanja(datum);

            clanDAO.kreiraj(clan);

            showInfo("Član je uspješno dodan!");

            SceneUtil.switchScene(btnDodajClana, "clanovi.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Greška pri dodavanju člana!");
        }
    }

    // Alert metode
    private void showAlert(String poruka) {
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
