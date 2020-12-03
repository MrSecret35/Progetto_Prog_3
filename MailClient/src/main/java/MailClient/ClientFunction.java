package MailClient;

import MailDataModel.ServerRequest;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientFunction {

    /*
    * effettua la verifica di un indirizzo mail
    * String tmp:possibile indirizzo mail
     */
    public static boolean verifyMail(String tmp){
        int c = tmp.indexOf("@");
        if(c>=1){
            int p= tmp.indexOf(".", c);
            return p != -1 && p != tmp.length() - 1;
        }
        return false;
    }

    /*
     * metodo che crea un alert Error con str
     * String str: stringa da stampare
     */
    public static void printError(String str){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText(str);
        alert.showAndWait();
    }

    /*
    * metodo che crea un alert Information con str
    * String str: stringa da stampare
     */
    public static void printInformation(String str){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INFORMATION Dialog");
        alert.setContentText(str);
        alert.showAndWait();
    }

    /*
    * funzione che effetua una richiesta(passata come parametro) al sever
    * restituisce una ServerRequest con i dati richiesti o con un errore
    * ServerRequest req: richiesta da effettuare
     */
    public static ServerRequest makeRequest(ServerRequest req){
        try {
            String nomeHost = InetAddress.getLocalHost().getHostName();
            Socket acc = new Socket(nomeHost, 8189);

            ObjectOutputStream outStream = new ObjectOutputStream(acc.getOutputStream());
            ObjectInputStream inStream = new ObjectInputStream(acc.getInputStream());
            try {
                outStream.writeObject(req);
                req = (ServerRequest) inStream.readObject();
            }finally {
                outStream.close();
                inStream.close();
                acc.close();
            }
        } catch (UnknownHostException e) {
            req.setErrNu(1);
            req.setErrStr("Problema con il client\n Si prega di riavviare il dispositivo");
        } catch (IOException e) {
            req.setErrNu(1);
            req.setErrStr("Connessione al Server Instabile ");
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            req.setErrNu(1);
            req.setErrStr("Problema di comunicazione con il Server ");
        }

        return req;
    }
}
