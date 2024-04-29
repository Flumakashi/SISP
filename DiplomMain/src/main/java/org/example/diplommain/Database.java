package org.example.diplommain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Database {

    private static final String dbHost = "localhost";
    private static final String dbPort = "5432";
    private static final String dbUser = "postgres";
    private static final String dbPass = "1209qwpo";
    private static final String dbName = "SISP";
    private static final String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, dbUser, dbPass);
    }

    public ObservableList<DataModel> getDataFromDB(){ // Получение исходных данных из бд
        ObservableList<DataModel> data = FXCollections.observableArrayList();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM sispinfo")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double num = resultSet.getDouble("num");
                data.add(new DataModel(id,num));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public ObservableList<DataTimeModel> getTimeData(){ // Получение временных исходных данных из бд
        ObservableList<DataTimeModel> dataList = FXCollections.observableArrayList();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM sisptimeinfo")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double timenum = resultSet.getDouble("timenum");
                dataList.add(new DataTimeModel(id, timenum));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static void saveDataToDatabase(ObservableList<Double[]> dataList) {
        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPass)) {
            PreparedStatement dropTableStatement = connection.prepareStatement("DROP TABLE IF EXISTS intensitymatrix");
            dropTableStatement.executeUpdate();
            // Создаем динамический SQL-запрос для создания таблицы
            StringBuilder createTableSql = new StringBuilder("CREATE TABLE intensitymatrix (");
            int numColumns = dataList.get(0).length;
            for (int i = 1; i <= numColumns; i++) {
                createTableSql.append("column").append(i).append(" DOUBLE PRECISION");
                if (i < numColumns) {
                    createTableSql.append(", ");
                }
            }
            createTableSql.append(")");

            // Выполняем запрос создания таблицы
            PreparedStatement createTableStatement = connection.prepareStatement(createTableSql.toString());
            createTableStatement.executeUpdate();

            // После создания таблицы можно выполнить вставку данных
            StringBuilder insertDataSql = new StringBuilder("INSERT INTO intensitymatrix (");
            for (int i = 1; i <= numColumns; i++) {
                insertDataSql.append("column").append(i);
                if (i < numColumns) {
                    insertDataSql.append(", ");
                }
            }
            insertDataSql.append(") VALUES (");
            for (int i = 0; i < numColumns; i++) {
                insertDataSql.append("?, ");
            }
            insertDataSql.delete(insertDataSql.length() - 2, insertDataSql.length()); // Удаляем последнюю запятую
            insertDataSql.append(")");

            // Выполняем запрос вставки данных
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSql.toString());
            for (Double[] row : dataList) {
                for (int i = 0; i < numColumns; i++) {
                    insertDataStatement.setDouble(i + 1, row[i]);
                }
                insertDataStatement.addBatch();
            }
            insertDataStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
