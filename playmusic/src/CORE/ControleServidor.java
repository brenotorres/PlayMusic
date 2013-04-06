package CORE;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import COMUNICACAO.SRserver;

public class ControleServidor extends Thread{
	
	private Servidor s;
	private SRserver srs;
	
	public ControleServidor(Servidor s, SRserver srs){
		this.s = s;
		this.srs = srs;
	}
	
	public void run(){
		String receive = "";
		while (true){
			try {
				receive = srs.receberString(5000);
				System.out.println(receive);
			} catch (IOException e) {}
			if (receive.length() > 4 && receive.substring(receive.length()-4, receive.length()).equals(".mp3")){
				try {
					s.enviarMusica(receive, InetAddress.getByName("localhost"));
					receive = "";
				} catch (UnknownHostException e) {}
			}else{
				if (receive.equals("pause")){
					s.pause();
					receive = "";
				}else{
					if (receive.equals("resume")){
						s.resume();
						receive = "";
					}else{
						if (receive.equals("lista")){
							try {
								s.enviarLista(InetAddress.getByName("localhost"));
								receive = "";
							} catch (UnknownHostException e) {}
						}else{
							if (receive.equals("cancel")){
								s.cancel();
								receive = "";
							}
						}
					}
				}
			}
		}
	}

}
