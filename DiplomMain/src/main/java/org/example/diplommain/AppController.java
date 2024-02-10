package org.example.diplommain;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;


public class AppController {

    @FXML
    private Button buttonGetInfo;
    @FXML
    private TableView<DataModel> tableViewInfo;

    @FXML
    private TableColumn<DataModel,Integer> idColumn;
    @FXML
    private TableColumn<DataModel,Double> numColumn;

    private DataModel dataModel;



    public void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        numColumn.setCellValueFactory(cellData -> cellData.getValue().numProperty().asObject());
    }

    @FXML
    public void loadData() {

        ObservableList<DataModel> dataList = FXCollections.observableArrayList(Database.getData());
        tableViewInfo.setItems(dataList);
    }

    @FXML
    public void getCalculation1() {
        ObservableList<DataModel> data = FXCollections.observableArrayList();
        for (DataModel item : Database.getData()) {
            // Выполняем вычисления для каждого элемента
            double calculatedValue = item.getNum() / 14.0;

            // Создаем новый объект DataModel с вычисленным значением и добавляем в список
            data.add(new DataModel((int)item.getNum(), calculatedValue));
        }
        // Устанавливаем данные в таблицу
        tableViewInfo.setItems(data);
    }
}