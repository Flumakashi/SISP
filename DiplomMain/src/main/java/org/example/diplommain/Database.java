package org.example.diplommain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;

public class Database {

    private static final String dbHost = "localhost";
    private static final String dbPort = "5432";
    private static final String dbUser = "postgres";
    private static final String dbPass = "1209qwpo";
    private static final String dbName = "Elen2";
    private static final String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, dbUser, dbPass);
    }

    public ObservableList<DataModel> getDataFromDB(){ // Получение исходных данных из бд
        ObservableList<DataModel> data = FXCollections.observableArrayList();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM public.\"Stations\"")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                double distance = resultSet.getDouble("Distance");
                String stationName = resultSet.getString("StationName");
                int traffic = resultSet.getInt("Traffic");
                int intervalDistance = resultSet.getInt("IntervalDistance");
                data.add(new DataModel(id,distance,stationName,traffic,intervalDistance));
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
             ResultSet resultSet = statement.executeQuery("SELECT * FROM public.\"Intervals\"")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                double passengerExchange = resultSet.getDouble("PassengerExchange");
                dataList.add(new DataTimeModel(id, passengerExchange));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static void saveMatrixToDatabase(ObservableList<Double[]> dataList) {
        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPass)) {
            PreparedStatement dropTableStatement = connection.prepareStatement("DROP TABLE IF EXISTS MetrixQueue");
            dropTableStatement.executeUpdate();
            // Создаем динамический SQL-запрос для создания таблицы
            StringBuilder createTableSql = new StringBuilder("CREATE TABLE MetrixQueue (");
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
            StringBuilder insertDataSql = new StringBuilder("INSERT INTO MetrixQueue (");
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

    public static void updateStations(List<DataModel> dataList) {
        String deleteSQL = "DELETE FROM public.\"Stations\"";
        String insertSQL = "INSERT INTO public.\"Stations\" (\"ID\", \"Distance\", \"StationName\", \"Traffic\", \"IntervalDistance\") VALUES (?, ?, ?, ?, ?)";
//                "UPDATE public.\"Stations\" " +
//                "SET \"Distance\" = ?, \"StationName\" = ?, \"Traffic\" = ?, \"IntervalDistance\" = ? " +
//                "WHERE \"ID\" = ?";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPass)) {
            // Начало транзакции
            connection.setAutoCommit(false); // Начинаем транзакцию

            try (Statement deleteStatement = connection.createStatement();
                 PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {

                deleteStatement.executeUpdate(deleteSQL); // Удаляем все старые записи

                for (DataModel data : dataList) {
                    // Извлечение значений из свойств
                    int id = data.getId();
                    double distance = data.getDistance();
                    String stationNameString = data.getStationName();
                    int traffic = data.getTraffic();
                    int intervalDistance = data.getIntervalDistance();

                    // Разбиение строки stationName на массив
                    String[] stationNamesArray = stationNameString.split(",");
                    Array sqlArray = connection.createArrayOf("text", stationNamesArray);

                    // Установка параметров в PreparedStatement
                    insertStatement.setInt(1, id);
                    insertStatement.setDouble(2, distance);
                    insertStatement.setArray(3, sqlArray);
                    insertStatement.setInt(4, traffic);
                    insertStatement.setInt(5, intervalDistance);

                    insertStatement.addBatch();
                }

                insertStatement.executeBatch(); // Вставляем все новые записи

                connection.commit(); // Подтверждаем транзакцию
            } catch (SQLException e) {
                connection.rollback(); // Откатываем изменения в случае ошибки
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePassengerExchangeAndGravitation(List<Object[]> timeList) {
        String updateQuery = "UPDATE public.\"Intervals\" SET \"PassengerExchange\" = ?, \"Gravitation\" = ? WHERE \"ID\" = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {

            for (Object[] updateData : timeList) {
                int id = (int) updateData[0];
                double passengerExchange = (double) updateData[1];
                double gravitation = (double) updateData[2];

                preparedStatement.setDouble(1, passengerExchange);
                preparedStatement.setDouble(2, gravitation);
                preparedStatement.setInt(3, id);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
