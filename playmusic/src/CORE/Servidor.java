package CORE;

import java.io.File;

public class Servidor {

	private RepositorioMusica musicas;

	public Servidor(String diretorio){
		this.musicas = new RepositorioMusica(diretorio);
	}

	public void onServer(){
		while (true){
			//Recebe String da camada comunicação
			String receive = "Manguetown.mp3";
			if (receive.contains(":")){
				enviarMusica(receive.substring(0, receive.indexOf(":")), receive.substring(receive.indexOf(":") + 1, receive.length()));
			}else{
				if (receive.equals("pause")){
					pause();
				}else{
					if (receive.equals("resume")){
						resume();
					}else{
						if (receive.equals("lista")){
							enviarLista(); 
						}else{
							if (receive.equals("cancel")){
								cancel();
							}
						}
					}
				}
			}
		}
	}

	protected void enviarLista(){
		Thread lista = new EnviarLista(musicas);
		lista.start();
	}

	protected void enviarMusica(String musica, String autor){
		Thread music = new EnviarMusica(musica, autor, musicas);
		music.start();
	}
	
	protected void pause(){
		//Chamar função pausa transferencia
	}
	
	protected void resume(){
		//Chamar função reinicia transferencia
	}
	
	protected void cancel(){
		//Chamar função que cancela transferencia
	}
	
	protected void listaUser(){
		
	}
}