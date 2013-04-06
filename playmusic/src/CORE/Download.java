package CORE;

import java.io.IOException;
import java.net.InetAddress;

import COMUNICACAO.SRclient;

public class Download extends Thread{
    
    private SRclient client;
    InetAddress IP;
	private String musica;

    public Download(String musica, SRclient client, InetAddress IP){
        this.client = client;
        this.IP = IP;
    	this.musica = musica;
    }

    public void run(){
        //Chama metodo que envia a string
        try {
			client.enviarString(this.musica, IP, 5000);
		} catch (IOException e) {}
        
        //Chama metodo para receber o arquivo
        try {
			client.receber(5004, IP);
		} catch (IOException e) {}
    }
}
