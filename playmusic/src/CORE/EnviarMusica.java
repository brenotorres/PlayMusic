package CORE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;

import javax.sound.sampled.UnsupportedAudioFileException;

import COMUNICACAO.SRserver;

public class EnviarMusica extends Thread {

	private String musica;
	private RepositorioMusica repositorio;
	private SRserver s;
	private InetAddress IP;

	public EnviarMusica(String musica, RepositorioMusica repositorio, SRserver s, InetAddress IP){
		this.musica = musica;
		this.repositorio = repositorio;
		this.IP = IP;
		this.s = s;
	}

	public void run(){
		File arquivo;
		try {
			arquivo = this.repositorio.procurarMusica(musica);
			
			FileInputStream fis = new FileInputStream(arquivo);
			s.criarPacote(fis, IP, 5004);
			
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}
}