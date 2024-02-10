package org.example.diplommain;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static List<DataModel> getData(){
        List<DataModel> dataList = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM sispinfo")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double num = resultSet.getDouble("num");
                dataList.add(new DataModel(id, num));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
