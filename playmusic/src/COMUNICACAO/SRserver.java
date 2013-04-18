package COMUNICACAO;


import java.io.*;
import java.net.*;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
public class SRserver {


	public static short base;
	public static int DEBUG = 1;
	public volatile static boolean esperar = true;
	public volatile static boolean pause = false;
	public static short janela;
	final public static int timeout = 500;
	public static DatagramSocket clientSocket;
	public static SortedSet<Short> semAckSequencia = new TreeSet<Short>();
	public static TreeMap <Short,DatagramPacket> pacotes = new TreeMap<Short,DatagramPacket>();

	public void criarPacote(DatagramSocket clientSocket, FileInputStream fis,InetAddress IPAddress,int port) throws IOException {

		//basicos
		System.out.println("Sending in progress!");
		short seqNo=0, proximo;
		int ackPort;
		janela = 7;
		esperar = true;
		semAckSequencia.clear();
		pacotes.clear();


		if (port==1){
			ackPort = port + 1; //criando uma porta para ack
		}else{
			ackPort = port - 1;
		}



		byte[] pacote = new byte[1003];
		byte[] buffer = new byte[1000];
		try {
			for (int readNum; (readNum = fis.read(buffer)) != -1;) {


				seqNo++;
				pacote[0] = ByteUtils.convertToBytes(seqNo)[0];
				pacote[1] = ByteUtils.convertToBytes(seqNo)[1];
				for(int j=0;j<readNum;j++) {
					pacote[j+2] = buffer[j];
				}


				if(readNum == 1000) {
					pacote[1002] = 1;
				}
				else {
					pacote[1002] = 0;
				}
				DatagramPacket pck = new DatagramPacket(pacote, pacote.length,IPAddress,port);

				pacotes.put(seqNo,pck);

				semAckSequencia.add(seqNo);

				buffer = new byte[1000];
				pacote = new byte[1003];
			}
		} catch (IOException ex) { }


		Thread ackThread = new Thread(new ackThread(ackPort, pacotes.lastKey(), timeout));
		ackThread.start();
		ackThread.setPriority(Thread.MAX_PRIORITY-1);
		// initial state
		base = 1;
		proximo = 1;

		while(esperar) {
						
			if(semAckSequencia.isEmpty()){
				esperar = false;
			}

			while(proximo<base+janela) {
				//System.out.println(proximo);
				if(pacotes.get(proximo) != null) {

					while(pause){

					}

					clientSocket.send(pacotes.get(proximo));
					
					byte[] temp = new byte[2];
					
					temp[0] = pacotes.get(proximo).getData()[0];
					temp[1] = pacotes.get(proximo).getData()[1];
					System.out.println(ByteUtils.convertShortFromBytes(temp));
					// start a Timer thread for this packet
					new Thread(new timerThread(proximo)).start();
					//if(DEBUG > 0) System.out.println("Active timer threads: "+(Thread.activeCount() - 3));
				}
				proximo++;
			}


		}
		//tratar para re-envio em acks pequenos



		clientSocket.close();
	}

	public String receberString(int port) throws IOException{

		DatagramSocket serverSocket = new DatagramSocket(port);


		byte[] string = new byte[100];
		byte[] buffer = new byte[100];

		DatagramPacket receivePacket = new DatagramPacket(string,string.length);
		serverSocket.receive(receivePacket);
		buffer = receivePacket.getData();

		String nome = new String(buffer,0, receivePacket.getLength());
		//Convert.toString(buffer);


		return nome;
	}



	public static synchronized void remSeq(short seq) {
		semAckSequencia.remove(seq);
	}
	public static synchronized boolean isSeqEmpty() {
		return semAckSequencia.isEmpty();
	}
	public static synchronized short seqFirst() {
		return semAckSequencia.first();
	}
	public static synchronized void setBase(short _base) {
		base = _base;
	}
	public static synchronized short getBase() {
		return base;
	}
	public static void setPause(boolean entrada){
		pause = entrada;
	}
	public static synchronized void sendPacket(DatagramPacket p) {
		try {
			clientSocket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void main(String args[]) throws Exception {

		FileInputStream fis = new FileInputStream("teste3");
		InetAddress IPAddress = InetAddress.getByName("localhost");
		int port = 2000;


		SRserver breno = new SRserver();
		clientSocket = new DatagramSocket();
		//String teste = breno.receberString(port);
		//System.out.println(teste);
		breno.criarPacote(clientSocket,fis, IPAddress, port);


	}
}