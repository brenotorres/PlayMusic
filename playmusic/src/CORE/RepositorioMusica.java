package CORE;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class RepositorioMusica {

	private Vector<Mp3> lista;
	private File diretorio;
	private AudioFileFormat audioFileFormat;
	private Map mapa;
	private String nomeDiretorio;

	public RepositorioMusica(String diretorio){
		this.diretorio = new File(diretorio);
		this.nomeDiretorio = diretorio;
	}

	protected Vector<Mp3> gerarLista() throws UnsupportedAudioFileException, IOException{
		lista = new Vector<Mp3>();
		File arquivos[] = diretorio.listFiles();
		Mp3 temp;
		long microseconds;
		String time;
		for (int i = 0; i < arquivos.length; i++){
			audioFileFormat = AudioSystem.getAudioFileFormat(arquivos[i]);
			mapa = ((AudioFileFormat)audioFileFormat).properties();
			temp = new Mp3();
			temp.setAutor((String)mapa.get("author"));
			temp.setAlbum((String)mapa.get("album"));
			temp.setGenero((String)mapa.get("id3v1genres"));
			temp.setTitulo((String)mapa.get("title"));
			temp.setNome(arquivos[i].getName());
			temp.setTamanho("" + arquivos[i].length());
			microseconds = (long)mapa.get("duration");
			int mili = (int) (microseconds / 1000);
			int sec = (mili / 1000) % 60;
			int min = (mili / 1000) / 60;
			time = min + ":" + sec;
			temp.setTempo(time);

			lista.addElement(temp);
		}
		return lista;
	}

	public File procurarMusica(String musica, String autor) throws UnsupportedAudioFileException, IOException{
		File arquivo = null;
		File arquivos[] = diretorio.listFiles();
		boolean encontrou = false;
		for (int i = 0; i < arquivos.length && !encontrou; i++){
			audioFileFormat = AudioSystem.getAudioFileFormat(arquivos[i]);
			mapa = ((AudioFileFormat)audioFileFormat).properties();
			if (musica.equals(((String)mapa.get("title")))){
				if (autor.equals(((String)mapa.get("author")))){
					arquivo = arquivos[i];
					encontrou = true;
				}
			}
		}
		return arquivo;
	}
	
	public File procurarMusica(String musica) throws UnsupportedAudioFileException, IOException{
		File arquivo = null;
		boolean encontrou = false;
		for (int i = 0; i < lista.size() && !encontrou; i++){
			if (musica.equals(lista.get(i).getNome())){
				encontrou = true;
			}
		}
		if (encontrou){
			arquivo = new File(nomeDiretorio + "/" + musica);
		}
		return arquivo;
	}
	
	public int getIndice(String musica){
		int indice = -1;
		boolean encontrou = false;
		for (int i = 0; i < lista.size() && !encontrou; i++){
			if (musica.equals(lista.get(i).getNome())){
				encontrou = true;
				indice = i;
			}
		}
		return indice;
	}
	
	public void adicionarMusica(String arquivo) throws UnsupportedAudioFileException, IOException{
		//Copiar arquivo pra pasta
		Mp3 temp = new Mp3();
		File arq = new File(nomeDiretorio + arquivo);
		audioFileFormat = AudioSystem.getAudioFileFormat(arq);
		mapa = ((AudioFileFormat)audioFileFormat).properties();
		temp.setAutor((String)mapa.get("author"));
		temp.setAlbum((String)mapa.get("album"));
		temp.setGenero((String)mapa.get("id3v1genres"));
		temp.setTitulo((String)mapa.get("title"));
		temp.setNome((String)mapa.get(arq.getName()));
		temp.setTamanho("" + arq.length());
		long microseconds = (long)mapa.get("duration");
		int mili = (int) (microseconds / 1000);
		int sec = (mili / 1000) % 60;
		int min = (mili / 1000) / 60;
		String time = min + ":" + sec;
		temp.setTempo(time);

		lista.addElement(temp);
	}
	
	public void removerMusica(String arquivo){
		//Remover musica da pasta
		int indice = getIndice(arquivo);
		if (indice != -1){
			lista.remove(indice);
		}
	}
}
