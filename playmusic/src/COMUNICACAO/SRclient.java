package COMUNICACAO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
	
	public File receber(int port,InetAddress IP,int ackPort ) throws IOException{
		int window = 300;
		short base, SeqNo = 0;
		
		DatagramSocket serverSocket = new DatagramSocket(port);
        serverSocket.setReuseAddress(true);
        @SuppressWarnings("resource")
		DatagramSocket clientSocket = new DatagramSocket();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		InetAddress IPAddress = IP;
		// checar depois
        serverSocket.setSoTimeout(1000000);
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
        
		 while (waiting) {
			 try {
			 DatagramPacket receivePacket = new DatagramPacket(dadosrecebidos,dadosrecebidos.length);
			 serverSocket.receive(receivePacket);
			 buffer = receivePacket.getData();
			 }
			 catch(SocketTimeoutException e) {
				 continue;
			 }
			 serverSocket.setSoTimeout(1000);
			 byte[] seq = new byte[2];
			 seq[0] = buffer[0];
			 seq[1] = buffer[1];
			 SeqNo = ByteUtils.convertShortFromBytes(seq);
			 //if(DEBUG > 1) System.out.println("Received "+SeqNo);
			 if ((SeqNo >= base) && (SeqNo <= (base+window-1))){
				 pacotes.put(SeqNo, ByteUtils.subbytes(buffer, 2, 1002));
				 clientSocket.send(acks.get(SeqNo));
				 byte tmp[] = new byte[2];
				 tmp[0] = acks.get(SeqNo).getData()[0];
				 tmp[1] = acks.get(SeqNo).getData()[1];
				 //if(DEBUG > 1) System.out.println("ACK "+ByteUtils.convertShortFromBytes(tmp));
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
				 //if(DEBUG > 1) System.out.println("ACK "+ByteUtils.convertShortFromBytes(tmp));
				 continue;
			 }
		 } 
		 
		 for(short z=0;z<pacotes.lastKey()+1;z++) {
	     		byte parse[] = pacotes.get(z);
	     		if(parse!=null) bos.write(parse,0,parse.length);
	     		}
		 
		 
	         	File filename = new File("novo");
	            FileOutputStream fos = new FileOutputStream(filename);
	            byte[] entireFileWithJunk = bos.toByteArray();
	            int lastpos = entireFileWithJunk.length;
	            // find what's the last zero
	            for(int i=entireFileWithJunk.length-1;i>=0;i--) {
	            	if(entireFileWithJunk[i] != 0) {
	            		lastpos = i+1;
	            		break;
	            	}
	            }
	            
	            
	            // subbyte the string and write it to file
	            fos.write(ByteUtils.subbytes(entireFileWithJunk, 0, lastpos));
	            fos.close();
	            serverSocket.close();
	            
				return filename;
	}
	
	
	
	
	public static void main(String[] args) throws UnknownHostException {
		
		 
		InetAddress IPAddress = InetAddress.getByName("localhost");	
		int port = 2000;	
		int ackPort = 2001;	
		
		SRclient breno = new SRclient();	
		
			File breno2 = null;
			try {
				breno2 = breno.receber( port , IPAddress, ackPort);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			System.out.println(breno2.exists());
		}
}	
	

