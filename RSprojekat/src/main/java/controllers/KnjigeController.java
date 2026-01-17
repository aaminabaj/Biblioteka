package controllers;

import dao.KnjigaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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
import models.Knjiga;
import utils.PDFUtil;
import utils.SceneUtil;

public class KnjigeController {

    @FXML private TableView<Knjiga> tblKnjige;
    @FXML private TableColumn<Knjiga, String> clNaziv;
    @FXML private TableColumn<Knjiga, String> clAutor;
    @FXML private TableColumn<Knjiga, Knjiga.Zanr> clZanr;
    @FXML private TableColumn<Knjiga, String> clIzdavac;
    @FXML private TableColumn<Knjiga, Integer> clGodina;
    @FXML private TableColumn<Knjiga, Boolean> clStatus;
    @FXML private TableColumn<Knjiga, Void> clEdit;
    @FXML private TableColumn<Knjiga, Void> clDelete;

    @FXML private Label lblDashboard;
    @FXML private Label lblKnjige;
    @FXML private Label lblClanovi;
    @FXML private Label lblPosudbe;

    @FXML private Button btnDodajKnjigu;
    @FXML private ImageView imgRefresh;

    @FXML private TextField txtSearch;

    private final KnjigaDAO dao = new KnjigaDAO();
    private ObservableList<Knjiga> knjigeList;

    @FXML
    private void dodajKnjigu() {
        SceneUtil.switchScene(btnDodajKnjigu, "dodajKnjigu.fxml");
    }

    @FXML
    private ComboBox<Knjiga.Zanr> cmbZanr; // već postoji u FXML

    @FXML
    public void initialize() {

        cmbZanr.setItems(FXCollections.observableArrayList(Knjiga.Zanr.values()));
        cmbZanr.getItems().add(0, null); // opcionalno: prva opcija "Sve"
        cmbZanr.setPromptText("Svi žanrovi");

        Image refreshImage = new Image(getClass().getResourceAsStream("/images/refresh.png"));
        imgRefresh.setImage(refreshImage);

        imgRefresh.setOnMouseClicked(event -> refreshTable());
        clNaziv.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        clAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        clZanr.setCellValueFactory(new PropertyValueFactory<>("zanr"));
        clIzdavac.setCellValueFactory(new PropertyValueFactory<>("izdavackaKuca"));
        clGodina.setCellValueFactory(new PropertyValueFactory<>("godinaIzdavanja"));
        clStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // prikaz boolean kao "dostupno/nedostupno"
        clStatus.setCellFactory(col -> new TableCell<Knjiga, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Dostupno" : "Nedostupno");
                }
            }
        });

        // Kolona za edit
        clEdit.setCellFactory(col -> new TableCell<>() {

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
                        Knjiga k = getTableView().getItems().get(getIndex());
                        openAzurirajKnjigu(k);
                        event.consume();
                    });
                    setGraphic(container);
                }
            }
        });


        // Kolona za delete
        clDelete.setCellFactory(col -> new TableCell<>() {

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
                        Knjiga k = getTableView().getItems().get(getIndex());

                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Brisanje");
                        confirm.setHeaderText(null);
                        confirm.setContentText("Da li ste sigurni da želite obrisati knjigu?");

                        confirm.showAndWait().ifPresent(result -> {
                            if (result == ButtonType.OK) {
                                // Logika - ne moze se obrisati knjiga ako ima posudba
                                boolean hasPosudbe = dao.imaPosudbe(k.getId());

                                if (hasPosudbe) {
                                    Alert info = new Alert(Alert.AlertType.WARNING);
                                    info.setTitle("Brisanje nije moguće");
                                    info.setHeaderText(null);
                                    info.setContentText("Knjiga je povezana s posudbama i ne može se obrisati.");
                                    info.showAndWait();
                                } else {
                                    dao.brisi(k.getId());
                                    knjigeList.remove(k);
                                }
                            }
                        });

                        event.consume();
                    });
                    setGraphic(container);
                }
            }
        });

        // Dohvacanje svih knjiga
        knjigeList = FXCollections.observableArrayList(dao.dohvatiSve());

        // Navigacija
        lblDashboard.setOnMouseClicked(e -> SceneUtil.switchScene(lblDashboard, "dashboard.fxml"));
        lblKnjige.setOnMouseClicked(e -> SceneUtil.switchScene(lblKnjige, "knjige.fxml"));
        lblClanovi.setOnMouseClicked(e -> SceneUtil.switchScene(lblClanovi, "clanovi.fxml"));
        lblPosudbe.setOnMouseClicked(e -> SceneUtil.switchScene(lblPosudbe, "posudbe.fxml"));

        setupSearchFilter();
    }

    // Azuriranje knjige
    private void openAzurirajKnjigu(Knjiga knjiga) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/azurirajKnjigu.fxml"));
            Parent root = loader.load();

            AzurirajKnjiguController controller = loader.getController();
            controller.setKnjiga(knjiga); // 👈 šaljemo knjigu

            Stage stage = (Stage) tblKnjige.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Refresh podataka u tabeli
    private void refreshTable() {
        knjigeList.setAll(dao.dohvatiSve());

        txtSearch.clear();
    }

    // Search filter
    private void setupSearchFilter() {
        FilteredList<Knjiga> filteredData = new FilteredList<>(knjigeList, k -> true);

        txtSearch.textProperty().addListener((obs, oldValue, newValue) -> applyFilter(filteredData));
        cmbZanr.valueProperty().addListener((obs, oldValue, newValue) -> applyFilter(filteredData));

        SortedList<Knjiga> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblKnjige.comparatorProperty());

        tblKnjige.setItems(sortedData);
    }

    // Metoda koja primjenjuje filter
    private void applyFilter(FilteredList<Knjiga> filteredData) {
        String searchText = txtSearch.getText().toLowerCase().trim();
        Knjiga.Zanr selectedZanr = cmbZanr.getValue();

        filteredData.setPredicate(k -> {
            boolean matchesSearch = searchText.isEmpty() ||
                    k.getNaziv().toLowerCase().contains(searchText) ||
                    k.getAutor().toLowerCase().contains(searchText) ||
                    k.getIzdavackaKuca().toLowerCase().contains(searchText) ||
                    String.valueOf(k.getGodinaIzdavanja()).contains(searchText);

            boolean matchesZanr = selectedZanr == null || k.getZanr() == selectedZanr;

            return matchesSearch && matchesZanr;
        });
    }

    // Export
    @FXML
    public void exportPDF() {
        PDFUtil.exportTableToPDF(tblKnjige, "Knjige");
    }
}
