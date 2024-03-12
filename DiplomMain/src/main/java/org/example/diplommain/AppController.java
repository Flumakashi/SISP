package org.example.diplommain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.ArrayList;
import java.util.List;

public class AppController {

    @FXML
    private Button buttonGetInfo;
    @FXML
    private TableView<DataModel> tableView1;
    @FXML
    private TableView<DataModel> tableView2;
    @FXML
    private TableView<Double[]> tableView3;
    public void initialize() {
    }
    @FXML
    public void loadData() {
        getCalculation1();
        getCalculation2();
        getCalculation3();
    }
    public void getCalculation1(){

        TableColumn<DataModel, Integer> idcolumn = new TableColumn<>("Номер остановки");
        TableColumn<DataModel, Double> numcolumn = new TableColumn<>("Пассажирообмен(di)");

        ObservableList<DataModel> dataList = FXCollections.observableArrayList(Database.getData());
        idcolumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        numcolumn.setCellValueFactory(cellData -> cellData.getValue().getNumProperty().asObject());
        tableView1.getColumns().addAll(idcolumn,numcolumn);
        tableView1.setItems(dataList);
    }
    public void getCalculation2(){
        ObservableList<DataModel> dataList = FXCollections.observableArrayList(Database.getData());
        TableColumn<DataModel, Double> numcolumn = new TableColumn<>("Пассажирообмен(di)");
        TableColumn<DataModel, Double> calculatedcolumn = new TableColumn<>("Пассажирообмен(di)/k");
        numcolumn.setCellValueFactory(cellData -> cellData.getValue().getNumProperty().asObject());
        calculatedcolumn.setCellValueFactory(cellData -> cellData.getValue().getNumProperty().divide(14.0).asObject());
        tableView2.getColumns().addAll(numcolumn,calculatedcolumn);
        tableView2.setItems(dataList);
    }
    public void getCalculation3() {
        ObservableList<DataModel> dataList = FXCollections.observableArrayList(Database.getData());
        ArrayList<Double> list = new ArrayList<>();
        for(DataModel datalist: dataList){
            list.add(datalist.getNum());
        }
        double[][] matrix=buildMatrix(list);//создаем матрицу
        ObservableList<Double[]> data = FXCollections.observableArrayList();

        // Добавление столбцов в TableView
        for (int i = 0; i < matrix[0].length; i++) {
            final int columnIndex = i;
            TableColumn<Double[], Double> column = new TableColumn<>(Integer.toString(i));
            column.setCellValueFactory(cellData -> {
                Double[] rowData = cellData.getValue();
                return new javafx.beans.property.SimpleObjectProperty<>(rowData[columnIndex]);
            });
            tableView3.getColumns().add(column);
        }

        // Заполнение данными из матрицы
        for (double[] row : matrix) {
            Double[] rowData = new Double[row.length];
            for (int i = 0; i < row.length; i++) {
                rowData[i] = row[i];
            }
            data.add(rowData);
        }
        tableView3.setItems(data);
        list.clear();
    }
    private double[][] buildMatrix( List<Double> values) {              //функций создания матрицы
        double[][] matrix = new double[values.size()][values.size()];
        double sum=0;
        for (int i=0;i<values.size();i++){
            sum=sum+values.get(i);          //сумма всех значений
        }
        for (int i = 0; i < values.size(); i++) {
            for (int j = 0; j < values.size(); j++) {
                if (j==i)matrix[i][j]=0.0;
                else {
                    matrix[i][j]=Math.round(values.get(j)/(sum-values.get(i))* 1000.0)/ 1000.0;   //расчет и округление
                }
            }
        }
        return matrix;
    }
}