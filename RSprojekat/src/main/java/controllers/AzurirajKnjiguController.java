package controllers;

import dao.KnjigaDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Knjiga;
import utils.SceneUtil;

public class AzurirajKnjiguController {

    @FXML private TextField txtNaziv;
    @FXML private TextField txtAutor;
    @FXML private TextField txtIzdavac;
    @FXML private TextField txtGodinaIzdanja;
    @FXML private ComboBox<Knjiga.Zanr> cmbZanr;
    @FXML private Button btnAzurirajKnjigu;
    @FXML private ImageView imgBack;

    private Knjiga knjiga;
    private final KnjigaDAO dao = new KnjigaDAO();

    @FXML
    public void initialize() {
        cmbZanr.setItems(FXCollections.observableArrayList(Knjiga.Zanr.values()));

        imgBack.setImage(new Image(getClass().getResourceAsStream("/images/back.png")));
        imgBack.setOnMouseClicked(event -> {
            Stage stage = (Stage) imgBack.getScene().getWindow();
            SceneUtil.switchScene(stage, "knjige.fxml", 1000, 600);
        });

        btnAzurirajKnjigu.setOnAction(e -> azurirajKnjigu());
    }


    public void setKnjiga(Knjiga knjiga) {
        this.knjiga = knjiga;

        txtNaziv.setText(knjiga.getNaziv());
        txtAutor.setText(knjiga.getAutor());
        txtIzdavac.setText(knjiga.getIzdavackaKuca());
        txtGodinaIzdanja.setText(String.valueOf(knjiga.getGodinaIzdavanja()));
        cmbZanr.setValue(knjiga.getZanr());
    }

    // Azuriranje knjige
    private void azurirajKnjigu() {
        try {
            knjiga.setNaziv(txtNaziv.getText());
            knjiga.setAutor(txtAutor.getText());
            knjiga.setIzdavackaKuca(txtIzdavac.getText());
            knjiga.setGodinaIzdavanja(
                    Integer.parseInt(txtGodinaIzdanja.getText())
            );
            knjiga.setZanr(cmbZanr.getValue());

            dao.azuriraj(knjiga);

            new Alert(Alert.AlertType.INFORMATION, "Knjiga uspješno ažurirana!")
                    .showAndWait();

            SceneUtil.switchScene(btnAzurirajKnjigu, "knjige.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Greška pri ažuriranju!").showAndWait();
        }
    }
}
