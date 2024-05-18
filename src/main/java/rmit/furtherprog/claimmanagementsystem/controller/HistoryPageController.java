/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import rmit.furtherprog.claimmanagementsystem.data.model.util.History;

import java.util.List;

public class HistoryPageController {
    @FXML
    private TableView<History> historyTable;

    @FXML
    private TableColumn<History, Integer> idColumn;

    @FXML
    private TableColumn<History, String> timestampColumn;

    @FXML
    private TableColumn<History, String> typeColumn;

    @FXML
    private TableColumn<History, String> eventColumn;

    @FXML
    public void initialize() {
        // Initialize the TableColumn bindings
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timeStampt"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("event"));
    }

    public void setHistoryData(List<History> historyList) {
        ObservableList<History> observableHistoryList = FXCollections.observableArrayList(historyList);
        historyTable.setItems(observableHistoryList);
    }
}
