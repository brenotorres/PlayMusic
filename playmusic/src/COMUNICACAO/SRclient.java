package COMUNICACAO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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






public class SRclient {
	public volatile static boolean waiting=true;
	//public static int DEBUG = 2;
	TreeMap <Short,byte[]> pacotes = new TreeMap<Short,byte[]>();
	ArrayList <DatagramPacket> acks = new ArrayList<DatagramPacket>();
	SortedSet<Short> trackBase=new TreeSet<Short>();
	//public static DatagramSocket clientSocketString;
	public retorno receber(DatagramSocket serverSocket,int port,InetAddress IP ) throws IOException{
		int window = 7, ackPort;
		short base, SeqNo = 0;

		if (port==1){
			ackPort = port + 1;
		}else {
			ackPort = port - 1;
		}


		serverSocket.setReuseAddress(true);
		@SuppressWarnings("resource")
		DatagramSocket clientSocket = new DatagramSocket();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		InetAddress IPAddress = IP;
		// checar depois
		serverSocket.setSoTimeout(1000000000);
		System.out.println("Waiting for data...");

		byte[] dadosrecebidos = new byte[1003];
		boolean waiting=true;
		byte[] buffer = new byte[1000];
		byte[] ACK = new byte[5];

		for(short j=0;j<Short.MAX_VALUE;j++) {
			ACK[0] = ByteUtils.convertToBytes(j)[0];
			ACK[1] = ByteUtils.convertToBytes(j)[1];
			for(int k=0;k<3;k++) {
				ACK[k+2] = "ACK".getBytes()[k];
			}

			DatagramPacket ackPacket = new DatagramPacket(ACK, ACK.length, IPAddress, ackPort);
			acks.add(ackPacket);
			trackBase.add((short)j);
			ACK = new byte[5];
		}
		trackBase.remove((short)0);
		base = 1;

		int portFonte = 0;
		InetAddress IPfonte = null;
		while (waiting) {
			try {
				DatagramPacket receivePacket = new DatagramPacket(dadosrecebidos,dadosrecebidos.length);
				serverSocket.receive(receivePacket);
				System.out.println("awdeasdasdasd "+ receivePacket.getAddress());

				buffer = receivePacket.getData();

				portFonte = receivePacket.getPort();
				IPfonte = receivePacket.getAddress();
			}
			catch(SocketTimeoutException e) {
				waiting = false;
				continue;
			}
			serverSocket.setSoTimeout(1000);
			byte[] seq = new byte[2];
			seq[0] = buffer[0];
			seq[1] = buffer[1];
			SeqNo = ByteUtils.convertShortFromBytes(seq);
			System.out.println("Received "+SeqNo);
			if ((SeqNo >= base) && (SeqNo <= (base+window-1))){
				pacotes.put(SeqNo, ByteUtils.subbytes(buffer, 2, 1002));
				clientSocket.send(acks.get(SeqNo));
				byte tmp[] = new byte[2];
				tmp[0] = acks.get(SeqNo).getData()[0];
				tmp[1] = acks.get(SeqNo).getData()[1];
				System.out.println("ACK "+ByteUtils.convertShortFromBytes(tmp));
				trackBase.remove(SeqNo);
				if (base == SeqNo) {
					base = trackBase.first();
				}
				continue;
			}
			if((SeqNo >= (base - window)) && (SeqNo <= base - 1)) {
				clientSocket.send(acks.get(SeqNo));
				byte tmp[] = new byte[2];
				tmp[0] = acks.get(SeqNo).getData()[0];
				tmp[1] = acks.get(SeqNo).getData()[1];
				System.out.println("ACK "+ByteUtils.convertShortFromBytes(tmp));
				continue;
			}
		}

		for(short z=0;z<pacotes.lastKey()+1;z++) {
			byte parse[] = pacotes.get(z);
			if(parse!=null) bos.write(parse,0,parse.length);
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

		return teste;



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