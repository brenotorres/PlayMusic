package COMUNICACAO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;






public class SRclient implements Serializable{
	public volatile static boolean waiting=true;
	boolean pause = false;



	//public static int DEBUG = 2;
	// TreeMap <Short,byte[]> pacotes = new TreeMap<Short,byte[]>();
	// ArrayList <DatagramPacket> acks = new ArrayList<DatagramPacket>();
	// SortedSet<Short> trackBase=new TreeSet<Short>();
	int valor, aux, portaPEGA;
	//public static DatagramSocket clientSocketString;
	public retorno receber(DatagramSocket serverSocket,int port,InetAddress IP ) throws IOException{
		int window = 50, ackPort;
		long pacotesperdidos = 0;
		long pacotestotais = 0;
		int base = 0, SeqNo = 0;
		TreeMap <Integer,byte[]> pacotes = new TreeMap<Integer,byte[]>();
		ArrayList <DatagramPacket> acks = new ArrayList<DatagramPacket>();
		SortedSet<Integer> trackBase=new TreeSet<Integer>();
		waiting = true;
		serverSocket.setReceiveBufferSize(100000000);

		short ackPorttest = 8;

		// if (port==1){
		// ackPort = port + 1;
		// }else {
		// ackPort = port - 1;
		// }


		serverSocket.setReuseAddress(true);
		@SuppressWarnings("resource")
		DatagramSocket clientSocket = new DatagramSocket();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		InetAddress IPAddress = IP;
		// checar depois
		serverSocket.setSoTimeout(0);
		System.out.println("Waiting for data...");

		byte[] dadosrecebidos = new byte[1009];
		//boolean waiting=true;
		byte[] buffer = new byte[1000];
		byte[] ACK = new byte[7];

		// for(short j=0;j<Short.MAX_VALUE;j++) {
		// ACK[0] = ByteUtils.convertToBytes(j)[0];
		// ACK[1] = ByteUtils.convertToBytes(j)[1];
		// for(int k=0;k<3;k++) {
		// ACK[k+2] = "ACK".getBytes()[k];
		// }
		//
		// DatagramPacket ackPacket = new DatagramPacket(ACK, ACK.length, IPAddress, ackPort);
		// acks.add(ackPacket);
		// trackBase.add((short)j);
		// ACK = new byte[5];
		// }
		// trackBase.remove((short)0);
		// base = 1;

		boolean primeiraVez = true;

		int portFonte = 0;
		InetAddress IPfonte = null;
		while (waiting) {
			try {

				while(pause){
					serverSocket.setSoTimeout(0);

				}

				DatagramPacket receivePacket = new DatagramPacket(dadosrecebidos,dadosrecebidos.length);
				serverSocket.receive(receivePacket);
				IPAddress = receivePacket.getAddress();
				
				
				
				
				
				System.out.println("awdeasdasdasd "+ receivePacket.getAddress());
				pacotestotais++;
				
				System.out.println("------------------"+IPAddress+" ---");



				serverSocket.setSoTimeout(1000);


				buffer = receivePacket.getData();

				portFonte = receivePacket.getPort();
				IPfonte = receivePacket.getAddress();


				//Modulo especial
				byte[] test = new byte[4];
				test[0] = buffer[1005];
				test[1] = buffer[1006];
				test[2] = buffer[1007];
				test[3] = buffer[1008];
				portaPEGA = Convert.toInt(test);






				byte[] porta = new byte[4];
				porta[0] = buffer[0];
				porta[1] = buffer[1];
				porta[2] = buffer[2];
				porta[3] = buffer[3];		
				aux = Convert.toInt(porta);



				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+ portaPEGA);

				valor = 1 + (int) (Math.random()*100);
			}
			catch(SocketTimeoutException e) {
				//e.printStackTrace();
				System.out.println("mandioca");
				waiting = false;
				continue;
			}






			// modulo especial
			if( valor < 0){ //em vez de 60 , pegar o valor do CORE (vindo da GUI)
				System.out.println("pacote " + aux + " perdido");
				pacotesperdidos++;
				dadosrecebidos = new byte[1009];
				buffer = new byte[1000];

			}else{
				//

				if(primeiraVez){
					primeiraVez = false;
					for(int j=0;j<1000000;j++) {
						ACK[0] = Convert.toByta(j)[0];
						ACK[1] = Convert.toByta(j)[1];
						ACK[2] = Convert.toByta(j)[2];
						ACK[3] = Convert.toByta(j)[3];
						for(int k=0;k<3;k++) {
							ACK[k+4] = "ACK".getBytes()[k];
						}

						DatagramPacket ackPacket = new DatagramPacket(ACK, ACK.length, IPAddress, portaPEGA);
						acks.add(ackPacket);
						trackBase.add((Integer)j);
						ACK = new byte[7];
					}
					trackBase.remove((Integer)0);
					base = 1;
				}
				
				
				byte[] seq = new byte[4];
				seq[0] = buffer[0];
				seq[1] = buffer[1];
				seq[2] = buffer[2];
				seq[3] = buffer[3];
				SeqNo = Convert.toInt(seq);
				System.out.println("Received "+SeqNo);
				
				if ((SeqNo >= base) && (SeqNo <= (base+window-1))){
					pacotes.put(SeqNo, ByteUtils.subbytes(buffer, 4, 1004));
					clientSocket.send(acks.get(SeqNo));
					byte tmp[] = new byte[4];
					tmp[0] = acks.get(SeqNo).getData()[0];
					tmp[1] = acks.get(SeqNo).getData()[1];
					tmp[2] = acks.get(SeqNo).getData()[2];
					tmp[3] = acks.get(SeqNo).getData()[3];
					System.out.println("ACK "+Convert.toInt(tmp));
					trackBase.remove(SeqNo);
					if (base == SeqNo) {
						base = trackBase.first();
					}
					continue;
				}
				if((SeqNo >= (base - window)) && (SeqNo <= base - 1)) {
					clientSocket.send(acks.get(SeqNo));
					byte tmp[] = new byte[4];
					tmp[0] = acks.get(SeqNo).getData()[0];
					tmp[1] = acks.get(SeqNo).getData()[1];
					tmp[2] = acks.get(SeqNo).getData()[2];
					tmp[3] = acks.get(SeqNo).getData()[3];
					System.out.println("ACK "+Convert.toInt(tmp));
					continue;
				}
				dadosrecebidos = new byte[1009];
				buffer = new byte[1000];


			}

		}

		System.out.println("Teve "+ pacotesperdidos +" pacotes perdidos");
		System.out.println("Teve " + pacotestotais + " pacotes no total");
		long temporaria = (long)pacotesperdidos/pacotestotais;
		System.out.println("media de " + temporaria+ " %");

		if (!pacotes.isEmpty()){
			for(int z=0;z<pacotes.lastKey()+1;z++) {
				byte parse[] = pacotes.get(z);
				if(parse!=null) bos.write(parse,0,parse.length);
			}
		}


		File filename = new File("novo");
		FileOutputStream fos = new FileOutputStream(filename);
		byte[] arquivoComLixo = bos.toByteArray();
		int lastpos = arquivoComLixo.length;


		for(int i=arquivoComLixo.length-1;i>=0;i--) {
			if(arquivoComLixo[i] != 0) {
				lastpos = i+1;
				break;
			}
		}

		retorno teste = new retorno(IPfonte, portFonte,filename);

		fos.write(ByteUtils.subbytes(arquivoComLixo, 0, lastpos));
		fos.close();
		serverSocket.close();
		clientSocket.close();

		return teste;
	}

	public void setPause(boolean entrada){
		this.pause = entrada;
	}

	public void enviarString(String nome,InetAddress IPAddress,int port ) throws IOException{

		byte[] string = new byte[100];

		DatagramSocket clientSocket = new DatagramSocket();
		string = nome.getBytes();

		DatagramPacket pck = new DatagramPacket(string, string.length,IPAddress,port);

		clientSocket.send(pck);

	}

	public static void main(String[] args) throws UnknownHostException, SocketException {


		InetAddress IPAddress = InetAddress.getByName("localhost");
		int port = 2000;
		//int ackPort = 2001;

		SRclient breno = new SRclient();
		DatagramSocket serverSocket = new DatagramSocket(port);
		retorno breno2 = null;
		try {
			breno2 = breno.receber( serverSocket, port , IPAddress);
		} catch (IOException e) {

			e.printStackTrace();
		}
		//String nome = "manteiga";
		//try {
		// breno.enviarString(nome, IPAddress, port);
		//} catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		//}


		//System.out.println(breno2.exists());
	}



}