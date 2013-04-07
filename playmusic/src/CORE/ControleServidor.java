package CORE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import COMUNICACAO.SRclient;
import COMUNICACAO.SRserver;
import COMUNICACAO.retorno;

public class ControleServidor extends Thread{

	private Servidor s;
	private SRserver srs;
	private SRclient src;
	private int porta;

	public ControleServidor(Servidor s, SRserver srs, SRclient src, int porta){
		this.s = s;
		this.srs = srs;
		this.src = src;
		this.porta = porta;
	}

	public void run(){
		DatagramSocket serverSocket = null;
		retorno receive;
		String teste = "";
		while (true){
			try {
				serverSocket = new DatagramSocket(porta);
				
				System.out.println(this.porta);
				
				receive = src.receber(serverSocket, porta, InetAddress.getByName("localhost"));				
				BufferedReader br = new BufferedReader(new FileReader(receive.getFilenam()));
				teste = br.readLine();
				br.close();
			} catch (IOException e) {}
			if (teste.length() > 4 && teste.substring(teste.length()-4, teste.length()).equals(".mp3")){
				s.enviarMusica(teste, serverSocket.getInetAddress(), serverSocket.getPort());
				teste = "";
			}else{
				if (teste.equals("pause")){
					s.pause(serverSocket.getInetAddress(), serverSocket.getPort());
					teste = "";
				}else{
					if (teste.equals("resume")){
						s.resume(serverSocket.getInetAddress(), serverSocket.getPort());
						teste = "";
					}else{
						if (teste.equals("lista")){System.out.println("listaaa");
						s.enviarLista(serverSocket.getInetAddress(), serverSocket.getPort());
						teste = "";
						}else{
							if (teste.equals("cancel")){
								s.cancel(serverSocket.getInetAddress(), serverSocket.getPort());
								teste = "";
							}
						}
					}
				}
			}
		}
	}

}
