package CORE;

public class Mp3 {

	private String nome;
	private String autor;
	private String genero;
	private String album;
	private String tamanho;
	private String tempo;
	private String titulo;

	public Mp3(String nome, String autor, String genero, String album, String tamanho, String tempo, String titulo){
		this.nome = nome;
		this.autor = autor;
		this.album = album;
		this.genero = genero;
		this.tamanho = tamanho;
		this.tempo = tempo;
		this.titulo = titulo;
	}

	public Mp3(){

	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getTamanho() {
		return tamanho;
	}

	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}

	public String getTempo() {
		return tempo;
	}

	public void setTempo(String tempo) {
		this.tempo = tempo;
	}

	public String toString() {
		return "Mp3 [nome=" + nome + ", autor=" + autor + ", genero=" + genero
				+ ", album=" + album + ", tamanho=" + tamanho + ", tempo="
				+ tempo + ", titulo=" + titulo + "]";
	}

}
