package org.example.diplommain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBFunctions extends Configs {

    //Подключение к БД
    Connection dbConnection = null;
    public Connection getDbConnection() {

        try {
            Class.forName("org.postgresql.Driver");
            dbConnection = DriverManager.getConnection(url, dbUser, dbPass);
            if(dbConnection!=null){
                System.out.println("Connection established");
            }
            else {
                System.out.println("Connection failed");
            }

        }catch (Exception e){
            System.out.println(e);
        }

        return dbConnection;
    }

    public ObservableList<DataModel> fetchDataFromDatabase() {
        ObservableList<DataModel> result = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPass)) {
            String sql = "SELECT id, num FROM road_data";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String idValue = resultSet.getString("id");
                    String numValue = resultSet.getString("num");
                    result.add(new DataModel(idValue, numValue));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


}
