package org.example.diplommain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("log4j.configurationFile", "A:/SISP/DiplomMain/main/resources/Log4j2.xml");
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("app-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("SISP");
        stage.setScene(scene);
        stage.show();
    }
}