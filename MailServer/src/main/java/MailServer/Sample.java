package MailServer;

import org.json.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Sample {
    /*
    public static void main(String[] args){
        Mail m2;Mail m1;
        Date g1,g2;


        MailBox mario= new MailBox();
        mario.setAccount("mario@gmail.com");

        m1= new Mail();
        g1= new Date();
        m1.setDate(g1);
        m1.setReciver("mario@gmail.com");
        m1.setObject("obj1");
        m1.setSender("luigi@gmail.com");
        m1.setID("01");
        m1.setText("ciao come va?");

        m2= new Mail();
        g2= new Date();
        m2.setDate(g2);
        m2.setReciver("mario@gmail.com");
        m2.setObject("obj2");
        m2.setSender("wario@gmail.com");
        m2.setID("02");
        m2.setText("ma ti scrivi con luigi S");

        mario.addMail(m1);
        mario.addMail(m2);




        MailBox luigi= new MailBox();
        mario.setAccount("luigi@gmail.com");

        m1= new Mail();
        g1= new Date();
        m1.setDate(g1);
        m1.setReciver("luigi@gmail.com");
        m1.setObject("obj3");
        m1.setSender("mario@gmail.com");
        m1.setID("03");
        m1.setText("Tutto bene Grazie");

        m2= new Mail();
        g2= new Date();
        m2.setDate(g2);
        m2.setReciver("luigi@gmail.com");
        m2.setObject("obj4");
        m2.setSender("wario@gmail.com");
        m2.setID("04");
        m2.setText("facciamo la pace?");

        luigi.addMail(m1);
        luigi.addMail(m2);

        MailBox wario= new MailBox();
        mario.setAccount("wario@gmail.com");

        m1= new Mail();
        g1= new Date();
        m1.setDate(g1);
        m1.setReciver("wario@gmail.com");
        m1.setObject("obj5");
        m1.setSender("luigi@gmail.com");
        m1.setID("05");
        m1.setText("e non saprei");

        m2= new Mail();
        g2= new Date();
        m2.setDate(g2);
        m2.setReciver("wario@gmail.com");
        m2.setObject("obj6");
        m2.setSender("mario@gmail.com");
        m2.setID("06");
        m2.setText("no no non ti preoccupare");

        wario.addMail(m1);
        wario.addMail(m2);


        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("MailBox.dat"), true));
            out.writeObject(mario);
            out.writeObject(luigi);
            out.writeObject(wario);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

     */

    public static void main(String[] args){
        JSONObject file = new JSONObject();
        JSONObject mario = new JSONObject();
        JSONObject luigi = new JSONObject();
        JSONObject wario = new JSONObject();


        try {
            mario.put("account", "mario@gmail.com");
            JSONArray mail1 = new JSONArray();
            JSONObject m1 = new JSONObject();
            JSONObject m2 = new JSONObject();

            m1.put("ID", "1");
            m1.put("sender", "luigi@gmail.com");
            JSONArray rec1 = new JSONArray();
            rec1.put("mario@gmail.com");
            m1.put("reciver", rec1);
            m1.put("object", "object1");
            m1.put("text", "ciao come va?");
            m1.put("date", new Date().toString());

            m2.put("ID", "2");
            m2.put("sender", "wario@gmail.com");
            JSONArray rec2 = new JSONArray();
            rec2.put("mario@gmail.com");
            m2.put("reciver", rec2);
            m2.put("object", "object2");
            m2.put("text", "we ma ti scrivi con luigi?");
            m2.put("date", new Date().toString());

            mail1.put(m1);
            mail1.put(m2);

            mario.put("mail", mail1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            luigi.put("account", "luigi@gmail.com");
            JSONArray mail1 = new JSONArray();
            JSONObject m1 = new JSONObject();
            JSONObject m2 = new JSONObject();

            m1.put("ID", "3");
            m1.put("sender", "mario@gmail.com");
            JSONArray rec1 = new JSONArray();
            rec1.put("luigi@gmail.com");
            m1.put("reciver", rec1);
            m1.put("object", "object12");
            m1.put("text", "tutto bene grazie");
            m1.put("date", new Date().toString());

            m2.put("ID", "4");
            m2.put("sender", "mario@gmail.com");
            JSONArray rec2 = new JSONArray();
            rec2.put("luigi@gmail.com");
            rec2.put("wario@gmail.com");
            m2.put("reciver", rec2);
            m2.put("object", "object21");
            m2.put("text", "Non ti parlo più");
            m2.put("date", new Date().toString());

            mail1.put(m1);
            mail1.put(m2);

            luigi.put("mail", mail1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            wario.put("account", "wario@gmail.com");
            JSONArray mail1 = new JSONArray();
            JSONObject m1 = new JSONObject();
            JSONObject m2 = new JSONObject();
            JSONObject m3 = new JSONObject();

            m1.put("ID", "5");
            m1.put("sender", "luigi@gmail.com");
            JSONArray rec1 = new JSONArray();
            rec1.put("wario@gmail.com");
            m1.put("reciver", rec1);
            m1.put("object", "object154");
            m1.put("text", "ok tranquillo");
            m1.put("date", new Date().toString());

            m2.put("ID", "6");
            m2.put("sender", "mario@gmail.com");
            JSONArray rec2 = new JSONArray();
            rec2.put("wario@gmail.com");
            m2.put("reciver", rec2);
            m2.put("object", "object2987");
            m2.put("text", "dai mi fido di te");
            m2.put("date", new Date().toString());

            m3.put("ID", "4");
            m3.put("sender", "mario@gmail.com");
            JSONArray rec3 = new JSONArray();
            rec3.put("luigi@gmail.com");
            rec3.put("wario@gmail.com");
            m3.put("reciver", rec3);
            m3.put("object", "object21");
            m3.put("text", "Non ti parlo più");
            m3.put("date", new Date().toString());

            mail1.put(m1);
            mail1.put(m2);
            mail1.put(m3);

            wario.put("mail", mail1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            file.put("mario@gmail.com", mario);
            file.put("luigi@gmail.com", luigi);
            file.put("wario@gmail.com", wario);
            file.put("ID",6);
            FileWriter fileVero = new FileWriter("MailBox.json");

            fileVero.write(file.toString());
            fileVero.flush();

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }


    }
}
