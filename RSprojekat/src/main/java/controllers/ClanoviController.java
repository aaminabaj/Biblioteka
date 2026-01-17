package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Clan;
import dao.ClanDAO;
import utils.PDFUtil;
import utils.SceneUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class ClanoviController {

    @FXML private TableView<Clan> tblClanovi;
    @FXML private TableColumn<Clan, Integer> clBroj;
    @FXML private TableColumn<Clan, String> clIme;
    @FXML private TableColumn<Clan, String> clPrezime;
    @FXML private TableColumn<Clan, String> clMail;
    @FXML private TableColumn<Clan, String> clDatum;
    @FXML private TableColumn<Clan, Void> clEdit;
    @FXML private TableColumn<Clan, Void> clDelete;
    @FXML private Label lblDashboard;
    @FXML private Label lblKnjige;
    @FXML private Label lblClanovi;
    @FXML private Label lblPosudbe;
    @FXML private TextField txtSearch;
    @FXML private ImageView imgRefresh;
    @FXML private Button btnDodajClana;

    private ObservableList<Clan> clanoviList;
    @FXML
    private void dodajClana() {
        SceneUtil.switchScene(btnDodajClana, "dodajClana.fxml");
    }
    @FXML
    public void initialize() {
        // Logika za refresh
        Image refreshImage = new Image(getClass().getResourceAsStream("/images/refresh.png"));
        imgRefresh.setImage(refreshImage);
        imgRefresh.setOnMouseClicked(event -> refreshTable());

        // Setup tabele
        clBroj.setCellValueFactory(new PropertyValueFactory<>("brojClanske"));
        clIme.setCellValueFactory(new PropertyValueFactory<>("ime"));
        clPrezime.setCellValueFactory(new PropertyValueFactory<>("prezime"));
        clMail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        clDatum.setCellValueFactory(new PropertyValueFactory<>("datumPridruzivanja"));

        // Kolona za edit
        clEdit.setCellFactory(col -> new TableCell<Clan, Void>() {
            private final ImageView editIcon = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/edit.png"))
            );
            private final HBox container = new HBox(editIcon);
            {
                editIcon.setFitWidth(18);
                editIcon.setFitHeight(18);
                editIcon.setPreserveRatio(true);
                container.setStyle("-fx-alignment: CENTER;");
                container.setCursor(javafx.scene.Cursor.HAND);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    editIcon.setOnMouseClicked(event -> {
                        Clan c = getTableView().getItems().get(getIndex());
                        openAzurirajClana(c);
                        event.consume();
                    });
                    setGraphic(container);
                }
            }
        });

        // Kolona za delete
        clDelete.setCellFactory(col -> new TableCell<Clan, Void>() {
            private final ImageView deleteIcon = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/delete.png"))
            );
            private final HBox container = new HBox(deleteIcon);

            {
                deleteIcon.setFitWidth(18);
                deleteIcon.setFitHeight(18);
                deleteIcon.setPreserveRatio(true);
                container.setStyle("-fx-alignment: CENTER;");
                container.setCursor(javafx.scene.Cursor.HAND);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    deleteIcon.setOnMouseClicked(event -> {
                        Clan c = getTableView().getItems().get(getIndex());

                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Brisanje");
                        confirm.setHeaderText(null);
                        confirm.setContentText("Da li ste sigurni da želite obrisati člana?");

                        confirm.showAndWait().ifPresent(result -> {
                            if (result == ButtonType.OK) {
                                // Logika: ako ima posudba -> clan se ne moze obrisati
                                boolean hasPosudbe = new ClanDAO().imaPosudbe(c.getId());

                                if (hasPosudbe) {
                                    Alert info = new Alert(Alert.AlertType.WARNING);
                                    info.setTitle("Brisanje nije moguće");
                                    info.setHeaderText(null);
                                    info.setContentText("Član je povezan s posudbama i ne može se obrisati.");
                                    info.showAndWait();
                                } else {
                                    new ClanDAO().brisi(c.getId());
                                    clanoviList.remove(c);
                                }
                            }
                        });

                        event.consume();
                    });
                    setGraphic(container);
                }
            }
        });

        // Dohvacanje svih clanova
        clanoviList = FXCollections.observableArrayList(
                new ClanDAO().dohvatiSve()
        );
        setupSearchFilter();

        // Navigacija
        lblDashboard.setOnMouseClicked(e -> SceneUtil.switchScene(lblDashboard, "dashboard.fxml"));
        lblKnjige.setOnMouseClicked(e -> SceneUtil.switchScene(lblKnjige, "knjige.fxml"));
        lblClanovi.setOnMouseClicked(e -> SceneUtil.switchScene(lblClanovi, "clanovi.fxml"));
        lblPosudbe.setOnMouseClicked(e -> SceneUtil.switchScene(lblPosudbe, "posudbe.fxml"));
    }

    // Funkcija za refresh podataka u tabeli
    private void refreshTable() {
        clanoviList.setAll(new ClanDAO().dohvatiSve());
        txtSearch.clear();
    }

    // Azuriranje clana
    private void openAzurirajClana(Clan clan) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/azurirajClana.fxml"));
            Parent root = loader.load();

            AzurirajClanaController controller = loader.getController();
            controller.setClan(clan);

            Stage stage = (Stage) tblClanovi.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Search filter
    private void setupSearchFilter() {

        FilteredList<Clan> filteredData =
                new FilteredList<>(clanoviList, c -> true);

        txtSearch.textProperty().addListener((obs, oldValue, newValue) -> {

            String filter = newValue.toLowerCase().trim();

            filteredData.setPredicate(clan -> {

                if (filter.isEmpty()) {
                    return true;
                }

                if (String.valueOf(clan.getBrojClanske()).contains(filter)) {
                    return true;
                }

                if (clan.getIme().toLowerCase().contains(filter)) {
                    return true;
                }

                if (clan.getPrezime().toLowerCase().contains(filter)) {
                    return true;
                }

                if (clan.getMail().toLowerCase().contains(filter)) {
                    return true;
                }

                if (clan.getDatumPridruzivanja() != null &&
                        clan.getDatumPridruzivanja().toString().contains(filter)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<Clan> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblClanovi.comparatorProperty());

        tblClanovi.setItems(sortedData);
    }

    // Export
    @FXML
    private void exportPDF() {
        PDFUtil.exportTableToPDF(tblClanovi, "Članovi");
    }
}
