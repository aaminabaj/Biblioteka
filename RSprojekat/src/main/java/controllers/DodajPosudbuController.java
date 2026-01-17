package controllers;

import dao.ClanDAO;
import dao.KnjigaDAO;
import dao.PosudbaDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Clan;
import models.Knjiga;
import models.Posudba;
import utils.SceneUtil;
import org.controlsfx.control.SearchableComboBox;

import java.time.LocalDate;
import java.util.List;

public class DodajPosudbuController {

    @FXML
    private SearchableComboBox<Clan> cmbClan;
    @FXML
    private SearchableComboBox<Knjiga> cmbKnjiga;
    @FXML
    private DatePicker dtmDatumPosudbe;
    @FXML
    private Button btnPosudi;
    @FXML
    private ImageView imgBack;

    private final PosudbaDAO posudbaDAO = new PosudbaDAO();
    private final ClanDAO clanDAO = new ClanDAO();
    private final KnjigaDAO knjigaDAO = new KnjigaDAO();


    @FXML
    public void initialize() {
        Image img = new Image(getClass().getResourceAsStream("/images/back.png"));
        imgBack.setImage(img);
        imgBack.setOnMouseClicked(event -> {
            Stage stage = (Stage) imgBack.getScene().getWindow();
            SceneUtil.switchScene(stage, "posudbe.fxml", 1000, 600);
        });

        List<Clan> clanovi = clanDAO.dohvatiSve();
        cmbClan.getItems().addAll(clanovi);
        // prikazuju se samo knjige koje su dostupne
        List<Knjiga> knjige = knjigaDAO.dohvatiSve();
        knjige.stream().filter(Knjiga::isStatus).forEach(cmbKnjiga.getItems()::add);

        btnPosudi.setOnAction(e -> posudiKnjigu());
    }

    // Posudba knjige - sva polja moraju biti popunjena
    private void posudiKnjigu() {
        Clan clan = cmbClan.getValue();
        Knjiga knjiga = cmbKnjiga.getValue();
        LocalDate datum = dtmDatumPosudbe.getValue();

        if (clan == null || knjiga == null || datum == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Molimo odaberite člana, knjigu i datum posudbe!");
            alert.showAndWait();
            return;
        }

        Posudba posudba = new Posudba();
        posudba.setClan(clan);
        posudba.setKnjiga(knjiga);
        posudba.setDatumPosudbe(datum);

        posudbaDAO.posudi(posudba);

        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle(null);
        success.setHeaderText(null);
        success.setContentText("Knjiga je uspješno posuđena!");
        success.showAndWait();

        SceneUtil.switchScene(btnPosudi, "posudbe.fxml");
    }

}
