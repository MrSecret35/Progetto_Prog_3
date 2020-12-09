package MailServer;

import MailDataModel.Mail;
import MailDataModel.ServerRequest;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
*classe chhe riceverà le richieste dei client e smistera' in un threadPool di ServerTask
 */
public class Server extends Thread {
    static boolean serverFlag = false;
    static boolean serverInitFlag = true;

    ObservableList<String> logList;

    private static final int NUM_THREAD = 10;

    ServerSocket serverSocket;

    public Server(ObservableList<String> logList){
        super();
        this.logList=logList;
    }

    public void run(){
        //creo il pool
        wrtiteLog(new Date().toString() + "\tServer Ready");
        ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREAD);
        try {
            serverSocket = new ServerSocket(8189);
            serverSocket.setSoTimeout(8000);

            ReadWriteLock rwl = new ReentrantReadWriteLock();
            Lock rl = rwl.readLock();
            Lock wl = rwl.writeLock();


            while(serverInitFlag){

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while(serverFlag){
                    try {
                        Socket incoming= serverSocket.accept();

                        Runnable task = new ServerTask(incoming, rl, wl, logList);
                        threadPool.execute(task);
                    } catch(SocketTimeoutException ignored){}
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            threadPool.shutdown();
        }

    }

    /*
    * operazione di Starting del Server
    */
    public void startBTN(){
        wrtiteLog(new Date().toString() + "\tServer Starting...");
        serverFlag=true;
    }

    /*
     * operazione di blocco delle funzionalita' del Server
     */
    public void pauseBTN(){
        wrtiteLog(new Date().toString() + "\tServer Stop");
        serverFlag= false;
    }

    /*
     * operazione di chiusura del server
     */
    public void quitBTN(){
        pauseBTN();
        serverInitFlag=false;
        wrtiteLog(new Date().toString() + "\tServer Quit...");

    }

    /*
     *scrive sul Log del Server la stringa str
     * String str: stringa da scrivere sul Log
     */
    private void wrtiteLog(String str){
        Platform.runLater(() -> logList.add(str));
    }

}

/*
* classe utilizata da Server per gestire le varie Richieste
 */
class ServerTask implements Runnable{
    ObservableList<String> logList;

    private final Socket incoming;

    private String user;


    Lock readLock , writelock;

    public ServerTask(Socket in,Lock rl, Lock wl, ObservableList<String> logList) {
        incoming = in;
        this.readLock=rl;
        this.writelock=wl;
        this.logList=logList;
    }

    ServerRequest req;

    @Override
    public void run(){
        try {
            ObjectInputStream inObjStream = null;
            ObjectOutputStream outStream = null;

            try {
                outStream = new ObjectOutputStream(incoming.getOutputStream());
                inObjStream = new ObjectInputStream(incoming.getInputStream());

                try {
                    req = ((ServerRequest) inObjStream.readObject());
                    user = req.getUser();
                    wrtiteLog((new Date()).toString() + "\t" + user + "\t" + "Inizio comunicazione");

                    //"CR" -> ClientRegistration();
                    switch (req.getRequest()) {
                        case "RM" -> readMail();
                        case "WM" -> writeMail();
                        case "DM" -> deleteMail();
                    }
                    try {
                        outStream.writeObject(req);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (SocketException ignored) {
                }

            } finally {
                wrtiteLog((new Date()).toString() + "\t" + user + "\tchiusura comunicazione");
                if (inObjStream != null) inObjStream.close();
                if (outStream != null) outStream.close();
                incoming.close();
            }
        }catch (IOException e) {e.printStackTrace();}
    }

    /*
     *metodo utilizzato per la lettura delle mail di un determinato utente
     */
    private void readMail(){
        wrtiteLog((new Date()).toString() + "\t" + user +"\t" + "Lettura mail");
        try {
            readLock.lock();

            JSONObject jsonObject = new JSONObject(new JSONTokener(new FileInputStream("MailBox.json")));
            if(jsonObject.has(user)){
                //utente trovato
                JSONArray mail = jsonObject.getJSONObject(user).getJSONArray("mail");
                req.setMailList(generateArrayList(mail));
            }else{
                req.setErrNu(1);
                req.setErrStr("Utente non trovato");
            }
        } catch (FileNotFoundException | JSONException e) {
            req.setErrNu(1);
            req.setErrStr("Impossibile accederer ai Dati\nServer momentaneamente indisponibile");
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }

    }

    /*
     *metodo utilizzato per la scrittura di una mail contenuta nella richiesta : req.getMail
     */
    private void writeMail(){
        wrtiteLog((new Date()).toString() + "\t" + user +"\t" + "Scrittura nuova mail");
        try {
            writelock.lock();//ottengo il lock per scrivere

            Mail tmp= req.getMail();
            JSONObject jsonObject = new JSONObject(new JSONTokener(new FileInputStream("MailBox.json")));

            int ID = jsonObject.getInt("ID") +1;//genero il nuovo ID
            tmp.setDate(new Date());
            tmp.setID(Integer.toString(ID));
            tmp.setSender(user);
            JSONObject m1 = createJsonMail(tmp);

            //controllo se i destinatari esistono
            boolean exist= true;String inuser="";
            for(String dest: tmp.getReciver()){
                if(!jsonObject.has(dest)) {
                    exist=false;
                    inuser+= "\t"+dest;
                }
            }

            if(exist){
                //invio la mail a tutti i destinatari
                for(String dest: tmp.getReciver()){
                    try {
                        JSONArray mail = jsonObject.getJSONObject(dest).getJSONArray("mail");
                        mail.put(m1);
                    }catch (JSONException e){
                        req.setErrNu(1);
                        req.setErrStr("Impossibile trovare l'utente desiderato\t" + dest);
                        tmp.getReciver().remove(dest);
                        m1=createJsonMail(tmp);
                    }
                }

                //scrivo la mail nel registro mail del mittente
                try {
                    if(tmp.getReciver().size() != 0){
                        JSONArray mail = jsonObject.getJSONObject(user).getJSONArray("mail");
                        mail.put(m1);
                        req.setMailList(generateArrayList(mail));
                    }
                }catch (JSONException e){
                    req.setErrNu(1);
                    req.setErrStr("Impossibile trovare l'utente desiderato\t" + user);
                }


                //agggiorno il nuovo ID
                jsonObject.put("ID",ID+1);

                FileWriter fileVero = new FileWriter("MailBox.json");

                fileVero.write(jsonObject.toString());
                fileVero.flush();
                fileVero.close();
            }else{
                req.setErrNu(1);
                req.setErrStr("Impossibile trovare gli utenti:\t"+ inuser);
            }

        } catch (IOException | JSONException e) {
            req.setErrNu(1);
            req.setErrStr("Impossibile accederer ai Dati\t");
            e.printStackTrace();
        } finally {
            writelock.unlock();
        }
    }

    /*
     *metodo utilizzato per la cancellazione della mail contenuta nella richiesta
     */
    private void deleteMail(){
        wrtiteLog((new Date()).toString() + "\t" + user +"\t" + "Eliminazione mail");
        try{
            writelock.lock();//ottengo il lock di scrittura
            Mail tmpMail = req.getMail();

            JSONObject jsonObject = new JSONObject(new JSONTokener(new FileInputStream("MailBox.json")));
            JSONArray mail = jsonObject.getJSONObject(user).getJSONArray("mail");

            int index= 0;
            for(int i= 0; i< mail.length(); i++ ){
                if(mail.getJSONObject(i).getString("ID").equals(tmpMail.getID())) index = i;
            }
            mail.remove(index);

            req.setMailList(generateArrayList(mail));
            FileWriter fileVero = new FileWriter("MailBox.json");

            fileVero.write(jsonObject.toString());
            fileVero.flush();
            fileVero.close();
        } catch (IOException | JSONException e) {
            req.setErrNu(1);
            req.setErrStr("Impossibile accederer ai Dati");
            e.printStackTrace();
        }finally {
            writelock.unlock();
        }
    }

    /*
     *converte gli elementi di un jsonArray in un arrayList java contenete della Mail
     */
    public ArrayList<Mail> generateArrayList(JSONArray mailJ) throws JSONException{
        ArrayList<Mail> mail= new ArrayList<>();
        for (int i = 0; i < mailJ.length(); i++){

            JSONObject tmp = mailJ.getJSONObject(i);
            Mail mailtmp = generateMail(tmp);

            mail.add(mailtmp);
        }
        return mail;
    }

    /*
     *genera l'oggetto Mail corrispondente all'oggetto Json passatto come argomento
     * JSONObject tmp:  oggetto da convertire in Mail
     */
    public Mail generateMail(JSONObject tmp){

        Mail mailtmp = new Mail();
        try{
            mailtmp.setID(tmp.getString("ID"));
            mailtmp.setSender(tmp.getString("sender"));

            JSONArray reciver = tmp.getJSONArray("reciver");
            ArrayList<String> rec= new ArrayList<>();
            for(int j=0; j< reciver.length(); j++){
                rec.add(reciver.getString(j));
            }
            mailtmp.setReciver(rec);
            mailtmp.setDate(tmp.getString("date"));
            mailtmp.setObject(tmp.getString("object"));
            mailtmp.setText(tmp.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mailtmp;
    }

    /*
     *scrive sul Log del Server la stringa str
     * String str: stringa da scrivere sul Log
     */
    private void wrtiteLog(String str){
        Platform.runLater(() -> logList.add(str));
    }

    /*
     *genera l'oggetto Json corrispondente all'oggetto Mail passato come parametro
     * Mail tmp:  oggetto da convertire in Json
     */
    private JSONObject createJsonMail(Mail tmp) throws JSONException{
        JSONObject m1 = new JSONObject();

        m1.put("ID", tmp.getID());
        m1.put("sender", tmp.getSender());
        JSONArray rec1 = new JSONArray();
        for (String dest: tmp.getReciver())
            rec1.put(dest);
        m1.put("reciver", rec1);
        m1.put("object", tmp.getObject());
        m1.put("text", tmp.getText());
        m1.put("date", tmp.getDate());

        return m1;

    }

}
