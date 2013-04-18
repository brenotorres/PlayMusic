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
		retorno receive = null;
		String teste = "";
		while(true){
			try {
				DatagramSocket serverSocket = new DatagramSocket(5000);

				System.out.println("Servidor espera conexão");
				receive = client.receber(serverSocket, 5000, InetAddress.getByName("localhost"));
				System.out.println("      --------------"+receive.getFilenam().length());
				BufferedReader br = new BufferedReader(new FileReader(receive.getFilenam()));
				teste = br.readLine();
				System.out.println(teste);
				if (teste != null && teste.equals("porta")){
					//Tem que checar portas UDP disponiveis
					System.out.println(receive.getIPfont()+" "+receive.getPortFont());
					waitClient(50040, receive.getIPfont(), receive.getPortFont());
				}
				receive = null;
				teste = "";
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

	public void waitClient(int porta, InetAddress IPdestino, int portaDestino){
		Thread controle = new ControleServidor(this, server, client, porta, IPdestino, portaDestino);
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