package COMUNICACAO;

import java.io.File;
import java.net.InetAddress;

public class retorno {
	InetAddress IPfont;
	int portFont;
	File filenam;
	
	public retorno(InetAddress IPfonte, int portFonte, File filename){
		IPfont = IPfonte;
		portFont = portFonte;
		filenam = filename;
	}

	public InetAddress getIPfont() {
		return IPfont;
	}

	public void setIPfont(InetAddress iPfont) {
		IPfont = iPfont;
	}

	public int getPortFont() {
		return portFont;
	}

	public void setPortFont(int portFont) {
		this.portFont = portFont;
	}

	public File getFilenam() {
		return filenam;
	}

	public void setFilenam(File filenam) {
		this.filenam = filenam;
	}
	
	
	
	
}
