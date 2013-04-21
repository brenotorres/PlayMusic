package CORE;

import java.io.File;

import COMUNICACAO.retorno;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public interface interfacePlayer {
      
    void play(Media file);
      
    void pause();
    
    void stop();
    
    void seek(double sec);
    
    String get_album();
    
    String get_title();
    
    String get_author();
    
    void set_volume(double f);
    	
    float get_maximo();
    
    double get_minimo();
    
	double get_volumeAtual();

	long get_Tempo();
	
	void mute(boolean mute);
	
	void reproducao();

	//void updateValues();
	
}