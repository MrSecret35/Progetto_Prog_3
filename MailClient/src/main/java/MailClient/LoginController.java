package MailClient;

import MailDataModel.Mail;
import MailDataModel.ServerRequest;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.ArrayList;

import static MailClient.ClientFunction.*;

public class LoginController {

    DataModel model;

    @FXML
    public TextField mailinput;

    @FXML
    public void login(){
        String user = mailinput.getText();
        if(user!= null && user.length() != 0 && verifyMail(user) ) {

            ServerRequest req= new ServerRequest(user ,1);
            req = makeRequest(req);

            if(req.getErrNu()==0){
                ArrayList<Mail> tmp = req.getMailList();

                model.setMailList(tmp);
                model.setAccount(user);

                MainClient.home();
            }else{printError(req.getErrStr());}
        }else{
            printError("Utente non Valido!");
        }
    }

    public void initModel(DataModel model){
        this.model= model;
    }


}
