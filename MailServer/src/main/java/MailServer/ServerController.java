package MailServer;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ServerController {

    ObservableList<String> logList;

    Server serverT;

    @FXML
    private void handleStartBtnAction(){
        //serverT.notify();
        serverT.startBTN();
    }

    @FXML
    public void handleStopBtnAction(){
        serverT.pauseBTN();
    }

    @FXML
    public void handleQuitBtnAction(){
        serverT.quitBTN();
        MainServer.quit();
    }

    @FXML
    private ListView<String> logView;

    /*
    * metodo per l'assegnazione del modello(Lista di log visualizzata)
     */
    public void initModel(ObservableList<String> tmp){
        logList=tmp;

        logView.setItems(logList);

        //creo il Thread del server che gestira' le risorse
        serverT= new Server(tmp);
        serverT.start();

    }


}
