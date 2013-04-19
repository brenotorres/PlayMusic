package CORE;

import java.io.File;

public interface interfacePlayer {
      
    void play(File file);
      
    void pause();
    
    void stop();
    
    void seek(long sec);
    
    String get_album();
    
    String get_title();
    
    String get_author();
    
    void set_volume(float f);
    	
    float get_maximo();
    
    float get_minimo();
    
	float get_volumeAtual();

	long Microseconds();
	
}