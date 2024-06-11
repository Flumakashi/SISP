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
import java.util.stream.Collectors;

import static org.example.diplommain.ExcelTableCreator.writeMatrixToExcel;

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
    private HBox textFieldBox;
    @FXML
    private VBox buttonBox;
    @FXML
    private HBox inputKBox;
    @FXML
    private Button buttonGetInfo;
    @FXML
    private Button buttonSaveExcel;
    @FXML
    private Label promptLabel;
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

    private TextField trafficField = new TextField();
    private TextField distanceField = new TextField();
    private TextField stationNameField = new TextField();
    private TextField intervalDistanceField = new TextField();
    private TextField[] fields = {trafficField, distanceField, stationNameField, intervalDistanceField};

    private ObservableList<DataModel> dataList = FXCollections.observableArrayList(); // лист для хранения данных
    private ObservableList<DataTimeModel> dataTimeList;
    private final Database database = new Database();
    private TextField[] timeTextField;
    private int k = 0; // переменная количества остановок
    private int currentRecordIndex = 0;
    private Button kButton = new Button("Подтвердить");
    private Button nextButton = new Button("Следующий");
    private Button saveButton = new Button("Сохранить");

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

        getStartInfo();
        getTrafficCalculation();
        getАttractivenessCalculation();
        getCorrespondenceCalculation();
        getStartTimeInfo();
        getIntensitiesCalculation();
    }

    @FXML
    public void onCreateNewData() { //Создание новый данных для вычислений
        clearFields();
        dataList.clear();
        TextField kTextField = new TextField();
        inputKBox.getChildren().add(kTextField); // создание поля ввода k
        makeNumericTextField(kTextField);

        buttonBox.getChildren().add(kButton);
        kButton.setOnAction(event -> {
            if (isTextFieldFilled(kTextField)) {
                k = Integer.parseInt(kTextField.getText());
                buttonBox.getChildren().add(nextButton);
                buttonBox.getChildren().remove(kButton);
                nextButton.setOnAction(event1 -> {
                    handleNextButton();
                });

                trafficField.setPromptText("Трафик");
                distanceField.setPromptText("Дистанция");
                stationNameField.setPromptText("Название остановки");
                intervalDistanceField.setPromptText("Интервал");

                makeNumericTextField(trafficField);
                makeNumericTextField(distanceField);
                makeNumericTextField(intervalDistanceField);

                textFieldBox.getChildren().addAll(distanceField, stationNameField, trafficField , intervalDistanceField);

                for (int i = 0; i < k; i++) {
                    dataList.add(new DataModel(i+1));
                }
            } else {
                errorBox.getChildren().clear();
                Label errorLabel = new Label("Пожалуйста, заполните все поля");
                errorBox.getChildren().add(errorLabel);
            }



        });
    }


    private void handleNextButton() {

        String[] inputValues = new String[fields.length];
        boolean allFieldsFilled = true;
        for (int i = 0; i < fields.length; i++) {
            inputValues[i] = fields[i].getText();
            if (inputValues[i].isEmpty()) {
                allFieldsFilled = false;
            }
        }

        if (!allFieldsFilled) {
            errorBox.getChildren().clear();
            Label errorLabel = new Label("Пожалуйста, заполните все поля");
            errorBox.getChildren().add(errorLabel);
            return;
        }

        DataModel currentDataModel = dataList.get(currentRecordIndex);
        currentDataModel.setTraffic(Integer.parseInt(inputValues[0]));
        currentDataModel.setDistance(Double.parseDouble(inputValues[1]));
        currentDataModel.setStationName(inputValues[2]);
        currentDataModel.setIntervalDistance(Integer.parseInt(inputValues[3]));

        // Очищаем текстовые поля
        for (TextField field : fields) {
            field.clear();
        }

        currentRecordIndex++;
        if (currentRecordIndex >= k) {
            buttonBox.getChildren().remove(nextButton);
            textFieldBox.getChildren().clear();
            currentRecordIndex = 0; // Сбрасываем индекс записей, если все записи заполнены
            System.out.println("Все данные заполнены и сохранены.");

            int numRows = 4;
            int numCols = 6;
            int row = 0;
            int col = 0;
            timeTextField = new TextField[24];
            for (int i = 0; i < 24; i++) {
                TextField textField = new TextField();
                makeNumericTextField(textField);
                textField.setPromptText(i + 1 + ":00");
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
            buttonBox.getChildren().add(saveButton);
            saveButton.setOnAction(event -> {

                if (dataList == null || dataTimeList == null) {
                    dataTimeList = FXCollections.observableArrayList();
                } else {
                    dataTimeList.clear();
                }
                saveTimeData();

            });
        }
    }

    public void getStartInfo() {     // показ исходных данных
        tableView1.getColumns().clear();

        // добавление столбцов
        TableColumn<DataModel, Integer> idColumn = new TableColumn<>("Номер остановки");
        TableColumn<DataModel, Integer> numColumn = new TableColumn<>("Пассажирообмен(di)");

        // заполнение столбцов данными
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        numColumn.setCellValueFactory(cellData -> cellData.getValue().getTrafficProperty().asObject());
        tableView1.getColumns().addAll(idColumn, numColumn);
        tableView1.setItems(dataList);
    }


    public void getTrafficCalculation() {         // расчет пассажирообмен di/k
        tableView2.getColumns().clear();
        TableColumn<DataModel, Integer> trafficСolumn = new TableColumn<>("Пассажирообмен(di)");
        TableColumn<DataModel, Double> calculatedСolumn = new TableColumn<>("Пассажирообмен(di)/k");
        trafficСolumn.setCellValueFactory(cellData -> cellData.getValue().getTrafficProperty().asObject());
        calculatedСolumn.setCellValueFactory(cellData -> cellData.getValue().getTrafficProperty().divide(14.0).asObject());
        tableView2.getColumns().addAll(trafficСolumn, calculatedСolumn);
        tableView2.setItems(dataList);
    }


    public CalculationResult getАttractivenessCalculation() {          // расчет матрицы привлекательности P
        tableView3.getColumns().clear();
        ArrayList<Integer> list = new ArrayList<>();
        for (DataModel data : dataList) {
            list.add(data.getTraffic());
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

    public CalculationResult getCorrespondenceCalculation() {        // расчет матрицы распределения объемов корреспонденций pij*di/k
        tableView4.getColumns().clear();
        ArrayList<Integer> list = new ArrayList<>();
        for (DataModel data : dataList) {
            list.add(data.getTraffic());
        }

        for (int i = 0; i < list.size(); i++) {
            list.set(i, (int) (Math.round(list.get(i) / 14 * 1000) / 1000.0));
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

    public void getStartTimeInfo(){                        // Показ временных показателей
        tableView5.getColumns().clear();
        TableColumn<DataTimeModel, Double> timenumcolumn = new TableColumn<>("Пассажирообмен часового интервала");
        TableColumn<DataTimeModel, Double> koefcolumn = new TableColumn<>("Весовые коэффиценты");
        timenumcolumn.setCellValueFactory(cellData -> cellData.getValue().getPassengerExchangeProperty().asObject());
        ArrayList<Double> list = new ArrayList<>();
        for(DataTimeModel datalist: dataTimeList){
            list.add(datalist.getTimeNum());
        }
        double sum = list.stream().mapToDouble(Double::doubleValue).sum();
        koefcolumn.setCellValueFactory(cellData -> cellData.getValue().getPassengerExchangeProperty().divide(sum).asObject());
        tableView5.getColumns().addAll(timenumcolumn,koefcolumn);
        tableView5.setItems(dataTimeList);
    }


    public CalculationResult getIntensitiesCalculation(){              // Расчет матрицы интенсивностей на часовой интервал t
        tableView6.getColumns().clear();

        ArrayList<Integer> list = new ArrayList<>();
        for (DataModel data : dataList) {
            list.add(data.getTraffic());
        }
        ArrayList<Double> timeList = new ArrayList<>();
        for(DataTimeModel datalist: dataTimeList){
            timeList.add(datalist.getTimeNum());
        }
        double sum = timeList.stream().mapToDouble(Double::doubleValue).sum();
        List<Double> wt = timeList.stream()
                .map(time -> time / sum)
                .collect(Collectors.toList());

        for (int i = 0; i < list.size(); i++) {
            list.set(i, (int) (Math.round(list.get(i) / 14 * 1000) / 1000.0));
        }
        double[][] matrix = buildMatrix(list);//создаем матрицу
        ObservableList<Double[]> data = FXCollections.observableArrayList();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (j==i)matrix[i][j]=0.0;
                else {
                    double buf = Math.round(list.get(i)*(matrix[i][j])* 10000.0)/ 10000.0;
                    matrix[i][j]=Math.round((buf * wt.get(j) / 3600) * 10000.0) / 10000.0;
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
    public void onSaveDataToDB(){ // Метод обновления данных в бд
        List<DataModel> dataListForUpdate = new ArrayList<>(dataList);
        Database.updateStations(dataListForUpdate);
        double sum = dataTimeList.stream().mapToDouble(DataTimeModel::getPassengerExchange).sum();
        ArrayList<Object[]> timeList = new ArrayList<>();
        for (DataTimeModel data: dataTimeList){
            double passengerExchange = data.getPassengerExchange();
            double gravitation = passengerExchange / sum;
            timeList.add(new Object[]{data.getId(), passengerExchange, gravitation});
        }
        Database.updatePassengerExchangeAndGravitation(timeList);

//        CalculationResult calculationResult = getCalculation6(); // Получение результата расчета
//        ObservableList<Double[]> data = calculationResult.getData(); // Получение данных из результата
//        Database.saveMatrixToDatabase(data); // Сохранение данных в базу данных
    }

    private double[][] buildMatrix( List<Integer> values) {              //функций создания матрицы
        double[][] matrix = new double[values.size()][values.size()];
        double sum=0;
        for (Integer value : values) {
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
                dataTimeList.add(new DataTimeModel(i + 25, Double.parseDouble(text)));
            }
            System.out.println("Данные сохранены");
            getStartInfo();
            getTrafficCalculation();
            getАttractivenessCalculation();
            getCorrespondenceCalculation();
            getStartTimeInfo();
            getIntensitiesCalculation();

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
    public void onSaveToExcel() {
        try {
            // Запись матриц в Excel файл
            CalculationResult calculationResult = getАttractivenessCalculation();
            double[][] matrix = calculationResult.getMatrix();
            writeMatrixToExcel(matrix, "A:/SISP/Tables.xlsx", "Матрица привлекательности");

            calculationResult = getCorrespondenceCalculation();
            matrix = calculationResult.getMatrix();
            writeMatrixToExcel(matrix, "A:/SISP/Tables.xlsx", "Матрица распределения объемов корреспонденций");

            calculationResult = getIntensitiesCalculation();
            matrix = calculationResult.getMatrix();
            writeMatrixToExcel(matrix, "A:/SISP/Tables.xlsx", "Матрица интенсивностей на часовой интервал");

            System.out.println("Матрицы успешно записаны в Excel файл.");
        } catch (IOException e) {
            System.err.println("Ошибка при записи матрицы в Excel файл: " + e.getMessage());
        }
    }

    private void makeNumericTextField(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}