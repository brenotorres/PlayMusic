package COMUNICACAO;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class ackThread extends Thread {
	//teste
	//private static int DEBUG = Sender4.DEBUG;
	private int ultimoAck;
	private DatagramSocket serverSocket;
	private boolean esperar1 = true;
	public static volatile ArrayList<Integer> ackRecebido = new ArrayList<Integer>();






	public void run() {
		byte[] ack = new byte[7];
		ackRecebido.clear();
		
		while(esperar1){
			DatagramPacket pacote = new DatagramPacket(ack, ack.length);

			try {
				serverSocket.receive(pacote);
				System.out.println("chupeta");
			} catch (IOException e) {
				e.printStackTrace();
			}

			byte[] sequenciaAck = new byte[4];


			sequenciaAck[0] = ack[0];
			sequenciaAck[1] = ack[1];
			sequenciaAck[2] = ack[2];
			sequenciaAck[3] = ack[3];
			
			int sequenciaAtual = Convert.toInt(sequenciaAck);

			System.out.println("Sequencia atual de ack " + sequenciaAtual);

			int b = SRserver.getBase();


			if((b<=sequenciaAtual)&& (sequenciaAtual<=(b+SRserver.janela-1))){

				ackRecebido.add(sequenciaAtual);

				SRserver.remSeq(sequenciaAtual);

				if(sequenciaAtual == b && !SRserver.isSeqEmpty()){
					SRserver.setBase(SRserver.seqFirst()); //isso faz a base caminhar
					System.out.println("Setou " +sequenciaAtual+ " como a nova base");
				}


				//tratando para ultimo ACK

				if(sequenciaAtual==ultimoAck){
					if(SRserver.isSeqEmpty()){
						esperar1= false;
						//SRserver.esperar = false;
						//SRclient.waiting = false;
					}

				}

			}

		}
		System.out.println("thread ack acabou aqui!");
		serverSocket.close();

	}

	public static boolean ackContains(int c) {
		return ackRecebido.contains(c);
	}
	public ackThread(DatagramSocket serverSocket, int ultimo, int timeout) {
		ultimoAck = ultimo;
		try {
			// serverSocket = new DatagramSocket(ackPort);
			this.serverSocket = serverSocket;
			serverSocket.setSoTimeout(0);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}


}