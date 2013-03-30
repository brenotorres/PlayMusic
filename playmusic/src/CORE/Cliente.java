package CORE;

import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;

public class Cliente{

    public Cliente(){

    }

    public Vector<Mp3> solicitarlista(){
        Vector<Mp3> lista = null;
        try {

            //chamar metodo pra receber o arquivo serializado

            FileInputStream fileStream = new FileInputStream("");    //Vai receber do metodo
            ObjectInputStream os = new ObjectInputStream(fileStream);

            lista = (Vector<Mp3>)os.readObject();

            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void Download(String musica, String autor){
        Thread d = new Download(musica, autor);
        d.start();
    }
}