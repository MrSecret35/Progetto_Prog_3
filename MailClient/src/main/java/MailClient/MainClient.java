package MailClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainClient extends Application {
    static Stage mainStage;

    static Scene homeS, loginS, readS, writeS;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage= primaryStage;
        primaryStage.setTitle("MailCenter");

        FXMLLoader loaderSceneLogin  = new FXMLLoader(getClass().getResource("/loginView.fxml"));
        FXMLLoader loaderSceneHome   = new FXMLLoader(getClass().getResource("/homeView.fxml"));
        FXMLLoader loaderSceneRead   = new FXMLLoader(getClass().getResource("/readView.fxml"));
        FXMLLoader loaderSceneWrite  = new FXMLLoader(getClass().getResource("/writeView.fxml"));

        loginS= new Scene(loaderSceneLogin.load());
        homeS  = new Scene(loaderSceneHome.load());
        readS  = new Scene(loaderSceneRead.load());
        writeS = new Scene(loaderSceneWrite.load());

        LoginController loginController = loaderSceneLogin.getController();
        HomeController homeController   = loaderSceneHome.getController();
        ReadController readController   = loaderSceneRead.getController();
        WriteController writeController = loaderSceneWrite.getController();

        DataModel model= new DataModel();

        loginController.initModel(model);
        homeController.initModel(model);
        readController.initModel(model);
        writeController.initModel(model);

        primaryStage.setScene(loginS);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    /*
    * setta la Scena HomeView come scena principale/mostrata
     */
    public static void home(){
        mainStage.setScene(homeS);
    }

    /*
     * setta la Scena readView come scena principale/mostrata
     */
    public static void read(){mainStage.setScene(readS);}

    /*
     * setta la Scena writeView come scena principale/mostrata
     */
    public static void write(){mainStage.setScene(writeS);}

    /*
     * chiude l'applicazione
     */
    public static void quit(){
        mainStage.close();
    }
}