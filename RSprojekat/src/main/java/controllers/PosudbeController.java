package controllers;

import dao.PosudbaDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert.AlertType;
import models.Posudba;
import utils.PDFUtil;
import utils.SceneUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import java.time.LocalDate;

public class PosudbeController {

    public TableView<Posudba> tblPosudbe;
    public TableColumn<Posudba, String> clKnjiga;
    public TableColumn<Posudba, String> clClan;
    public TableColumn<Posudba, LocalDate> clPosudba;
    public TableColumn<Posudba, LocalDate> clVracanje;
    public TableColumn<Posudba, Posudba> clVrati;
    @FXML private TextField txtSearch;

    private ObservableList<Posudba> posudbeList;

    @FXML private Label lblDashboard;
    @FXML private Label lblKnjige;
    @FXML private Label lblClanovi;
    @FXML private Label lblPosudbe;
    @FXML private ImageView imgRefresh;
    @FXML private ComboBox cmbVrati;
    @FXML private Button btnDodajPosudbu;
    @FXML
    private void dodajPosudbu() {
        SceneUtil.switchScene(btnDodajPosudbu, "dodajPosudbu.fxml");
    }

    private PosudbaDAO posudbaDAO = new PosudbaDAO();

    public void initialize() {
        imgRefresh.setImage(new Image(getClass().getResourceAsStream("/images/refresh.png")));

        imgRefresh.setOnMouseClicked(e -> {
            posudbeList = FXCollections.observableArrayList(posudbaDAO.dohvatiSvePosudbe());
            setupSearchFilter();
            txtSearch.clear();
        });

        loadPosudbe();
        // Postavljanje kolona
        clKnjiga.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getKnjiga().getNaziv())
        );
        clClan.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getClan().getIme() + " " + data.getValue().getClan().getPrezime())
        );
        clPosudba.setCellValueFactory(new PropertyValueFactory<>("datumPosudbe"));
        clVracanje.setCellValueFactory(new PropertyValueFactory<>("datumVracanja"));

        // Dugme Vrati
        clVrati.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue()));
        clVrati.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Vrati");
            private final HBox container = new HBox(btn);

            {
                container.setStyle("-fx-alignment: CENTER;");
            }
            @Override
            protected void updateItem(Posudba item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }

                if (item.getDatumVracanja() != null) {
                    btn.setDisable(true);
                } else {
                    btn.setDisable(false);
                    btn.setOnAction(e -> vratiKnjigu(item));
                }

                setGraphic(container);
            }
        });

        loadPosudbe();

        cmbVrati.setItems(FXCollections.observableArrayList(
                "Sve posudbe",
                "Aktivne posudbe",
                "Neaktivne posudbe"
        ));
        cmbVrati.setValue("Sve posudbe"); // default

        cmbVrati.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (posudbeList == null || newValue == null) return;

            FilteredList<Posudba> filteredData = new FilteredList<>(posudbeList, p -> true);

            String selected = newValue.toString();

            filteredData.setPredicate(posudba -> {
                switch (selected) {
                    case "Aktivne posudbe":
                        return posudba.getDatumVracanja() == null;
                    case "Neaktivne posudbe":
                        return posudba.getDatumVracanja() != null;
                    default: // Sve posudbe
                        return true;
                }
            });

            SortedList<Posudba> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblPosudbe.comparatorProperty());
            tblPosudbe.setItems(sortedData);
        });

        // Navigacija
        lblDashboard.setOnMouseClicked(e -> SceneUtil.switchScene(lblDashboard, "dashboard.fxml"));
        lblKnjige.setOnMouseClicked(e -> SceneUtil.switchScene(lblKnjige, "knjige.fxml"));
        lblClanovi.setOnMouseClicked(e -> SceneUtil.switchScene(lblClanovi, "clanovi.fxml"));
        lblPosudbe.setOnMouseClicked(e -> SceneUtil.switchScene(lblPosudbe, "posudbe.fxml"));
    }

    // Dohvacanjr svih posudbi
    private void loadPosudbe() {
        posudbeList = FXCollections.observableArrayList(
                posudbaDAO.dohvatiSvePosudbe()
        );
        setupSearchFilter();
    }

    // Search filter
    private void setupSearchFilter() {

        FilteredList<Posudba> filteredData =
                new FilteredList<>(posudbeList, p -> true);

        txtSearch.textProperty().addListener((obs, oldValue, newValue) -> {

            String filter = newValue.toLowerCase().trim();

            filteredData.setPredicate(posudba -> {

                if (filter.isEmpty()) {
                    return true;
                }

                if (posudba.getKnjiga() != null &&
                        posudba.getKnjiga().getNaziv().toLowerCase().contains(filter)) {
                    return true;
                }

                if (posudba.getClan() != null) {
                    String imePrezime = (posudba.getClan().getIme() + " " +
                            posudba.getClan().getPrezime()).toLowerCase();

                    if (imePrezime.contains(filter)) {
                        return true;
                    }
                }

                if (posudba.getDatumPosudbe() != null &&
                        posudba.getDatumPosudbe().toString().contains(filter)) {
                    return true;
                }

                if (posudba.getDatumVracanja() != null &&
                        posudba.getDatumVracanja().toString().contains(filter)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<Posudba> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblPosudbe.comparatorProperty());

        tblPosudbe.setItems(sortedData);
    }

    // Vracanje knjige
    private void vratiKnjigu(Posudba p) {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Potvrda vraćanja");
        confirm.setHeaderText(null);
        confirm.setContentText("Da li ste sigurni da želite vratiti knjigu " + p.getKnjiga().getNaziv() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                p.setDatumVracanja(LocalDate.now());
                posudbaDAO.vrati(p);
                loadPosudbe();
            }
        });
    }

    // Export
    @FXML
    private void exportPDF() {
        PDFUtil.exportTableToPDF(tblPosudbe, "Posudbe");
    }



}
