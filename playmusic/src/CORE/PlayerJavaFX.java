package CORE;

import java.io.File;

import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class PlayerJavaFX implements interfacePlayer{

	public MediaPlayer mp; 
	private Status status ;
	private double volumeatual = 0.7;
	public Duration duration;



	@Override
	public void pause() {
		mp.pause();		
	}

	@Override
	public void stop() {
		if(mp!=null){
			mp.stop();
			status = mp.getStatus();
			mp = null;
		}else{}
	}

	@Override
	public void seek(double sec) {
		if(mp.getTotalDuration() != null){
			mp.seek(duration.multiply(sec / 100.0));
		}
	}



	@Override
	public String get_album() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String get_title() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String get_author() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set_volume(double d) {
		mp.setVolume(d);
		volumeatual = d;
	}



	@Override
	public float get_maximo() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double get_minimo() {
		return 0.0;
	}

	@Override
	public double get_volumeAtual() {
		return mp.getVolume();
	}

	@Override
	public long get_Tempo() {
		return (long) Math.floor(mp.getMedia().getDuration().toSeconds());
	}

	@Override
	public void play(Media m) {
		if(mp == null){
			mp = new MediaPlayer(m);
			status = mp.getStatus();
			mp.setVolume(volumeatual);
			System.out.println(volumeatual);
		}
		if(status == Status.UNKNOWN || status == Status.PAUSED || status == Status.READY || status == Status.STOPPED){
			mp.play();
			mp.setOnReady(new Runnable() {
				public void run() {
					System.out.println("setonready");
					duration = mp.getMedia().getDuration();
				}
			});	}
	}

	@Override
	public void mute(boolean bmute) {
		if(bmute){
			volumeatual = mp.getVolume();
			mp.setMute(true);
		}else{
			mp.setMute(false);
			mp.setVolume(volumeatual);
		}
	}


	@Override
	public void reproducao() {
	}

}
