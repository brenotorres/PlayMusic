package CORE;

import java.io.IOException;
import java.net.InetAddress;

import javax.sound.sampled.UnsupportedAudioFileException;

import COMUNICACAO.SRserver;

public class Servidor {

	private RepositorioMusica musicas;
	private SRserver server;

	public Servidor(String diretorio){
		this.musicas = new RepositorioMusica(diretorio);
		server = new SRserver();
		IniciarRepositorio();
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
	
	public void onServer(){
		Thread controle = new ControleServidor(this, server);
		controle.start();
	}

	protected void enviarLista(InetAddress IP){
		Thread lista = new EnviarLista(musicas, server, IP);
		lista.start();
	}

	protected void enviarMusica(String musica, InetAddress IP){
		Thread music = new EnviarMusica(musica, musicas, server, IP);
		music.start();
	}
	
	protected void pause(){
		//Chamar função pausa transferencia
	}
	
	protected void resume(){
		//Chamar função reinicia transferencia
	}
	
	protected void cancel(){
		//Chamar função que cancela transferencia
	}
	
	protected void listaUser(){
		
	}
}