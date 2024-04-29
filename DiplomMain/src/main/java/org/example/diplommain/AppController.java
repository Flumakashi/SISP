package org.example.diplommain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class AppController {
    @FXML
    private AnchorPane errorBox;
    @FXML
    private AnchorPane root;
    @FXML
    private TabPane tabPane;
    @FXML
    private GridPane timeTextFieldBox;
    @FXML
    private GridPane textFieldBox;
    @FXML
    private VBox buttonBox;
    @FXML
    private HBox inputKBox;
    @FXML
    private Button buttonGetInfo;
    @FXML
    private Button buttonSaveExcel;
    @FXML
    private TableView<DataModel> tableView1;
    @FXML
    private TableView<DataModel> tableView2;
    @FXML
    private TableView<Double[]> tableView3;
    @FXML
    private TableView<Double[]> tableView4;
    @FXML
    private TableView<DataTimeModel> tableView5;
    @FXML
    private TableView<Double[]> tableView6;

    private ObservableList<DataModel> dataList; // лист для хранения данных
    private ObservableList<DataTimeModel> dataTimeList;
    private Database database = new Database();
    private TextField[] numTextField;
    private TextField[] timeTextField;
    private int k = 0; // переменная количества остановок

    public void initialize() {
    }

    @FXML
    public void onLoadData() {  // Загрузка данных из бд, вывод итогов вычеслений
        clearFields();
        if (dataList == null || dataTimeList == null) { // проверка на заполненость в листе данных
            dataList = FXCollections.observableArrayList(); // Инициализация dataList
            dataTimeList = FXCollections.observableArrayList();
        } else {
            dataList.clear();
            dataTimeList.clear();
        }
        dataList = database.getDataFromDB();
        dataTimeList = database.getTimeData();

        getCalculation1();
        getCalculation2();
        getCalculation3();
        getCalculation4();
        getCalculation5();
        getCalculation6();
    }

    @FXML
    public void onCreateNewData() { //Создание новый данных для вычислений
        clearFields();

        TextField kTextField = new TextField();
        inputKBox.getChildren().add(kTextField); // создание поля ввода k
        Button kButton = new Button("Подтвердить");
        buttonBox.getChildren().add(kButton);
        kButton.setOnAction(event -> { // создание полей для ввода исходных данных
            textFieldBox.getChildren().clear();
            timeTextFieldBox.getChildren().clear();
            int numRows = 3;
            int numCols = 7;
            if (isTextFieldFilled(kTextField)) {  // проверка на заполненность поля
                k = Integer.parseInt(kTextField.getText());
                numTextField = new TextField[k];
                int row = 0;
                int col = 0;

                for (int i = 0; i < k; i++) {
                    TextField textField = createNumericTextField();
                    numTextField[i] = textField;
                    textField.setPrefWidth(35); // Устанавливаем ширину текстового поля
                    textFieldBox.add(textField, col, row); // Добавляем TextField в GridPane
                    col++;
                    if (col >= numCols) {
                        col = 0;
                        row++;
                        if (row >= numRows) {
                            // Если закончились строки, выходим из цикла
                            break;
                        }
                    }
                }
                numRows=3;
                numCols=6;
                row = 0;
                col = 0;
                timeTextField = new TextField[18];
                for (int i = 0; i < 18; i++) {
                    TextField textField = createNumericTextField();
                    timeTextField[i] = textField;
                    textField.setPrefWidth(35); // Устанавливаем ширину текстового поля
                    timeTextFieldBox.add(textField, col, row); // Добавляем TextField в GridPane
                    col++;
                    if (col >= numCols) {
                        col = 0;
                        row++;
                        if (row >= numRows) {
                            // Если закончились строки, выходим из цикла
                            break;
                        }
                    }
                }


            } else {
                errorBox.getChildren().clear();
                Label errorLabel = new Label("Пожалуйста, заполните все поля");
                errorBox.getChildren().add(errorLabel);
            }
        });

        Button numButton = new Button("Сохранить");
        buttonBox.getChildren().add(numButton);
        numButton.setOnAction(event -> {

            if (dataList == null || dataTimeList == null) {
                dataList = FXCollections.observableArrayList(); // Инициализация dataList
                dataTimeList = FXCollections.observableArrayList();
            } else {
                dataList.clear();
                dataTimeList.clear();
            }
            saveData();
            saveTimeData();

        });
    }

    public void getCalculation1() {     // показ исходных данных
        tableView1.getColumns().clear();

        // добавление столбцов
        TableColumn<DataModel, Integer> idColumn = new TableColumn<>("Номер остановки");
        TableColumn<DataModel, Double> numColumn = new TableColumn<>("Пассажирообмен(di)");

        // заполнение столбцов данными
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        numColumn.setCellValueFactory(cellData -> cellData.getValue().getNumProperty().asObject());
        tableView1.getColumns().addAll(idColumn, numColumn);
        tableView1.setItems(dataList);
    }

    public void getCalculation2() {         // расчет пассажирообмен di/k
        tableView2.getColumns().clear();
        TableColumn<DataModel, Double> numСolumn = new TableColumn<>("Пассажирообмен(di)");
        TableColumn<DataModel, Double> calculatedСolumn = new TableColumn<>("Пассажирообмен(di)/k");
        numСolumn.setCellValueFactory(cellData -> cellData.getValue().getNumProperty().asObject());
        calculatedСolumn.setCellValueFactory(cellData -> cellData.getValue().getNumProperty().divide(14.0).asObject());
        tableView2.getColumns().addAll(numСolumn, calculatedСolumn);
        tableView2.setItems(dataList);
    }

    public CalculationResult getCalculation3() {          // расчет матрицы привлекательности P
        tableView3.getColumns().clear();
        ArrayList<Double> list = new ArrayList<>();
        for (DataModel data : dataList) {
            list.add(data.getNum());
        }

        double[][] matrix = buildMatrix(list);//создаем матрицу
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
        return new CalculationResult(data, matrix);
    }

    public CalculationResult getCalculation4() {        // расчет матрицы распределения объемов корреспонденций pij*di/k
        tableView4.getColumns().clear();
        ArrayList<Double> list = new ArrayList<>();
        for (DataModel data : dataList) {
            list.add(data.getNum());
        }

        for (int i = 0; i < list.size(); i++) {
            list.set(i, Math.round(list.get(i) / 14 * 1000) / 1000.0);
        }
        double[][] matrix = buildMatrix(list);//создаем матрицу
        ObservableList<Double[]> data = FXCollections.observableArrayList();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (j == i) matrix[i][j] = 0.0;
                else {
                    matrix[i][j] = Math.round(list.get(i) * (matrix[i][j]) * 10000.0) / 10000.0;
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
        return new CalculationResult(data, matrix);
    }

    public void getCalculation5(){                        // Показ временных показателей
        tableView5.getColumns().clear();
        TableColumn<DataTimeModel, Double> timenumcolumn = new TableColumn<>("Пассажирообмен часового интервала");
        TableColumn<DataTimeModel, Double> koefcolumn = new TableColumn<>("Весовые коэффиценты");
        timenumcolumn.setCellValueFactory(cellData -> cellData.getValue().getTimeNumProperty().asObject());
        ArrayList<Double> list = new ArrayList<>();
        for(DataTimeModel datalist: dataTimeList){
            list.add(datalist.getTimeNum());
        }
        double sum = list.stream().mapToDouble(Double::doubleValue).sum();
        koefcolumn.setCellValueFactory(cellData -> cellData.getValue().getTimeNumProperty().divide(sum).asObject());
        tableView5.getColumns().addAll(timenumcolumn,koefcolumn);
        tableView5.setItems(dataTimeList);
    }

    public CalculationResult getCalculation6(){              // Расчет матрицы интенсивностей на часовой интервал t
        tableView6.getColumns().clear();
        double wt = 0.015997;
        ArrayList<Double> list = new ArrayList<>();
        for (DataModel data : dataList) {
            list.add(data.getNum());
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
                    double buf = Math.round(list.get(i)*(matrix[i][j])* 10000.0)/ 10000.0;
                    matrix[i][j]=Math.round((buf*wt/3600)* 10000.0)/ 10000.0;
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
            tableView6.getColumns().add(column);
        }

        // Заполнение данными из матрицы
        for (double[] row : matrix) {
            Double[] rowData = new Double[row.length];
            for (int i = 0; i < row.length; i++) {
                rowData[i] = row[i];
            }
            data.add(rowData);
        }

        tableView6.setItems(data);
        list.clear();
        return new CalculationResult(data, matrix);
    }
    @FXML
    public void onSaveDataToDB(){
        CalculationResult calculationResult = getCalculation6(); // Получение результата расчета
        ObservableList<Double[]> data = calculationResult.getData(); // Получение данных из результата
        Database.saveDataToDatabase(data); // Сохранение данных в базу данных
    }

    private double[][] buildMatrix( List<Double> values) {              //функций создания матрицы
        double[][] matrix = new double[values.size()][values.size()];
        double sum=0;
        for (Double value : values) {
            sum = sum + value;          //сумма всех значений
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

    private boolean isTextFieldFilled(TextField textField){ // проверка на заполненость поля
        return !textField.getText().isEmpty();
    }

    private TextField createNumericTextField() {
        TextField textField = new TextField();

        // Создаем UnaryOperator, который фильтрует ввод и разрешает только числа
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };

        // Применяем фильтр ввода к TextField
        textField.setTextFormatter(new TextFormatter<>(filter));

        return textField;
    }

    private void saveData() {
        boolean allFieldsFilled = true;

        // Проверяем, все ли TextFields заполнены
        for (TextField textField : numTextField) {
            if (textField.getText().isEmpty()) {
                allFieldsFilled = false;
                break;
            }
        }

        if (allFieldsFilled) {
            // Добавляем данные в ObservableList
            for (int i = 0; i < numTextField.length; i++) {
                String text = numTextField[i].getText();
                dataList.add(new DataModel(i + 1, Double.parseDouble(text)));
            }
            System.out.println("Данные сохранены");
            getCalculation1();
            getCalculation2();
            getCalculation3();
            getCalculation4();
        } else {
            // Выводим сообщение об ошибке
            errorBox.getChildren().clear();
            Label errorLabel = new Label("Пожалуйста, заполните все поля");
            errorBox.getChildren().add(errorLabel);
        }
    }

    private void saveTimeData(){
        boolean allFieldsFilled = true;

        // Проверяем, все ли TextFields заполнены
        for (TextField textField : timeTextField) {
            if (textField.getText().isEmpty()) {
                allFieldsFilled = false;
                break;
            }
        }

        if (allFieldsFilled) {
            // Добавляем данные в ObservableList
            for (int i = 0; i < timeTextField.length; i++) {
                String text = timeTextField[i].getText();
                dataTimeList.add(new DataTimeModel(i + 1, Double.parseDouble(text)));
            }
            System.out.println("Данные сохранены");
            getCalculation5();
            getCalculation6();
        } else {
            // Выводим сообщение об ошибке
            errorBox.getChildren().clear();
            Label errorLabel = new Label("Пожалуйста, заполните все поля");
            errorBox.getChildren().add(errorLabel);
        }
    }

    public void clearFields(){ // очистка полей ввода
        buttonBox.getChildren().clear();
        inputKBox.getChildren().clear();
        textFieldBox.getChildren().clear();
        timeTextFieldBox.getChildren().clear();
    }

    @FXML
    public void onSaveToExcel(){
        try {
            // Запись матрицы в Excel файл
            CalculationResult calculationResult = getCalculation3();
            double[][] matrix = calculationResult.getMatrix();
            ExcelTableCreator.writeMatrixToExcel(matrix, "A:/SISP/Tables.xlsx","Матрица привлекательности");
            calculationResult = getCalculation4();
            matrix = calculationResult.getMatrix();
            ExcelTableCreator.writeMatrixToExcel(matrix, "A:/SISP/Tables.xlsx","Матрица распределения объемов корреспонденций");
            calculationResult = getCalculation6();
            matrix = calculationResult.getMatrix();
            ExcelTableCreator.writeMatrixToExcel(matrix, "A:/SISP/Tables.xlsx","Матрица интенсивностей на часовой интервал");
            System.out.println("Матрица успешно записана в Excel файл.");
        } catch (IOException e) {
            System.err.println("Ошибка при записи матрицы в Excel файл: " + e.getMessage());
        }
    }
}