package CORE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class EnviarMusica extends Thread {

	private String musica;
	private String autor;
	private File diretorio;
	private AudioFileFormat audioFileFormat;
	private Map mapa;

	public EnviarMusica(String musica, String autor, File diretorio){
		this.musica = musica;
		this.autor = autor;
		this.diretorio = diretorio;
	}

	public void run(){
		File arquivo = null;
		File arquivos[] = diretorio.listFiles();
		boolean encontrou = false;
		try {
			for (int i = 0; i < arquivos.length && !encontrou; i++){
				audioFileFormat = AudioSystem.getAudioFileFormat(arquivos[i]);
				mapa = ((AudioFileFormat)audioFileFormat).properties();
				if (musica.equals(((String)mapa.get("title")))){
					if (autor.equals(((String)mapa.get("author")))){
						arquivo = arquivos[i];
						encontrou = true;
					}
				}
			}
			FileOutputStream fos = new FileOutputStream(arquivo);
			//Chamar o metodo enviar arquivo da comunicação

		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}