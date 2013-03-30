package CORE;

import java.io.File;

public class Servidor {

	private File diretorio = new File("H:/Musicas");

	public Servidor(){
		
	}

	public void onServer(){
		while (true){
			//Recebe String da camada comunica��o
			String receive = "Manguetown:Chico Science";
			if (receive.contains(":")){
				enviarMusica(receive.substring(0, receive.indexOf(":")), receive.substring(receive.indexOf(":") + 1, receive.length()));
			}else{
				if (receive.equals("pause")){
					//Chamar fun��o pausa transferencia
				}else{
					if (receive.equals("start")){
						//Chamar fun��o reinicia transferencia
					}else{
						if (receive.equals("lista")){
							enviarLista(); 
						}
					}
				}
			}
		}
	}

	protected void enviarLista(){
		Thread lista = new EnviarLista(diretorio);
		lista.start();
	}

	protected void enviarMusica(String musica, String autor){
		Thread music = new EnviarMusica(musica, autor, diretorio);
		music.start();
	}
}