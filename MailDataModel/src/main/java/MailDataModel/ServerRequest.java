package MailDataModel;

import java.io.Serializable;
import java.util.ArrayList;

/*
* classe utilizzata per la creazione di richieste da effettuare al server
* le istanze di questa classe saranno i messaggi tra client e server
 */
public class ServerRequest implements Serializable {
    private String request = "";
    private String user = "";

    private Mail mail;
    private ArrayList<Mail> mailList = new ArrayList<Mail>();

    private int ErrNu=0;//numero di errore Se != da 0 allora c'e stato un errore
    private String ErrStr;//stringa di errore

    public ServerRequest(String username, int NumReq){
        switch (NumReq){
            case 1: request= "RM";break;//ReadMail
            case 2: request= "WM";break;//WriteMail
            case 3: request= "DM";break;//DeleteMail
        }
        this.user=username;
    }

    public String getRequest(){
        return request;
    }

    public String getUser(){
        return user;
    }

    public Mail getMail(){
        return mail;
    }

    public ArrayList<Mail> getMailList(){
        return mailList;
    }

    public int getErrNu(){
        return ErrNu;
    }

    public String getErrStr(){
        return ErrStr;
    }

    public void setErrNu(int errNu){
        ErrNu = errNu;
    }

    public void setErrStr(String errStr){
        ErrStr = errStr;
    }

    public void setMail(Mail mail){
        this.mail = mail;
    }

    public void setMailList(ArrayList<Mail> mailList){
        this.mailList = mailList;
    }
}
