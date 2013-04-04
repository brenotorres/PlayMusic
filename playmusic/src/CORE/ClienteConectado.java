package CORE;

import java.util.Vector;

public class ClienteConectado {
	
	private String IP;
	private Vector<String> portas;
	
	public ClienteConectado(String IP, String porta){
		this.IP = IP;
		this.portas.addElement(porta);
	}
	
	public ClienteConectado(){
		
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String IP) {
		this.IP = IP;
	}

	public void adicionarPorta(String porta) {
		this.portas.addElement(porta);
	}
	
	public void removerPorta(String porta){
		int indice = getIndice(porta);
		if (indice != -1){
			portas.remove(indice);
		}
	}
	
	public Vector<String> getPortas() {
		return portas;
	}

	public int getIndice(String porta){
		int indice = -1;
		boolean encontrou = false;
		for (int i = 0; i < portas.size() && !encontrou; i++){
			if (porta.equals(portas.get(i))){
				encontrou = true;
				indice = i;
			}
		}
		return indice;
	}
}
