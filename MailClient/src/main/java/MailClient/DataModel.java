package MailClient;

import MailDataModel.Mail;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class DataModel {
    private final StringProperty account = new SimpleStringProperty();

    public  final StringProperty accountProperty() {
        return this.account;
    }

    public  final String getAccount() {
        return this.accountProperty().get();
    }

    public  final void setAccount(final String account) {
        this.accountProperty().set(account);
    }

    private final ObservableList<Mail> mailList = FXCollections.observableArrayList(new ArrayList<>());

    public ObservableList<Mail> getMailList() {return mailList;}

    public void setMailList(ArrayList<Mail> tmp){
        mailList.setAll(tmp);
       // mailList= FXCollections.observableArrayList(tmp);
    }

    private final ObjectProperty<Mail> currentMail = new SimpleObjectProperty<>(null);

    public ObjectProperty<Mail> currentMailProperty() {
        return currentMail ;
    }

    public final Mail getCurrentMail() {
        return currentMailProperty().get();
    }

    public final void setCurrentMail(Mail mail) {
        currentMailProperty().set(mail);
    }




}
