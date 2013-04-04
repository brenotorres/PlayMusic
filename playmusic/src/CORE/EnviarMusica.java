package CORE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

public class EnviarMusica extends Thread {

	private String musica;
	//private String autor;
	private RepositorioMusica repositorio;

	public EnviarMusica(String musica, String autor, RepositorioMusica repositorio){
		this.musica = musica;
		//this.autor = autor;
		this.repositorio = repositorio;
	}

	public void run(){
		File arquivo;
		try {
			arquivo = this.repositorio.procurarMusica(musica);
			
			FileOutputStream fos = new FileOutputStream(arquivo);
			//Chamar o metodo enviar arquivo da comunicação
			
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}
}