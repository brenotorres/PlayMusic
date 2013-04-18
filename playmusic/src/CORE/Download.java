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
import COMUNICACAO.retorno;

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
		
		System.out.println("download");
	}

	public void run(){
		retorno receive = null;
		int portaLocal;
		//Chama metodo que envia a string
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			portaLocal = clientSocket.getLocalPort();
			File arq = new File("string");
			arq.delete();
			arq.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(arq));
			out.write(musica);
			out.close();
			FileInputStream fis = new FileInputStream(arq);
			//synchronized (this) {
			server.criarPacote(clientSocket, fis, IP, porta);
			//Chama metodo para receber o arquivo
			DatagramSocket serverSocket = new DatagramSocket(portaLocal);
			System.out.println("MANTEGOSO");
			receive = client.receber(serverSocket, portaLocal, IP);
			
			//}
		} catch (IOException e) {}
	}
}
