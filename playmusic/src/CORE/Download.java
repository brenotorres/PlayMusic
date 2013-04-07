package CORE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import COMUNICACAO.SRclient;
import COMUNICACAO.SRserver;

public class Download extends Thread{

	private SRclient client;
	private SRserver server;
	private InetAddress IP;
	private String musica;
	private int porta;

	public Download(String musica, SRclient client, SRserver server, InetAddress IP, int porta){
		this.client = client;
		this.server = server;
		this.IP = IP;
		this.porta = porta;
		this.musica = musica;
	}

	public void run(){
		//Chama metodo que envia a string
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			File arq = new File("string");
			arq.delete();
			arq.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(arq));
			out.write(musica);
			FileInputStream fis = new FileInputStream(arq);
			server.criarPacote(clientSocket, fis, IP, porta);
			//Chama metodo para receber o arquivo
			client.receber(clientSocket, clientSocket.getLocalPort(), IP);
		} catch (IOException e) {}
	}
}
