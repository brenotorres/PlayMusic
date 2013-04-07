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

import javax.sound.sampled.UnsupportedAudioFileException;

import COMUNICACAO.SRserver;
import COMUNICACAO.SRclient;
import COMUNICACAO.retorno;

public class Servidor extends Thread{

	private RepositorioMusica musicas;
	private SRserver server;
	private SRclient client;

	public Servidor(String diretorio){
		this.musicas = new RepositorioMusica(diretorio);
		server = new SRserver();
		client = new SRclient();
		IniciarRepositorio();
		this.start();
	}

	public void run(){
		retorno receive;
		String teste = "";
		while(true){
			try {
				DatagramSocket serverSocket = new DatagramSocket(5000);
				receive = client.receber(serverSocket, 5000, InetAddress.getByName("localhost"));
				BufferedReader br = new BufferedReader(new FileReader(receive.getFilenam()));
				teste = br.readLine();
				if (teste.equals("porta")){
					//Tem que checar portas UDP disponiveis
					String porta = "50040";
					File arq = new File("string");
					//arq.delete();
					arq.createNewFile();
					BufferedWriter out = new BufferedWriter(new FileWriter(arq));
					out.write(porta);
					out.close();
					FileInputStream fis = new FileInputStream(arq);
					DatagramSocket clientSocket = new DatagramSocket();


					System.out.println(receive.getPortFont()+"      "+receive.getIPfont());


					server.criarPacote(clientSocket, fis, receive.getIPfont(), receive.getPortFont());
					waitClient(50040);
				}
			} catch (IOException e) {}
		}
	}

	public void IniciarRepositorio(){
		try {
			musicas.gerarLista();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void waitClient(int porta){
		Thread controle = new ControleServidor(this, server, client, porta);
		controle.start();
	}

	protected void enviarLista(InetAddress IP, int porta){
		Thread lista = new EnviarLista(musicas, server, IP, porta);
		lista.start();
	}

	protected void enviarMusica(String musica, InetAddress IP, int porta){
		Thread music = new EnviarMusica(musica, musicas, server, IP, porta);
		music.start();
	}

	protected void pause(InetAddress IP, int porta){
		//Chamar função pausa transferencia
	}

	protected void resume(InetAddress IP, int porta){
		//Chamar função reinicia transferencia
	}

	protected void cancel(InetAddress IP, int porta){
		//Chamar função que cancela transferencia
	}

	protected void listaUser(){

	}
}