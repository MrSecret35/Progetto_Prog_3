package MailClient;

import MailDataModel.Mail;
import MailDataModel.ServerRequest;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeController {

    DataModel model;

    @FXML
    public Label UserName;
    @FXML
    public ListView<Mail> MailView;

    @FXML
    public void writemail(){
        model.setCurrentMail(new Mail());
        MainClient.write();
    }

    @FXML
    public void quit(){
        MainClient.quit();
    }

    @FXML
    public void deleteMail(){
        ServerRequest req= new ServerRequest(model.getAccount(),3);
        req.setMail(model.getCurrentMail());

        req=ClientFunction.makeRequest(req);

        if(req.getErrNu()!= 0) ClientFunction.printError(req.getErrStr());
        else {
            model.setCurrentMail(new Mail());
            model.setMailList(req.getMailList());
            ClientFunction.printInformation("Mail eliminata con successo");
        }
    }

    public void initModel(DataModel model){
        this.model= model;

        MailView.setItems(model.getMailList());

        MailView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> model.setCurrentMail(newSelection));

        MailView.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(Mail mail, boolean empty){
                super.updateItem(mail, empty);
                if (empty) {
                    setText(null);
                } else {
                    String intro="";
                    if(mail.getSender().equals(model.getAccount())){ intro="->\t";}
                    try {
                        Date d = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(mail.getDate());
                        setText(intro + new SimpleDateFormat("MM/dd/yy HH:mm").format(d) + "\t" + mail.getSender() + "--- " + mail.getObject());
                    } catch (ParseException e) {
                        setText(intro + "\t" + mail.getSender() + "--- " + mail.getObject());
                    }
                }
            }
        });

        UserName.textProperty().bind(model.accountProperty());

        MailView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                MainClient.read();
            }else{
                model.setCurrentMail(MailView.getSelectionModel().getSelectedItem());
            }
        });

        UpdateList threadUpdate= new UpdateList(5);
        threadUpdate.start();
    }

    /*
    * classe utilizzata come Thread per il costante aggiornamento della lista Mail
     */
    private class UpdateList extends Thread{

        private int TimeSec;// tempo in secondi in cui ripetere il controllo delle nuove mail

        public  UpdateList(int delay){
            setDaemon(true);
            TimeSec=delay;
        }

        @Override
        public void run(){
            while (true){
                try{
                    Thread.sleep(TimeSec*1000);
                    if(model.getAccount()!= null){
                        ServerRequest req= new ServerRequest(model.getAccount(),1);
                        req= ClientFunction.makeRequest(req);
                        if(req.getErrNu()==0){
                            //controllo se la dim della nuova lista mail differisce da quella gia' in possesso
                            if(req.getMailList().size()!= model.getMailList().size()){
                                Platform.runLater(() -> ClientFunction.printInformation("Nuova mail arrivata"));
                                model.setCurrentMail(new Mail());
                                ArrayList<Mail> tmp = req.getMailList();
                                Platform.runLater(() -> model.setMailList(tmp));
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
