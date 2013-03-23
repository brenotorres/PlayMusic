package COMUNICACAO;

public class timerThread extends Thread {

	protected int checar = 1;
	
	public static int DEBUG = SRserver.DEBUG;
	
	private int tamanhoTimeout = SRserver.timeout;
	
	private int tempoPassado;
	
	private short threadSequencia;
	
	private boolean running = true;
	
	public timerThread(short sequencia){
		Thread.currentThread().setPriority(MIN_PRIORITY);
		
		if(DEBUG > 1) System.out.println("Sending: "+sequencia);
		threadSequencia = sequencia;
	    tempoPassado = 0;
	}
	
	
		
		public void run()
		{
			// Keep looping
			loop:
				while (running){
					// Put the timer to sleep
					try{ 
						Thread.sleep(checar);
					}catch (InterruptedException ioe) {
						continue;
					}
					
					
					// is the packet ACK-ed?
					if(ackThread.ackContains(threadSequencia)) {
						running = false;
						break loop;
					}
					synchronized (this) {
						// Increment time remaining
						tempoPassado += checar;

						// Check to see if the time has been exceeded
						if (tempoPassado > tamanhoTimeout)
						{
							// Trigger a timeout and reset timer
							synchronized (this) {//testar depois
								timeout();
								tempoPassado = 0;
							}
						}
					}
				}
		}
		
		public void timeout() {
			synchronized (this) {
				try {
					// re-send packet
					SRserver.clientSocket.send(SRserver.pacotes.get(threadSequencia));
				} catch (Exception e) {
					// in case by the time this reaches the timeout, the ACK is received and the clientSocket is closed.
					System.exit(0);
				}
				if(DEBUG > 1) System.out.println("Resend "+threadSequencia);
			}
		}
	
	
}
