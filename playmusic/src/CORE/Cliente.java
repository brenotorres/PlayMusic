package CORE;

import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.sound.sampled.UnsupportedAudioFileException;

public class Cliente{

	private RepositorioMusica repositorio;
	private String TED;
	private InetAddress IP;

	public Cliente(String diretorio){
		repositorio = new RepositorioMusica(diretorio);
	}

	public void IniciarRepositorio(){
		try {
			repositorio.gerarLista();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Vector<Mp3> solicitarlista(){
		Vector<Mp3> lista = null;
		try {

			//chamar metodo pra receber o arquivo serializado

			FileInputStream fileStream = new FileInputStream("");    //Vai receber do metodo
			ObjectInputStream os = new ObjectInputStream(fileStream);

			lista = (Vector<Mp3>)os.readObject();

			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return lista;
	}

	public void Download(String musica, String autor){
		Thread d = new Download(musica, autor);
		d.start();
	}
	
	public String getTED() {
		return this.TED;
	}

	public void setTED(String TED) {
		this.TED = TED;
	}

	public InetAddress getIP() {
		return this.IP;
	}

	public void setIP(String IP) throws UnknownHostException {
		this.IP = InetAddress.getByName(IP);
	}
}