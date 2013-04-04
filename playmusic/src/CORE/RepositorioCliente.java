package CORE;

import java.util.Vector;

public class RepositorioCliente {

	private Vector<ClienteConectado> listaCliente;

	public RepositorioCliente(){
		
	}

	public void adicionarCliente(String IP, String porta){
		int indice = -1;
		boolean encontrou = false;
		for (int i = 0; i < listaCliente.size() && !encontrou; i++){
			if (IP.equals(listaCliente.get(i).getIP())){
				encontrou = true;
				indice = i;
			}
		}
		if (indice == -1){
			ClienteConectado c = new ClienteConectado(IP, porta);
			listaCliente.addElement(c);
		}else{
			listaCliente.get(indice).adicionarPorta(porta);
		}
	}

	public void removerCliente(String IP, String porta){
		int indice;
		boolean encontrou = false;
		for (int i = 0; i < listaCliente.size() && !encontrou; i++){
			if (IP.equals(listaCliente.get(i).getIP())){
				encontrou = true;
				indice = i;
				listaCliente.get(i).removerPorta(porta);
				if (listaCliente.get(i).getPortas().isEmpty()){
					listaCliente.remove(indice);
				}
			}
		}
	}
}
