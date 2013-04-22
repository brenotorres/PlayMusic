package COMUNICACAO;


import java.io.*;
import java.net.*;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
public class SRserver implements Serializable{


	public static int base;
	public static int DEBUG = 1;
	public volatile static boolean esperar = true;
	public volatile static boolean pause = false;
	public static short janela;
	final public static int timeout = 500;
	public static DatagramSocket clientSocket;
	public static SortedSet<Integer> semAckSequencia = new TreeSet<Integer>();
	public static TreeMap <Integer,DatagramPacket> pacotes = new TreeMap<Integer,DatagramPacket>();

	public void criarPacote(DatagramSocket clientSocket, FileInputStream fis,InetAddress IPAddress,int port) throws IOException {

		//basicos
		clientSocket.setSendBufferSize(100000000);
		clientSocket.setSoTimeout(0);

		System.out.println("Sending in progress!");
		int seqNo=0, proximo; //mudei
		int ackPort;
		janela = 50;
		esperar = true;
		semAckSequencia.clear();
		pacotes.clear();


		// if (port==1){
		// ackPort = port + 1; //criando uma porta para ack
		// }else{
		// ackPort = port - 1;
		// }

		byte[] pacote = new byte[1009];
		byte[] buffer = new byte[1000];

		DatagramSocket serverSocket = new DatagramSocket();

		int portaParaAck = serverSocket.getLocalPort();

		System.out.println(portaParaAck);


		//pacote[0] = ByteUtils.convertToBytes(seqNo)[0];
		//pacote[1] = ByteUtils.convertToBytes(seqNo)[1];

		//pacote[2] = ByteUtils.convertToBytes(portaParaAck)[0];
		//pacote[3] = ByteUtils.convertToBytes(portaParaAck)[1];




		// DatagramPacket pckACK = new DatagramPacket(pacote, pacote.length,IPAddress,port);
		//
		// pacotes.put(seqNo,pckACK);
		//
		// semAckSequencia.add(seqNo);
		//
		// pacote = new byte[1003];
		// buffer = new byte[1000];

		try {
			for (int readNum; (readNum = fis.read(buffer)) != -1;) {


				seqNo++;

			
				
				// Numero de Sequencia
				pacote[0] = Convert.toByta(seqNo)[0];
				pacote[1] = Convert.toByta(seqNo)[1];
				pacote[2] = Convert.toByta(seqNo)[2];
				pacote[3] = Convert.toByta(seqNo)[3];
				// Numero de Sequencia

				// Numero de porta
				pacote[1005] = Convert.toByta(portaParaAck)[0];
				pacote[1006] = Convert.toByta(portaParaAck)[1];
				pacote[1007] = Convert.toByta(portaParaAck)[2];
				pacote[1008] = Convert.toByta(portaParaAck)[3];
				//

				for(int j=0;j<readNum;j++) {
					pacote[j+4] = buffer[j];
				}






				if(readNum == 1000) {
					pacote[1004] = 1;
				}
				else {
					pacote[1004] = 0;
				}
				DatagramPacket pck = new DatagramPacket(pacote, pacote.length,IPAddress,port);

				pacotes.put(seqNo,pck);

				semAckSequencia.add(seqNo);

				buffer = new byte[1000];
				pacote = new byte[1009];
			}
		} catch (IOException ex) { }


		Thread ackThread = new Thread(new ackThread(serverSocket, pacotes.lastKey(), timeout));
		ackThread.start();
		ackThread.setPriority(Thread.MAX_PRIORITY-1);
		// initial state
		base = 1;
		proximo = 1;

		while(esperar) {

			if(semAckSequencia.isEmpty()){
				esperar = false;
			}
			//System.out.println("será?");
			while(proximo<base+janela) {
				//System.out.println(proximo);
				if(pacotes.get(proximo) != null) {

					while(pause){


					}

					clientSocket.send(pacotes.get(proximo));

					// start a Timer thread for this packet
					new Thread(new timerThread(proximo, clientSocket)).start();

					byte[] temp = new byte[4];

					temp[0] = pacotes.get(proximo).getData()[0];
					temp[1] = pacotes.get(proximo).getData()[1];
					temp[2] = pacotes.get(proximo).getData()[2];
					temp[3] = pacotes.get(proximo).getData()[3];
					System.out.println(Convert.toInt(temp));
					//if(DEBUG > 0) System.out.println("Active timer threads: "+(Thread.activeCount() - 3));
				}
				proximo++;
			}


		}
		//tratar para re-envio em acks pequenos

		System.out.println("mandiocaServer");

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



	public static synchronized void remSeq(int seq) {
		semAckSequencia.remove(seq);
	}
	public static synchronized boolean isSeqEmpty() {
		return semAckSequencia.isEmpty();
	}
	public static synchronized int seqFirst() {
		return semAckSequencia.first();
	}
	public static synchronized void setBase(int _base) {
		base = _base;
	}
	public static synchronized int getBase() {
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
		//H:/Enviar/manguetown.mp3
		FileInputStream fis = new FileInputStream("teste7");
		InetAddress IPAddress = InetAddress.getByName("localhost");
		int port = 2000;


		SRserver breno = new SRserver();
		clientSocket = new DatagramSocket();
		//String teste = breno.receberString(port);
		//System.out.println(teste);
		breno.criarPacote(clientSocket,fis, IPAddress, port);


	}
}