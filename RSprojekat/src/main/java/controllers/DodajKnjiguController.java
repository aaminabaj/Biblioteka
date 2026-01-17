package controllers;

import dao.KnjigaDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Knjiga;
import utils.SceneUtil;

public class DodajKnjiguController {

    @FXML
    private TextField txtNaziv;
    @FXML
    private TextField txtAutor;
    @FXML
    private TextField txtIzdavac;
    @FXML
    private TextField txtGodinaIzdanja;
    @FXML
    private ComboBox<Knjiga.Zanr> cmbZanr;
    @FXML
    private Button btnDodajKnjigu;
    @FXML
    private ImageView imgBack;

    @FXML
    public void initialize() {
        Image img = new Image(getClass().getResourceAsStream("/images/back.png"));
        imgBack.setImage(img);
        imgBack.setOnMouseClicked(event -> {
            Stage stage = (Stage) imgBack.getScene().getWindow();
            SceneUtil.switchScene(stage, "knjige.fxml", 1000, 600);
        });

        cmbZanr.getItems().setAll(Knjiga.Zanr.values());

        btnDodajKnjigu.setOnAction(e -> dodajKnjigu());
    }

    // Dodavanje knjige - sva polja moraju biti popunjena + godina izdavanja mora biti broj
    private void dodajKnjigu() {
        try {
            String naziv = txtNaziv.getText().trim();
            String autor = txtAutor.getText().trim();
            String izdavac = txtIzdavac.getText().trim();
            String godinaText = txtGodinaIzdanja.getText().trim();
            Knjiga.Zanr zanr = cmbZanr.getValue();

            if (naziv.isEmpty() || autor.isEmpty() || izdavac.isEmpty() || godinaText.isEmpty() || zanr == null) {
                showAlert("Sva polja se moraju popuniti!");
                return;
            }

            int godina;
            try {
                godina = Integer.parseInt(godinaText);
            } catch (NumberFormatException ex) {
                showAlert("Godina izdavanja mora biti broj!");
                return;
            }

            Knjiga k = new Knjiga();
            k.setNaziv(naziv);
            k.setAutor(autor);
            k.setIzdavackaKuca(izdavac);
            k.setGodinaIzdavanja(godina);
            k.setZanr(zanr);
            k.setStatus(true); // dostupna

            KnjigaDAO dao = new KnjigaDAO();
            dao.kreiraj(k);

            Stage stage = (Stage) btnDodajKnjigu.getScene().getWindow();
            SceneUtil.switchScene(stage, "knjige.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Greška prilikom dodavanja knjige!");
        }
    }

    // Alert metoda
    private void showAlert(String poruka) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(poruka);
        alert.showAndWait();
    }

}
