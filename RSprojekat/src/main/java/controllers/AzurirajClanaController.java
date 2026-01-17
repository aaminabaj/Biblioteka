package controllers;

import dao.ClanDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Clan;
import utils.SceneUtil;

public class AzurirajClanaController {

    @FXML private TextField txtIme;
    @FXML private TextField txtPrezime;
    @FXML private TextField txtBrojClanskeKarte;
    @FXML private TextField txtMail;
    @FXML private DatePicker dtmDatumPridruzivanja;
    @FXML private Button btnAzurirajClana;
    @FXML private ImageView imgBack;

    private Clan clan;
    private final ClanDAO dao = new ClanDAO();

    @FXML
    public void initialize() {
        imgBack.setImage(new Image(getClass().getResourceAsStream("/images/back.png")));
        imgBack.setOnMouseClicked(event -> {
            Stage stage = (Stage) imgBack.getScene().getWindow();
            SceneUtil.switchScene(stage, "clanovi.fxml", 1000, 600);
        });

        btnAzurirajClana.setOnAction(e -> azurirajClana());
    }


    public void setClan(Clan clan) {
        this.clan = clan;

        txtIme.setText(clan.getIme());
        txtPrezime.setText(clan.getPrezime());
        txtBrojClanskeKarte.setText(String.valueOf(clan.getBrojClanske()));
        txtMail.setText(clan.getMail());
        dtmDatumPridruzivanja.setValue(clan.getDatumPridruzivanja());
    }

    // Azuriranje clana
    private void azurirajClana() {
        try {
            clan.setIme(txtIme.getText());
            clan.setPrezime(txtPrezime.getText());
            clan.setBrojClanske(txtBrojClanskeKarte.getText());
            clan.setMail(txtMail.getText());
            clan.setDatumPridruzivanja(dtmDatumPridruzivanja.getValue());

            dao.azuriraj(clan);

            new Alert(Alert.AlertType.INFORMATION, "Član uspješno ažuriran!").showAndWait();

            SceneUtil.switchScene(btnAzurirajClana, "clanovi.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Greška pri ažuriranju člana!").showAndWait();
        }
    }
}
