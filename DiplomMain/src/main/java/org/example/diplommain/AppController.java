package org.example.diplommain;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppController {
    @FXML
    private TableColumn num;
    @FXML
    private TableColumn id;
    @FXML
    private TableView tableView1;
    @FXML
    private Label Label1;
    @FXML
    private Label Label2;
    @FXML
    private Button buttonInput;
    @FXML
    private TableView<Double[]> tableView;
    @FXML
    private Button butInput;
    @FXML
    private TextField textField10;

    @FXML
    private TextField textField11;

    @FXML
    private TextField textField12;

    @FXML
    private TextField textField13;

    @FXML
    private TextField textField14;
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;

    @FXML
    private TextField textField3;

    @FXML
    private TextField textField4;

    @FXML
    private TextField textField5;

    @FXML
    private TextField textField6;

    @FXML
    private TextField textField7;

    @FXML
    private TextField textField8;

    @FXML
    private TextField textField9;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    private ArrayList<Double> list=new ArrayList<>();

    @FXML
    void initialize() {

    }

    @FXML
    void onButClck(ActionEvent event) {
        //проверка на пустые значения
        if(isAllFieldsFilled(textField1,textField2,textField3,textField4,textField5,textField6,
                textField7,textField8,textField9,textField10,textField11,textField12,textField13,textField14)){
            Label2.setText("chinas");

            //получаем список входных данных
            list.add(Double.valueOf(textField1.getText()));
            list.add(Double.valueOf(textField2.getText()));
            list.add(Double.valueOf(textField3.getText()));
            list.add(Double.valueOf(textField4.getText()));
            list.add(Double.valueOf(textField5.getText()));
            list.add(Double.valueOf(textField6.getText()));
            list.add(Double.valueOf(textField7.getText()));
            list.add(Double.valueOf(textField8.getText()));
            list.add(Double.valueOf(textField9.getText()));
            list.add(Double.valueOf(textField10.getText()));
            list.add(Double.valueOf(textField11.getText()));
            list.add(Double.valueOf(textField12.getText()));
            list.add(Double.valueOf(textField13.getText()));
            list.add(Double.valueOf(textField14.getText()));

            double[][] matrix=buildMatrix(list);//создаем матрицу
            ObservableList<Double[]> data = FXCollections.observableArrayList();

            // Добавление столбцов в TableView
            for (int i = 0; i < matrix[0].length; i++) {
                final int columnIndex = i;
                TableColumn<Double[], Double> column = new TableColumn<>("Column " + i);
                column.setCellValueFactory(cellData -> {
                    Double[] rowData = cellData.getValue();
                    return new javafx.beans.property.SimpleObjectProperty<>(rowData[columnIndex]);
                });

                tableView.getColumns().add(column);
            }

            // Заполнение данными из матрицы
            for (double[] row : matrix) {
                Double[] rowData = new Double[row.length];
                for (int i = 0; i < row.length; i++) {
                    rowData[i] = row[i];
                }
                data.add(rowData);
            }

            tableView.setItems(data);
            list.clear();
        }
        else {
            Label2.setText("ne China");//если не ввели все данные
        }

    }
    private boolean isAllFieldsFilled(TextField... textFields) {            //функция проверки на пустые ячейки
        for (TextField field : textFields) {
            if (field.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
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
    @FXML
    void ChekInput(KeyEvent event) {
        String character = event.getCharacter(); // Получаем введенный символ
        if (!character.matches("[0-9]")) {
            textField1.deletePreviousChar(); // Удаляем символ, если он не является числом
        }

    }
    @FXML
    void ChekInput2(KeyEvent event) {
        String character = event.getCharacter(); // Получаем введенный символ
        if (!character.matches("[0-9]")) {
            textField2.deletePreviousChar(); // Удаляем символ, если он не является числом
        }

    }
    @FXML
    void ChekInput3(KeyEvent event) {
        String character = event.getCharacter(); // Получаем введенный символ
        if (!character.matches("[0-9]")) {
            textField3.deletePreviousChar(); // Удаляем символ, если он не является числом
        }
    }
    @FXML
    void ChekInput4(KeyEvent event) {
        String character = event.getCharacter(); // Получаем введенный символ
        if (!character.matches("[0-9]")) {
            textField4.deletePreviousChar(); // Удаляем символ, если он не является числом
        }
    }
    @FXML
    void ChekInput5(KeyEvent event) {
        String character = event.getCharacter(); // Получаем введенный символ
        if (!character.matches("[0-9]")) {
            textField5.deletePreviousChar(); // Удаляем символ, если он не является числом
        }
    }
    @FXML
    void ChekInput6(KeyEvent event) {
        String character = event.getCharacter(); // Получаем введенный символ
        if (!character.matches("[0-9]")) {
            textField6.deletePreviousChar(); // Удаляем символ, если он не является числом
        }
    }
    @FXML
    void ChekInput7(KeyEvent event) {
        String character = event.getCharacter(); // Получаем введенный символ
        if (!character.matches("[0-9]")) {
            textField7.deletePreviousChar(); // Удаляем символ, если он не является числом
        }

    }
    @FXML
    void ChekInput8(KeyEvent event) {
        String character = event.getCharacter(); // Получаем введенный символ
        if (!character.matches("[0-9]")) {
            textField8.deletePreviousChar(); // Удаляем символ, если он не является числом
        }

    }
    @FXML
    void ChekInput9(KeyEvent event) {
        String character = event.getCharacter(); // Получаем введенный символ
        if (!character.matches("[0-9]")) {
            textField9.deletePreviousChar(); // Удаляем символ, если он не является числом
        }

    }
    @FXML
    void ChekInput10(KeyEvent event) {
        String character = event.getCharacter(); // Получаем введенный символ
        if (!character.matches("[0-9]")) {
            textField10.deletePreviousChar(); // Удаляем символ, если он не является числом
        }

    }
    @FXML
    void ChekInput11(KeyEvent event) {
        String character = event.getCharacter(); // Получаем введенный символ
        if (!character.matches("[0-9]")) {
            textField11.deletePreviousChar(); // Удаляем символ, если он не является числом
        }

    }
    @FXML
    void ChekInput12(KeyEvent event) {
        String character = event.getCharacter(); // Получаем введенный символ
        if (!character.matches("[0-9]")) {
            textField12.deletePreviousChar(); // Удаляем символ, если он не является числом
        }
    }
    @FXML
    void ChekInput13(KeyEvent event) {
        String character = event.getCharacter(); // Получаем введенный символ
        if (!character.matches("[0-9]")) {
            textField13.deletePreviousChar(); // Удаляем символ, если он не является числом
        }
    }
    @FXML
    void ChekInput14(KeyEvent event) {
        String character = event.getCharacter(); // Получаем введенный символ
        if (!character.matches("[0-9]")) {
            textField14.deletePreviousChar(); // Удаляем символ, если он не является числом
        }
    }
}