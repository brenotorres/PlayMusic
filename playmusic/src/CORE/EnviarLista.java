package CORE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Vector;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class EnviarLista extends Thread{
    
    private File diretorio;
    private AudioFileFormat audioFileFormat;
    private Map mapa;
    private Vector<Mp3> lista;
    
    public EnviarLista(File diretorio){
        this.diretorio = diretorio;
    }
    
    
    protected void gerarLista(){
        File arquivos[] = diretorio.listFiles();
        Mp3 temp;
        long microseconds;
        String time;
        try {
            for (int i = 0; i < arquivos.length; i++){
                audioFileFormat = AudioSystem.getAudioFileFormat(arquivos[i]);
                mapa = ((AudioFileFormat)audioFileFormat).properties();
                temp = new Mp3();
                temp.setAutor((String)mapa.get("author"));
                temp.setAlbum((String)mapa.get("album"));
                temp.setGenero((String)mapa.get("id3v1genres"));
                //temp.setTitulo((String)mapa.get("title"));
                temp.setNome((String)mapa.get("title"));
                temp.setTamanho("" + arquivos[i].length());
                microseconds = (long)mapa.get("duration");
                int mili = (int) (microseconds / 1000);
                int sec = (mili / 1000) % 60;
                int min = (mili / 1000) / 60;
                time = min + ":" + sec;
                temp.setTempo(time);

                lista.addElement(temp);
            }

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        gerarLista();

        File arquivo = new File("lista.txt");   
        try {
            if (!arquivo.exists()) { 
                arquivo.createNewFile();
            }
            OutputStream out = new FileOutputStream(arquivo);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);  

            objectOutputStream.writeObject(lista);
            objectOutputStream.close();  
            out.close();
            
            FileOutputStream fos = new FileOutputStream(arquivo);
            //chamar metodo da comunicação pra enviar arquivo serializado

        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}