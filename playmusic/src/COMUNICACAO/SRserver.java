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
	//declarações
	byte[] buffer = new byte[1000];
	byte[] pacote = new byte[1003];
	public static int DEBUG = 1;
	public static DatagramSocket clientSocket;
	public static SortedSet<Short> semACK = new TreeSet<Short>();
	public static TreeMap <Short,DatagramPacket> pacotes = new TreeMap<Short,DatagramPacket>();
	public static short janela = 7;
	public static short base, proximo;
	public static boolean esperar = true;
	final public static int timeout = 500;
	
	
	
	public void criarPacote(FileInputStream fis,InetAddress IPAddress,int port) throws IOException{
		int ackPort;
		
		if (port==1) ackPort = port++; //criando uma porta para ack 
		else ackPort = port--;
		
		
		try {

			
			clientSocket = new DatagramSocket();
			short numeroDeSequencia = 0;
			//int numerolido = fis.read(buffer);//lê do arquivo (fis) e salva no buffer
			
			for(int numerolido; (numerolido = fis.read(buffer)) != -1;){//quando for igual a um quer dizer que não tem mais o que ler
				numeroDeSequencia++;
				pacote[0]= ByteUtils.convertToBytes(numeroDeSequencia)[0]; //conversão de short para byte, leva 2 bytes
				pacote[1]= ByteUtils.convertToBytes(numeroDeSequencia)[1];
				
				for(int i = 0 ; i<numerolido;i++){
					pacote[i+2] = buffer[i]; //preenche o resto do pacote com os dados.
				}
				
				if(numerolido != 1000){ //o metodo fis.read tambem retorna a quantidade de bytes lidos.
					pacote[1002]=1;
				}else{
					pacote[1002]=0;//esse espaço vai ser reservado para saber se é o ultimo pacote ou não.
				}
				
				
				DatagramPacket enviar = new DatagramPacket(pacote, pacote.length , IPAddress, port);
				
				
				
				pacotes.put(numeroDeSequencia, enviar); //salvando
				
				semACK.add(numeroDeSequencia); //salvando
				
				buffer = new byte[1000];//limpando
				pacote = new byte[1003];//limpando
				
			}
			
			
		} catch (IOException e) {}
		
				//Lançar ack thread
				Thread ackThread = new Thread(new ackThread(ackPort, pacotes.lastKey(), timeout));
		        ackThread.start();
		        ackThread.setPriority(Thread.MAX_PRIORITY-1);
		        //ack thread
		        
		        base = 1;
		        proximo = 1;
		       
				
				//enviar pacote, e iniciar a Thread de tempo
				while(esperar){
					System.out.println("");
					while(proximo<base+janela){
						System.out.println("numero do proximo: " +proximo);
						if(proximo == 5){
							System.out.println("teste");
						}
						if(pacotes.get(proximo)!=null){
							
								if(proximo == 4){
									
								}else{
									
								clientSocket.send(pacotes.get(proximo));
								}
								
								
								new Thread(new timerThread(proximo)).start();
								
						}
						proximo++;
					}	
				}
				//File f = new File("teste1");
				System.out.println("parou de executar");
				clientSocket.close();
	}
					
	
	//metodos para as threads terem acesso as informações daqui.
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
	
	
	SRserver breno = new SRserver();
		
	breno.criarPacote(fis, IPAddress, port);	
		
		
	}
	
	
}
