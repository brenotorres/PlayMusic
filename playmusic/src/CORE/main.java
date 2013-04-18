package CORE;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import COMUNICACAO.SRclient;

public class main {

	public static void main(String[] args){
		BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
		interfacePlayer i = new PlayerMusic();

		Cliente c = null;
		try {
			c = new Cliente("H:/Recebidos", InetAddress.getByName("localhost"));
		} catch (UnknownHostException e) {}

		Vector<Mp3> lista = c.solicitarlista();
		System.out.println(lista.get(1).getNome());

		c.Download("manguetown.mp3");
		//		c.Download("03 - try.mp3");


		//		try{
		//			File file = new File("C:/Users/Toinho/Desktop/My Shared Folder/she wolf.mp3");
		//			System.out.println(file.getName());
		//			i.play(file);
		//			String f;
		//			System.out.println(i.get_album()+" - "+i.get_author()+" - "+i.get_title());
		//			while (true){
		//				f = b.readLine();
		//				System.out.println(i.get_volumeAtual());
		//				f = b.readLine();
		//				System.out.println(i.get_minimo());
		//				i.play(file);
		//
		//				System.out.println(f);
		//			}
		//		}catch (IOException e){
		//			System.out.println("Deu merda aqui vei");
		//		}
	}
}