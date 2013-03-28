package COMUNICACAO;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class ackThread extends Thread {
	
	//private static int DEBUG = Sender4.DEBUG;
	private short ultimoAck;
	private DatagramSocket serverSocket;
	private boolean esperar1 = true;
	public static volatile ArrayList<Short> ackRecebido = new ArrayList<Short>();
	
	

    public ackThread(int ackPort, short ultimo, int timeout) {
    	ultimoAck = ultimo;
		try {
			serverSocket = new DatagramSocket(ackPort);
			serverSocket.setSoTimeout(0);
		} catch (SocketException e) {
			e.printStackTrace();
		}
    }
	
	
	public void run() {
		 byte[] ack = new byte[5];
		 
		 while(esperar1){
			 DatagramPacket pacote = new DatagramPacket(ack, ack.length);
			 
			 
			 try {
				serverSocket.receive(pacote);
			} catch (IOException e) {
				e.printStackTrace();
			}
			 
		byte[] sequenciaAck = new byte[2];
		
		
		sequenciaAck[0] = ack[0];
		sequenciaAck[1]	= ack[1];
			 
		short sequenciaAtual = ByteUtils.convertShortFromBytes(sequenciaAck);	 
		
		short b = SRserver.getBase();
		
		
		
		if((b<=sequenciaAtual)&& (sequenciaAtual<=(b+SRserver.janela-1))){
			
			ackRecebido.add(sequenciaAtual);
			
			SRserver.remSeq(sequenciaAtual);
			
			if(sequenciaAtual == b && !SRserver.isSeqEmpty()){
				SRserver.setBase(SRserver.seqFirst()); //isso faz a base caminhar
			}
			
			
			//tratando para ultimo ACK
			
			if(sequenciaAtual==ultimoAck){
				esperar1= false;
				SRserver.esperar = false;
				SRclient.waiting = false;
			}
			
			
			
		}
		
		
		
		 }
	}
	
	public static boolean ackContains(short c) {
		return ackRecebido.contains(c);
	}
	
	
	
}
