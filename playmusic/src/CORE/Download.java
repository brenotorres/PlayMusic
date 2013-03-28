package CORE;

public class Download extends Thread{
    
    private String musica;
    private String autor;

    public Download(String musica, String autor){
        this.musica = musica;
        this.autor = autor;
    }

    public void run(){
        String toServer = this.musica + ":" + this.autor;
        //Chama metodo que envia a string
        
        
        
        //Chama metodo para receber o arquivo
    }
}
