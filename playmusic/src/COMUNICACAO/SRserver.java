package COMUNICACAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class SRserver {
	//declara��es
	byte[] buffer = new byte[1000];
	byte[] pacote = new byte[1003];
	public static DatagramSocket clientSocket;
	public static SortedSet<Short> semACK = new TreeSet<Short>();
	public static TreeMap <Short,DatagramPacket> pacotes = new TreeMap<Short,DatagramPacket>();
	public static short janela;
	public static short base;
	public static boolean esperar = true;
	final public static int timeout = 500;
	
	
	
	public void criarPacote(FileInputStream fis,InetAddress IPAddress,int port){
		
		try {
			int numerolido = fis.read(buffer);//l� do arquivo (fis) e salva no buffer
			
			while(numerolido != -1){//quando for igual a um quer dizer que n�o tem mais o que ler
				short numeroDeSequencia = 0;
				pacote[0]= ByteUtils.convertToBytes(numeroDeSequencia)[0]; //convers�o de short para byte, leva 2 bytes
				pacote[1]= ByteUtils.convertToBytes(numeroDeSequencia)[1];
				
				for(int i = 2 ; i<buffer.length;i++){
					pacote[i] = buffer[i-2]; //preenche o resto do pacote com os dados.
				}
				
				if(numerolido != 1000){ //o metodo fis.read tambem retorna a quantidade de bytes lidos.
					pacote[1003]=1;
				}else{
					pacote[1003]=0;
				}
				
				
				DatagramPacket enviar = new DatagramPacket(pacote, pacote.length , IPAddress, port);
				
				pacotes.put(numeroDeSequencia, enviar); //salvando
				
				semACK.add(numeroDeSequencia); //salvando
				
				buffer = new byte[1000];//limpando
				pacote = new byte[1003];//limpando
			}
		} catch (IOException e) {}
		
		
	}
	
	public void chamarThread(int ackPort){
		
		
		
		Thread ackThread = new Thread(new ackThread(ackPort, pacotes.lastKey(), timeout));
        ackThread.start();
        ackThread.setPriority(Thread.MAX_PRIORITY-1);
	
		
	}
	
	public void janelaDeslizante(){
		
		base = 1;
		short proximo = 1;
		
		while(esperar){
			
			while(proximo<base+janela){
				if(pacotes.get(proximo)!=null){
					try {
						clientSocket.send(pacotes.get(proximo));
						
						
					} catch (IOException e) {}
					
				}
			}
			
		}
		
		
	}
	
	
	//metodos para as threads terem acesso as informa��es daqui.
	public static synchronized void remSeq(short seq) {
		semACK.remove(seq);
	}
	public static synchronized boolean isSeqEmpty() {
		return semACK.isEmpty();
	}
	public static synchronized short seqFirst() {
		return semACK.first();
	}
	public static synchronized void setBase(short _base) {
		base = _base;
	}
	public static synchronized short getBase() {
		return base;
	}
	public static synchronized void sendPacket(DatagramPacket p) {
		try {
			clientSocket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//fim
	
	public static void main(String args[]) throws Exception {
		
	FileInputStream fis = new FileInputStream("teste1"); 
	InetAddress IPAddress = InetAddress.getByName("localhost");	
	int port = 2000;	
		
		
		
		
		
		
	}
	
	
}