package CORE;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.sound.sampled.UnsupportedAudioFileException;

import COMUNICACAO.SRclient;

public class Cliente{

	private RepositorioMusica repositorio;
	private String TED;
	private InetAddress IP;
	private SRclient client;

	public Cliente(String diretorio, InetAddress IP){
		this.IP = IP;
		repositorio = new RepositorioMusica(diretorio);
		client = new SRclient();
		IniciarRepositorio();
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
			//chama metodo pra enviar string solicitando lista
			client.enviarString("lista", IP, 5000);

			//chamar metodo pra receber o arquivo serializado
			File arquivo = client.receber(5002, IP);
			
			FileInputStream fileStream = new FileInputStream(arquivo);   //Vai receber do metodo
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

	public void Download(String musica){
		Thread d = new Download(musica, client, IP);
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