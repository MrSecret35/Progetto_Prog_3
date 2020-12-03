package MailServer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

public class MainServer extends Application{

    static Stage main;
    Scene serverScene;


    @Override
    public void start(Stage primaryStage) throws Exception{
        main =primaryStage;

        primaryStage.setTitle("Mail Server");

        //crete and setting scene and controller
        FXMLLoader loaderScene = new FXMLLoader(getClass().getResource("/serverView.fxml"));
        serverScene = new Scene(loaderScene.load());
        FXMLLoader.load(getClass().getResource("/serverView.fxml"));
        ServerController serverController = loaderScene.getController();

        //dichiarazione del modello mostrato dalla view (Stringhe di Log)
        ObservableList<String> logView = FXCollections.observableArrayList(new ArrayList<>());
        serverController.initModel(logView);

        primaryStage.setScene(serverScene);

        primaryStage.show();
        main.setOnCloseRequest(t -> serverController.handleQuitBtnAction());

    }


    public static void main(String[] args) {
        launch(args);
    }



    public static void quit(){
        main.close();
    }



}


