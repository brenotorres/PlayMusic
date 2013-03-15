package CORE;

import java.io.File;  
import java.io.IOException;  
import javax.sound.sampled.AudioFormat;  
import javax.sound.sampled.AudioInputStream;  
import javax.sound.sampled.AudioSystem;  
import javax.sound.sampled.DataLine;  
import javax.sound.sampled.LineUnavailableException;  
import javax.sound.sampled.SourceDataLine;

public class PlayerMusic implements interfacePlayer, Runnable{
	
	private AudioFormat format;
	private AudioInputStream audioin;
	private AudioInputStream encodedain;
	private SourceDataLine line;
	private File dataSource;
	
	public static final int UNKNOWN = -1;
    public static final int PLAYING = 0;
    public static final int PAUSED = 1;
    public static final int OPENED = 2;
    private int state = UNKNOWN;
	
	public PlayerMusic(){
        dataSource = null;
        state = UNKNOWN;
        if (audioin != null){
            synchronized (audioin){
                closeStream();
            }
        }
        audioin = null;
        format = null;
        encodedain = null;
        if (line != null){
            line.stop();
            line.close();
            line = null;
        }
    }
	
	protected void closeStream(){
		try {
			audioin.close();
		} catch (IOException e) {
			
		}
	}
	
	protected void open(File file) {
		
	}
	
	protected void playMusic(){
		
	}
	
	protected void pauseMusic(){
		
	}
	
	protected void cancelMusic(){
		
	}

	public void run() {
		
	}

	public void play(File file) {
		open(file);
		playMusic();
	}

	public void cancel() {
		cancelMusic();
	}

	public void pause() {
		pauseMusic();
	}	
}
