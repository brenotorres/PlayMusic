package CORE;

import java.io.File;

public interface interfacePlayer {
      
    void play(File file);
      
    void pause();
    
    void stop();
    
    String get_album();
    
    String get_title();
    
    String get_author();
    
}