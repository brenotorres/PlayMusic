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

	private String[] id3v1genres = { "Blues",
			"Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge",
			"Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop",
			"R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative",
			"Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno",
			"Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance",
			"Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip",
			"Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space",
			"Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic",
			"Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk",
			"Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta",
			"Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American",
			"Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer",
			"Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro",
			"Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock",
			"National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival",
			"Celtic", "Bluegrass", "Avantgarde", "Gothic Rock",
			"Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock",
			"Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour",
			"Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony",
			"Booty Brass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club",
			"Tango", "Samba", "Folklore", "Ballad", "Power Ballad",
			"Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo",
			"A Capela", "Euro-House", "Dance Hall", "Goa", "Drum & Bass",
			"Club-House", "Hardcore", "Terror", "Indie", "BritPop", "Negerpunk",
			"Polsk Punk", "Beat", "Christian Gangsta Rap", "Heavy Metal",
			"Black Metal", "Crossover", "Contemporary Christian",
			"Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime",
			"JPop", "SynthPop"												
	};


	public RepositorioMusica(String diretorio){
		this.diretorio = new File(diretorio);
		this.nomeDiretorio = diretorio;
	}

	public Vector<Mp3> gerarLista() throws UnsupportedAudioFileException, IOException{
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
			String a = (String) mapa.get("mp3.id3tag.genre");
			if(a != null && a.charAt(0)=='('&& a.charAt(a.length()-1)==')'){
				String semparen = a.substring(1,a.length()-1);
				int numerogenero = Integer.parseInt(semparen); 
				temp.setGenero(id3v1genres[numerogenero]);
			}else{
				temp.setGenero(a);
			}
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
		String a = (String) mapa.get("mp3.id3tag.genre");
		if(a.charAt(0)=='('&& a.charAt(a.length()-1)==')'){
			String semparen = a.substring(1,a.length()-1);
			int numerogenero = Integer.parseInt(semparen); 
			temp.setGenero(id3v1genres[numerogenero]);
		}else{
			temp.setGenero(a);
		}
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
