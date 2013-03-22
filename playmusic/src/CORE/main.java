package CORE;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class main {
	
    public static void main(String[] args){
        BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
        interfacePlayer i = new PlayerMusic();
        try{
	        File file = new File("manguetown.mp3");
	        i.play(file);
	        System.out.println(i.get_album()+i.get_author()+i.get_title());
	        while (true){
	        	b.readLine();
	        	i.seek(60);
	        	b.readLine();
	        	i.play(file);
	        }
	    }catch (IOException e){
        	System.out.println("Deu merda aqui vei");
        }
    }
}