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
	public static short janela;
	final public static int timeout = 500;
	public static DatagramSocket clientSocket;
	public static SortedSet<Short> semAckSequencia = new TreeSet<Short>();
	public static TreeMap <Short,DatagramPacket> pacotes = new TreeMap<Short,DatagramPacket>();
	
	public void criarPacote(FileInputStream fis,InetAddress IPAddress,int port) throws IOException {
		
		//basicos
			System.out.println("Sending in progress!");
			short seqNo=0, proximo; 
			int ackPort;
			janela = 7;
			
			
			if (port==1) ackPort = port++; //criando uma porta para ack 
			else ackPort = port--;	
			
			clientSocket = new DatagramSocket();
			
			
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
		        	while(proximo<base+janela) {
		        		if(pacotes.get(proximo) != null) {
		        			if(proximo==4){
		        				
		        			}else{
		        			clientSocket.send(pacotes.get(proximo));
		        			}
		        			// start a Timer thread for this packet
		        			new Thread(new timerThread(proximo)).start();
		        			//if(DEBUG > 0) System.out.println("Active timer threads: "+(Thread.activeCount() - 3));
		        		}
		        		proximo++;
		        	}
		        }
		       
		        
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
	public static synchronized void sendPacket(DatagramPacket p) {
		try {
			clientSocket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String args[]) throws Exception {
		
		FileInputStream fis = new FileInputStream("teste1");  
		InetAddress IPAddress = InetAddress.getByName("localhost");	
		int port = 2000;	
		
		
		SRserver breno = new SRserver();
		
		//String teste =  breno.receberString(port);
		//System.out.println(teste);
		breno.criarPacote(fis, IPAddress, port);	
			
			
		}
	}
	