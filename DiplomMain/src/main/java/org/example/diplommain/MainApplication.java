package org.example.diplommain;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;



public class MainApplication extends Application {

    private final ObservableList<DataModel> data = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) throws IOException {
        TableView<DataModel> tableView1 = new TableView<>();
        TableColumn<DataModel, String> id = new TableColumn<>("id");
        id.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        TableColumn<DataModel, String> num = new TableColumn<>("num");
        num.setCellValueFactory(cellData -> cellData.getValue().numProperty());

        tableView1.getColumns().addAll(id, num);
        tableView1.setItems(data);


        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("app-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


        DBFunctions db = new DBFunctions();
        db.getDbConnection();

        CompletableFuture.supplyAsync(() -> db.fetchDataFromDatabase())
                .thenAccept(result -> data.addAll(result))
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });

    }



    public static void main(String[] args) throws Exception {
        launch();
    }

}