package CORE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;

import javax.sound.sampled.UnsupportedAudioFileException;

import COMUNICACAO.SRserver;

public class EnviarLista extends Thread{

	private RepositorioMusica repositorio;
	private SRserver s;
	private InetAddress IP;
	private int porta;

	public EnviarLista(RepositorioMusica repositorio, SRserver s, InetAddress IP, int porta){
		this.repositorio = repositorio;
		this.s = s;
		this.IP = IP;
		this.porta = porta;
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

			DatagramSocket clientSocket = new DatagramSocket();
			FileInputStream fis = new FileInputStream(arquivo);
			//chamar metodo da comunicação pra enviar arquivo serializado
			s.criarPacote(clientSocket, fis, IP, porta);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e){
			e.printStackTrace();
		}
	}
}