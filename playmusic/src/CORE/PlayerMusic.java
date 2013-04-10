package CORE;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
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
	private String author;
	private String title;
	private String album; 


	private FloatControl gainControl;
	private boolean controleVolume = false;


	public boolean erro = false;

	private static final int UNKNOWN = -1;
	private static final int PLAYING = 0;
	private static final int PAUSED = 1;
	private static final int STOPPED = 2;
	private static final int SEEKING = 3;
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
			if (audioin != null){
				audioin.close();
				audioin = null;
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
				line.flush();
				line.stop();
			}
		}
	}

	protected void stopMusic(){
		if (state == PLAYING || state == PAUSED){
			if (line != null){
				state = STOPPED;
				line.stop();
				line.flush();
				synchronized (audioin){
					closeStream();
				}
			}
		}
	}

	protected void resumeMusic(){
		if(line!=null){
			state = PLAYING;
			line.start();
		}
	}

	protected void open(File file){
		dataSource = file;
		try {
			if (audioin != null){
				synchronized (audioin){
					closeStream();
				}
			}
			prop(file);
			audioin = AudioSystem.getAudioInputStream(dataSource);
			if (audioin != null){
				//volumeControl(audioin);
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

	protected void skip(long bytes){
		try {	
			int pState = state;
			state = SEEKING;
			long total = 0;
			long skipped;
			boolean sair = false;
			synchronized (audioin){
				while (total < bytes && !sair){
					skipped = audioin.skip(bytes - total);
					if (skipped == 0){
						sair = true;
					}
					total = total + skipped;
				}
				state = pState;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//	public void run() {
	//		try {
	//			byte[] data = new byte[4096];
	//			DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
	//			line = (SourceDataLine) AudioSystem.getLine(info);
	//			line.open(decodedFormat);
	//			if (line != null){
	//				volumeControl();
	//				// Start
	//				state = PLAYING;
	//				line.start();
	//				synchronized (audioin){
	//					int nBytesRead = 0;
	//					while (nBytesRead != -1 && state != STOPPED){
	//						nBytesRead = audioin.read(data, 0, data.length);
	//						if (nBytesRead != -1 && line != null){
	//							line.write(data, 0, nBytesRead);
	//						}
	//						while(state == PAUSED || state == SEEKING){}
	//					}
	//					// Stop
	//					if (line != null){
	//						state = UNKNOWN;
	//						line.drain();
	//						line.stop();
	//						line.close();
	//						line = null;
	//						closeStream();
	//					}
	//				}
	//			}
	//		} catch (LineUnavailableException e) {
	//			erro = true;
	//		} catch (IOException e) {
	//			erro = true;
	//		}
	//	}

	public void run() {
		try {
			synchronized (audioin){
				byte[] data = new byte[4096];
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
				line = (SourceDataLine) AudioSystem.getLine(info);
				line.open(decodedFormat);
				if (line != null){
					volumeControl();
					// Start
					state = PLAYING;
					line.start();
					int nBytesRead = 0, nBytesWritten = 0;
					while (nBytesRead != -1 && state != STOPPED){
						if(state==PLAYING){	
							nBytesRead = audioin.read(data, 0, data.length);
							if (nBytesRead != -1){
								nBytesWritten = line.write(data, 0, nBytesRead);
							}
						}else{
							try {
								Thread.sleep(0);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
						}
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

	public void seek(int sec){
		stopMusic();
		line = null;
		System.out.println("Entrou agora!");
		play(dataSource);
		long skip = Math.round(format.getFrameRate() * decodedFormat.getFrameSize() * sec * 100);
		System.out.println("framerate "+(int)format.getFrameRate()+" framesize "+decodedFormat.getFrameSize()+" skip "+skip);
		skip(skip);
	}

	protected void volumeControl(){
		if ( (line != null) && (line.isControlSupported(FloatControl.Type.MASTER_GAIN))){
			gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
			controleVolume = true;
		}else{
			controleVolume = false;
		}
	}

	public void set_volume(float fa){
		if(controleVolume&&(fa<=get_maximo())&&(fa>=get_minimo())){
			gainControl.setValue(fa); 
			//System.out.println("#comeu a bronca"+gainControl.getValue());
		}else{
			volumeControl();
			//System.out.println("#comendo a bronca");
		}
	}

	public float get_volumeAtual(){
		volumeControl();
		if(controleVolume)
			return gainControl.getValue();
		return 0;
	}

	public float get_maximo(){
		if(controleVolume){
			return gainControl.getMaximum();
		}else{
			volumeControl();
			if(controleVolume){
				return gainControl.getMaximum();
			}
		}
		return 0;

	}

	public float get_minimo(){
		if(controleVolume){
			return gainControl.getMinimum();
		}else{
			volumeControl();
			if(controleVolume){
				return gainControl.getMinimum();
			}
		}
		return 0;
	}

	public long Microseconds(){
		if(line!= null){
			return line.getMicrosecondPosition()/1000000;
		}else{
			return 0;
		}
			
	}
}
