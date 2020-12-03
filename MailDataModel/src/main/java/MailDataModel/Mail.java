package MailDataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Mail implements Serializable {
    private String ID;
    private String sender;
    private ArrayList<String> reciver;
    private String object;
    private String text;
    private String date;

    public Mail(){
        this.ID="";
        this.sender="";
        this.reciver= new ArrayList<>();
        this.object="";
        this.text="";
        this.date="";
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID){
        this.ID = ID;
    }

    public String getSender(){
        return sender;
    }

    public ArrayList<String> getReciver(){
        return reciver;
    }

    public String getReciverString(){
        String tmp="";
        for (String s : reciver) tmp +=  s + " ";
        return tmp;
    }

    public String getObject(){
        return object;
    }

    public String getText(){
        return text;
    }

    public String getDate(){
        return date;
    }

    public void setSender(String sender){
        this.sender = sender;
    }

    public void setReciver(ArrayList<String> reciver){
        this.reciver = reciver;
    }

    public void setNewReciver(String reciver){
        this.reciver= new ArrayList<>();
        this.reciver.add(reciver);
    }
    public void setReciver(String reciver){
        this.reciver.add(reciver);
    }

    public void setObject(String object){
        this.object = object;
    }

    public void setText(String text){
        this.text = text;
    }

    public void setDate(Date date){
        this.date = date.toString();
    }

    public void setDate(String date){
        this.date = date;
    }

    public String toString(){
        return date + "\nS: " + sender + "\nR: " +reciver+ "\nOBJ: "+ object + "\nText: " + text + "\n";
    }

}
