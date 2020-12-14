package MailClient;

import MailDataModel.Mail;
import MailDataModel.ServerRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


import java.util.ArrayList;

import static MailClient.ClientFunction.*;

public class WriteController {

    DataModel model;

    @FXML
    public Label recivers;

    @FXML
    public TextField reciver;

    @FXML
    public TextField objectM;

    @FXML
    public TextArea text;

    @FXML
    public void addReciver(){
        String rec= reciver.textProperty().getValue().trim();
        if(verifyMail(rec) && rec.length() != 0){
            if(!model.getCurrentMail().getReciver().contains(rec)){
                if(! model.getAccount().equals(rec)){
                    model.getCurrentMail().setReciver(rec);
                    recivers.setText(model.getCurrentMail().getReciverString());
                }else{ printError("Non puoi inviare una mail a te stesso"); }
            }else{ printError("Mail già inserita"); }
        }else{ printError("Mail non valida\t" + rec); }
        reciver.setText("");
    }
    @FXML
    public void viewAllRecivers(){
        String s="";
        for (String tmp : model.getCurrentMail().getReciver()){
            s += tmp + ";\n";
        }
        ClientFunction.printInformation(s);
    }

    @FXML
    public void removeRecivers(){
        recivers.setText("");
        model.getCurrentMail().setReciver(new ArrayList<>());
    }

    @FXML
    public void send(){

        Mail tmp = model.getCurrentMail();
        if(reciver.textProperty().getValue().trim().length() != 0){
            addReciver();
        }

        if(verifyMailCamp(tmp)){

            ServerRequest req = new ServerRequest(model.getAccount(), 2);
            req.setMail(tmp);

            req= makeRequest(req);

            if(req.getErrNu()!=0){ printError(req.getErrStr());
            }else{
                printInformation("Mail inviata con Successo");
                model.setMailList(req.getMailList());
                model.setCurrentMail(new Mail());
                MainClient.home();
            }
        }else{ printError("Campi mancanti!!\n impossibile inviare la mail"); }
    }

    @FXML
    public void home(){
        MainClient.home();
    }

    public void initModel(DataModel model){
        this.model=model;

        model.currentMailProperty().addListener((obs, oldMail, newMail) -> {
            if(newMail != null){
                objectM.setText(newMail.getObject());
                text.setText(newMail.getText());
                if(newMail.getReciver().size()>=1){
                    recivers.setText(newMail.getReciverString());
                    reciver.setText("");
                }else{
                    reciver.setText("");
                    recivers.setText("");
                }
            }else {
                objectM.setText("");
                text.setText("");
                recivers.setText("");
                reciver.setText("");
            }
        });

        objectM.textProperty().addListener((obs, oldObjectM, newObjectM) -> {if(model.getCurrentMail()!=null) model.getCurrentMail().setObject(newObjectM);});
        text.textProperty().addListener((obs, oldtext, newtext) -> {if(model.getCurrentMail()!=null) model.getCurrentMail().setText(newtext);});
    }

    /*
    * verifica dei campi di una mail
    * verifica che i campi necessari all'invio della Mail
    * (destinatario/oggetto/testo)
    * non siano vuoti
     */
    public boolean verifyMailCamp(Mail tmp){
        return tmp.getReciver().size() != 0 &&
                tmp.getObject().length() != 0 &&
                tmp.getText().length() != 0;
    }


}
