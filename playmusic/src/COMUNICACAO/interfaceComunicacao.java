package COMUNICACAO;


public interface interfaceComunicacao {

	void iniciarDownload(String arquivo);
	
	void pausarDownload(String arquivo);//qual referência deve ser passada para saber qual download pausar?
	
	void reiniciarDownload(String arquivo); //mesma duvida acima
	
	void continuarDownload(String arquivo); //mesma duvida acima
	
	String getIPServidor();
	
	String getNomeServidor();//o prof menciona poder mudar nome e IP do servidor, não sei bem como vai ser isso de "nome"
	
	void setIPServidor(String novoIP);
	
	void setNomeServidor(String novoNome);
	
	void setTaxaPerda(int taxaPerda); //taxa de perda de pacotes escolhida pelo usuário
	
	int getTamanhoArquivo();//retorna o tamanho do arquivo. Qual seria o parâmetro?
	
	int getBytesRecebidos(); //retorna a qnt de bytes recebidos no pacote. será usado para calcular % de download e TED
}
