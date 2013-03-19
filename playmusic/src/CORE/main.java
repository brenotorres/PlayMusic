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
        File file = new File("C:/Users/Toinho/Desktop/My Shared Folder/chico science & nação zumbi - manguetown.mp3");
        i.play(file);
        while (true){
        	b.readLine();
        	i.stop();
        	b.readLine();
        	i.play(file);
        }
        }catch (IOException e){
        	System.out.println("Deu merda aqui vei");
        }
    }
}