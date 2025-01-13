package com.project.model;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/schermata_iniziale.fxml"));
            
            primaryStage.setTitle("Sistema elettorale");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Errore nel caricamento del file FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}