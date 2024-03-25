package org.example.diplommain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class AppController {

    @FXML
    private AnchorPane root;
    @FXML
    private TabPane tabPane;
    @FXML
    private HBox textFieldBox;
    @FXML
    private VBox buttonBox;
    @FXML
    private HBox inputKBox;
    @FXML
    private Button buttonGetInfo;
    @FXML
    private TableView<DataModel.DataEntry> tableView1;
    @FXML
    private TableView<DataModel.DataEntry> tableView2;
    @FXML
    private TableView<Double[]> tableView3;
    @FXML
    private TableView<Double[]> tableView4;
    @FXML
    private TableView<DataTimeModel> tableView5;

    private DataModel dataModel = new DataModel();

    private int k = 0;

    public void initialize() {
    }
    @FXML
    public void onLoadData() {
        clearTable(root);
        getCalculation1();
        getCalculation2();
        getCalculation3();
        getCalculation4();
        getCalculation5();
        System.out.println(k);
    }

    @FXML
    public void onLoadFromDB(){
        clearDataModel();
        Database.getDataFromDB(dataModel);
        onLoadData();
    }

    @FXML
    public void onCreateNewData(){
        clearDataModel();
        TextField kTextField = new TextField();
        inputKBox.getChildren().add(kTextField);
        Button kButton = new Button("Enter");
        buttonBox.getChildren().add(kButton);
        kButton.setOnAction(event -> {          //
            k = Integer.parseInt(kTextField.getText());
            for(int i = 0; i<k; i++){
                TextField numTextField = new TextField();
                textFieldBox.getChildren().add(numTextField);
            }
        });

        Button numButton = new Button("Save");
        buttonBox.getChildren().add(numButton);
        numButton.setOnAction(event -> {
            dataModel.getDataList().clear();
            for (int i = 0; i < k; i++) {
                if (textFieldBox.getChildren().get(i) instanceof TextField) {
                    TextField numTextField = (TextField) textFieldBox.getChildren().get(i);
                    double num = Double.parseDouble(numTextField.getText());
                    dataModel.addDataEntry(new DataModel.DataEntry(i+1, num));
                }
            }
            System.out.println("Data List:");
            for (DataModel.DataEntry entry : dataModel.getDataList()) {
                System.out.println("ID: " + entry.getId() + ", Num: " + entry.getNum());
            }
        });
    }
    public void getCalculation1(){
        TableColumn<DataModel.DataEntry, Integer> idColumn = new TableColumn<>("Номер остановки");
        TableColumn<DataModel.DataEntry, Double> numColumn = new TableColumn<>("Пассажирообмен(di)");

        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        numColumn.setCellValueFactory(cellData -> cellData.getValue().getNumProperty().asObject());
        tableView1.getColumns().addAll(idColumn,numColumn);
        tableView1.setItems(dataModel.getDataList());
    }
    public void getCalculation2(){
        TableColumn<DataModel.DataEntry, Double> numcolumn = new TableColumn<>("Пассажирообмен(di)");
        TableColumn<DataModel.DataEntry, Double> calculatedcolumn = new TableColumn<>("Пассажирообмен(di)/k");
        numcolumn.setCellValueFactory(cellData -> cellData.getValue().getNumProperty().asObject());
        calculatedcolumn.setCellValueFactory(cellData -> cellData.getValue().getNumProperty().divide(14.0).asObject());
        tableView2.getColumns().addAll(numcolumn,calculatedcolumn);
        tableView2.setItems(dataModel.getDataList());
    }
    public void getCalculation3() {
        List<DataModel.DataEntry> dataList = dataModel.getDataList();
        ArrayList<Double> list = new ArrayList<>();
        for (DataModel.DataEntry entry : dataList) {
            list.add(entry.getNum());
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

    public void getCalculation4(){
        List<DataModel.DataEntry> dataList = dataModel.getDataList();
        ArrayList<Double> list = new ArrayList<>();
        for (DataModel.DataEntry entry : dataList) {
            list.add(entry.getNum());
        }

        for (int i = 0; i < list.size(); i++) {
            list.set(i, Math.round(list.get(i) / 14 * 1000) / 1000.0);
        }
        double[][] matrix = buildMatrix(list);//создаем матрицу
        ObservableList<Double[]> data = FXCollections.observableArrayList();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (j==i)matrix[i][j]=0.0;
                else {
                    matrix[i][j]=Math.round(list.get(i)*(matrix[i][j])* 10000.0)/ 10000.0;
                }
            }
        }

        for (int i = 0; i < matrix[0].length; i++) {
            final int columnIndex = i;
            TableColumn<Double[], Double> column = new TableColumn<>(Integer.toString(i));
            column.setCellValueFactory(cellData -> {
                Double[] rowData = cellData.getValue();
                return new javafx.beans.property.SimpleObjectProperty<>(rowData[columnIndex]);
            });
            tableView4.getColumns().add(column);
        }

        // Заполнение данными из матрицы
        for (double[] row : matrix) {
            Double[] rowData = new Double[row.length];
            for (int i = 0; i < row.length; i++) {
                rowData[i] = row[i];
            }
            data.add(rowData);
        }
        tableView4.setItems(data);
        list.clear();
    }

    public void getCalculation5(){

        TableColumn<DataTimeModel, Double> timenumcolumn = new TableColumn<>("Пассажирообмен часового интервала");
        TableColumn<DataTimeModel, Double> koefcolumn = new TableColumn<>("Весовые коэффиценты");
        ObservableList<DataTimeModel> dataList = FXCollections.observableArrayList(Database.getTimeData());
        timenumcolumn.setCellValueFactory(cellData -> cellData.getValue().getTimeNumProperty().asObject());
        ArrayList<Double> list = new ArrayList<>();
        for(DataTimeModel datalist: dataList){
            list.add(datalist.getTimeNum());
        }
        double sum = list.stream().mapToDouble(Double::doubleValue).sum();
        koefcolumn.setCellValueFactory(cellData -> cellData.getValue().getTimeNumProperty().divide(sum).asObject());
        tableView5.getColumns().addAll(timenumcolumn,koefcolumn);
        tableView5.setItems(dataList);
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

    public void clearTable(AnchorPane root){
        for (Node node : root.getChildren()) {
            if (node instanceof TableView) {
                ((TableView<?>) node).getItems().clear();
            }
        }
    }
    public void clearDataModel(){
        dataModel.clearDataList();
    }
}