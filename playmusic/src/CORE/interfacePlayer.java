package CORE;

import java.io.File;

public interface interfacePlayer {
      
    void play(File file);
      
    void pause();
    
    void stop();
    
    void seek(int sec);
    
    String get_album();
    
    String get_title();
    
    String get_author();
    
}