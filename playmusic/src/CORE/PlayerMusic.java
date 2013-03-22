package CORE;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


public class PlayerMusic implements interfacePlayer, Runnable{
	private AudioFormat format;
	private AudioFormat decodedFormat;
	private AudioInputStream audioin;
	private SourceDataLine line;
	private File dataSource;
	private Thread thread;
	private AudioFileFormat baseFileFormat = null;
	private AudioFormat baseFormat = null;
	private String author;
	private String title;
	private String album; 

	
	public boolean erro = false;

	public static final int UNKNOWN = -1;
	public static final int PLAYING = 0;
	public static final int PAUSED = 1;
	public static final int STOPPED = 2;
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
		if (line != null){
			line.stop();
			line.close();
			line = null;
		}
	}

	protected void closeStream(){
		try {
			synchronized (audioin){
				if (audioin != null){
					audioin.close();
					audioin = null;
				}
			}
		} catch (IOException e) {
			erro = true;
		}
	}

	protected void playMusic(){
		if (dataSource != null){
			thread = new Thread(this);
			thread.start();
		}else{
			//Arquivo não existe
		}
	}

	protected void pauseMusic(){
		if (line != null){
			if(state == PLAYING){
				state = PAUSED;
				line.stop();
				line.flush();
			}
		}
	}

	protected void stopMusic(){
		if (state == PLAYING || state == PAUSED){
			if (line != null){
				state = STOPPED;
				line.stop();
				line.flush();
				closeStream();
			}
		}
	}

	protected void resumeMusic(){
		state = PLAYING;
		line.start();
	}

	protected void open(File file){
		dataSource = file;
		try {
			if (audioin != null){
				closeStream();
			}
			prop(file);
			audioin = AudioSystem.getAudioInputStream(dataSource);
			if (audioin != null){
				format = audioin.getFormat();
				decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
						format.getSampleRate(), 16, format.getChannels(), format.getChannels() * 2,
						format.getSampleRate(), false);
				audioin = AudioSystem.getAudioInputStream(decodedFormat, audioin);
			}
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			synchronized (audioin){
				byte[] data = new byte[4096];
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
				line = (SourceDataLine) AudioSystem.getLine(info);
				line.open(decodedFormat);
				if (line != null){
					// Start
					state = PLAYING;
					line.start();
					int nBytesRead = 0, nBytesWritten = 0;
					while (nBytesRead != -1 && state != STOPPED){
						nBytesRead = audioin.read(data, 0, data.length);
						if (nBytesRead != -1){
							nBytesWritten = line.write(data, 0, nBytesRead);
						}
						while(state == PAUSED){}
					}
					// Stop
					if (line != null){
						state = UNKNOWN;
						line.drain();
						line.stop();
						line.close();
						line = null;
						closeStream();
					}
				}
			}
		} catch (LineUnavailableException e) {
			erro = true;
		} catch (IOException e) {
			erro = true;
		}
	}

	public void play(File file) {
		if (state == PAUSED){
			resumeMusic();
		}else{
			open(file);
			playMusic();
		}
	}

	public void stop() {
		stopMusic();
	}

	public void pause() {
		pauseMusic();
	}
	
	protected void prop(File file) throws UnsupportedAudioFileException, IOException {
		baseFileFormat = AudioSystem.getAudioFileFormat(file);
		baseFormat = baseFileFormat.getFormat();
		Map properties = ((AudioFileFormat)baseFileFormat).properties();
		
		String key = "author";
		author = (String) properties.get(key);
		key = "title";
		title = (String) properties.get(key);
		key = "album";
		album = (String) properties.get(key);
	}

	public String get_album() {
		return album;
	}

	public String get_title() {
		return title;
	}

	public String get_author() {
		return author;
	}

	
}