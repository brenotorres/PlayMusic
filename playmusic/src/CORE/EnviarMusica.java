package CORE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.sound.sampled.UnsupportedAudioFileException;

import COMUNICACAO.SRserver;

public class EnviarMusica extends Thread {

	private String musica;
	private RepositorioMusica repositorio;
	private SRserver s;
	private InetAddress IP;
	private int porta;

	public EnviarMusica(String musica, RepositorioMusica repositorio, SRserver s, InetAddress IP, int porta){
		this.musica = musica;
		this.repositorio = repositorio;
		this.IP = IP;
		this.porta = porta;
		this.s = s;
	}

	public void run(){
		File arquivo;

		try {
			arquivo = this.repositorio.procurarMusica(musica);
			
			DatagramSocket clientSocket = new DatagramSocket();
			FileInputStream fis = new FileInputStream(arquivo);
			s.criarPacote(clientSocket, fis, IP, porta);
			
		} catch (UnsupportedAudioFileException e) {}
		catch (IOException e) {}
	}
}