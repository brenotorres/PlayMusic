package CORE;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import COMUNICACAO.SRclient;
import COMUNICACAO.SRserver;
import COMUNICACAO.retorno;

public class ControleServidor extends Thread{

	private Servidor s;
	private SRserver srs;
	private SRclient src;
	private int porta;
	private InetAddress IPdestino;
	private int portaDestino;

	public ControleServidor(Servidor s, SRserver srs, SRclient src, int porta, InetAddress IPdestino, int portaDestino){
		this.s = s;
		this.srs = srs;
		this.src = src;
		this.porta = porta;
		this.IPdestino = IPdestino;
		this.portaDestino = portaDestino;
	}

	public void run(){
		DatagramSocket serverSocket = null;

		retorno receive = null;
		String teste = "";

		try {
			String porta = ""+this.porta;
			File arq = new File("string");
			//arq.delete();
			arq.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(arq));
			out.write(porta);
			out.close();
			FileInputStream fis = new FileInputStream(arq);
			DatagramSocket clientSocket = new DatagramSocket();

			System.out.println("Servidor envia porta que vai ficar escutando");
			srs.criarPacote(clientSocket, fis, IPdestino, portaDestino);
		} catch (IOException e) {}

		while (true){
			try {
				System.out.println("Servidor espera comando, porta: "+this.porta);
				serverSocket = new DatagramSocket(this.porta);
				receive = src.receber(serverSocket, this.porta, InetAddress.getByName("localhost"));
				BufferedReader br = new BufferedReader(new FileReader(receive.getFilenam()));
				teste = br.readLine();
				br.close();
			} catch (IOException e) {}
			if (teste != null){
				if (teste.length() > 4 && teste.substring(teste.length()-4, teste.length()).equals(".mp3")){
					s.enviarMusica(teste, receive.getIPfont(), receive.getPortFont());
					receive = null;
					teste = "";
				}else{
					if (teste.equals("pause")){
						s.pause(receive.getIPfont(), receive.getPortFont());
						receive = null;
					}else{
						if (teste.equals("resume")){
							s.resume(receive.getIPfont(), receive.getPortFont());
							receive = null;
						}else{
							if (teste.equals("lista")){
							s.enviarLista(receive.getIPfont(), receive.getPortFont());
							receive = null;
							}else{
								if (teste.equals("cancel")){
									s.cancel(receive.getIPfont(), receive.getPortFont());
									receive = null;
								}
							}
						}
					}
				}
			}
		}
	}

}
