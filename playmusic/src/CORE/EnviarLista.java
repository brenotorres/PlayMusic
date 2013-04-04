package CORE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.sound.sampled.UnsupportedAudioFileException;

public class EnviarLista extends Thread{

	private RepositorioMusica repositorio;

	public EnviarLista(RepositorioMusica repositorio){
		this.repositorio = repositorio;
	}

	public void run(){
		try {
			Vector<Mp3> lista = this.repositorio.gerarLista();

			File arquivo = new File("lista.txt");   
			if (!arquivo.exists()) { 
				arquivo.createNewFile();
			}
			OutputStream out = new FileOutputStream(arquivo);

			ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);  

			objectOutputStream.writeObject(lista);
			objectOutputStream.close();  
			out.close();

			FileOutputStream fos = new FileOutputStream(arquivo);
			//chamar metodo da comunicação pra enviar arquivo serializado

		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e){
			e.printStackTrace();
		}
	}
}