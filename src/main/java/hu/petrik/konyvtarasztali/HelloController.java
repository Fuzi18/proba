package hu.petrik.konyvtarasztali;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class HelloController {

    @FXML
    private Button torles;
    @FXML
    private TableColumn kiadasEv;
    @FXML
    private TableColumn cim;
    @FXML
    private TableColumn szerzo;
    @FXML
    private TableColumn oldalszam;
    private KonyvtarDB db;
    @FXML
    private TableView tableview;

    @Deprecated
    private void initialize() {
        cim.setCellValueFactory(new PropertyValueFactory<>("title"));
        szerzo.setCellValueFactory(new PropertyValueFactory<>("author"));
        kiadasEv.setCellValueFactory(new PropertyValueFactory<>("publish_year"));
        oldalszam.setCellValueFactory(new PropertyValueFactory<>("page_count"));
        try {
            db = new KonyvtarDB();
            tableview.getItems().addAll(db.readKonyv());
        } catch (SQLException e) {
            Platform.runLater(() -> {
                sqlAlert(e);
                Platform.exit();
            });
        }
    }

    private void sqlAlert(SQLException e) {
        alert(Alert.AlertType.ERROR,
                "Hiba történt az adatbázis kapcsolat kialakításakor",
                e.getMessage());
    }

    private Optional<ButtonType> alert(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    @Deprecated
    public void torlesClick(ActionEvent actionEvent) {
        Konyv selected = getSelectedKonyv();
        if (selected == null){
            alert(Alert.AlertType.WARNING, "Törléshez előbb válasszon ki könyvet", "");
            return;
        }
        Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION,
                "Biztos szeretné törölni a kiválasztott könyvet?", "");
        if (optionalButtonType.isEmpty() ||
                (!optionalButtonType.get().equals(ButtonType.OK) &&
                        !optionalButtonType.get().equals(ButtonType.YES))) {
            return;
        }

        try {
            if (db.deleteKonyv(selected.getId())) {
                alert(Alert.AlertType.WARNING, "Sikeres törlés", "");
            } else {
                alert(Alert.AlertType.WARNING, "Sikertelen törlés", "");
            }
            tableview.setItems(FXCollections.observableList(db.readKonyv()));
        } catch (SQLException e) {
            sqlAlert(e);
        }
    }

    private Konyv getSelectedKonyv() {
    }



    @FXML
    public void onClickDelete(ActionEvent actionEvent) {
    }
}