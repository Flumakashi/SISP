package org.example.diplommain;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AppController {
    @FXML
    private TableView<DataModel> tableViewInfo = new TableView<>();

    @FXML
    private TableColumn<DataModel,Integer> idColumn;
    @FXML
    private TableColumn<DataModel,Integer> numColumn;

    public void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        numColumn.setCellValueFactory(cellData -> cellData.getValue().numProperty().asObject());
    }

    @FXML
    public void loadData() {
        ObservableList<DataModel> dataList = FXCollections.observableArrayList(Database.getData());
        tableViewInfo.setItems(dataList);
    }

}