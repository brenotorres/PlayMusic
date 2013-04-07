package CORE;

import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.sound.sampled.UnsupportedAudioFileException;

import COMUNICACAO.SRclient;
import COMUNICACAO.SRserver;
import COMUNICACAO.retorno;

public class Cliente{

	private RepositorioMusica repositorio;
	private String TED;
	private InetAddress IP;
	private SRclient client;
	private SRserver server;
	private int porta;

	public Cliente(String diretorio, InetAddress IP){
		this.IP = IP;
		repositorio = new RepositorioMusica(diretorio);
		client = new SRclient();
		server = new SRserver();
		IniciarRepositorio();
		IniciarConexao();
	}
	
	public void IniciarConexao(){
		//Envia pro servidor uma solicitação de porta pra se conectar
		DatagramSocket clientSocket;
		try {
			clientSocket = new DatagramSocket();
			int localPorta = clientSocket.getLocalPort();
			File arq = new File("string");
			//arq.delete();
			arq.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(arq));
			out.write("porta");
			out.close();
			FileInputStream fis = new FileInputStream(arq);
			server.criarPacote(clientSocket, fis, IP, 5000);

			//Recebe a porta qua vai ouvir os comandos do cliente
			clientSocket = new DatagramSocket(localPorta);
			retorno receive = client.receber(clientSocket, localPorta, IP);
			BufferedReader br = new BufferedReader(new FileReader(receive.getFilenam()));
			porta = Integer.parseInt(br.readLine());
		} catch (SocketException e) {} catch (IOException e) {}
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
			File arq = new File("string");
			arq.delete();
			arq.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(arq));
			out.write("lista");
			out.close();
			FileInputStream fis = new FileInputStream(arq);
			//chama metodo pra enviar string solicitando lista
			DatagramSocket clientSocket = new DatagramSocket();
			int localPorta = clientSocket.getLocalPort();
			
			System.out.println("porta 50040 - "+porta);
			
			server.criarPacote(clientSocket, fis, IP, porta);

			//chamar metodo pra receber o arquivo serializado
			clientSocket = new DatagramSocket(localPorta);
			retorno r = client.receber(clientSocket, localPorta, IP);
			File arquivo = r.getFilenam();

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
		Thread d = new Download(musica, client, server, IP, porta);
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