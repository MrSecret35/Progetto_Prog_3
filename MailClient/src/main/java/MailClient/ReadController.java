package MailClient;

import MailDataModel.Mail;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class ReadController {

    @FXML
    public Label date;

    @FXML
    public TextField reciver;

    DataModel model;

    @FXML
    public TextArea text;

    @FXML
    public TextField objectM;
    @FXML
    public TextField sender;

    /*
    * metodo per rispondere al mittente una mail
     */
    @FXML
    public void reply(){
        Mail tmp = new Mail();
        if(!model.getCurrentMail().getSender().equals(model.getAccount())){
            tmp.setNewReciver(model.getCurrentMail().getSender());//set del mittente come destinatario
            tmp.setText("Mail di Risposta:\n");
            model.setCurrentMail(tmp);

            MainClient.write();
        }else ClientFunction.printError("Non puoi rispondere ad una tua mail");

    }

    /*
     * metodo per rispondere al mittente e agli altri destinatari della mail
     */
    @FXML
    public void reply_all(){
        Mail tmp = new Mail();
        tmp.setReciver(new ArrayList<>());

        if (!model.getCurrentMail().getSender().equals(model.getAccount())) tmp.setReciver(model.getCurrentMail().getSender());
        for(String dest: model.getCurrentMail().getReciver()) {
            if (!dest.equals(model.getAccount()))
                tmp.setReciver(dest);
        }
        tmp.setText("Mail di Risposta:\n");
        model.setCurrentMail(tmp);
        MainClient.write();
    }

    /*
     * metodo per inoltrare una mail
     */
    @FXML
    public void forward(){
        Mail tmp = new Mail();

        tmp.setObject(model.getCurrentMail().getObject());
        tmp.setText("Mail Ricevuta da : " + model.getCurrentMail().getSender()
                + "\n" + model.getCurrentMail().getText());
        tmp.setReciver("");
        model.setCurrentMail(tmp);

        MainClient.write();
    }

    @FXML
    public void home(){
        MainClient.home();
    }

    public void initModel(DataModel model){
        this.model=model;

        model.currentMailProperty().addListener((obs, oldMail, newMail) -> {
            if(newMail!= null){
                sender.setText(newMail.getSender());
                objectM.setText(newMail.getObject());
                text.setText(newMail.getText());
                date.setText(newMail.getDate());
                String s = "";
                for (String tmp: newMail.getReciver()) {
                    s+= " " +  tmp;
                }
                reciver.setText(s);
            }
        });

        //setEditable(false) essendo una lettura non e' possibile modificare gli attributi

        sender.setEditable(false);
        objectM.setEditable(false);
        text.setEditable(false);
        //reciver.setEditable(false);


    }


}
