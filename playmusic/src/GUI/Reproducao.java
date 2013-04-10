package GUI;

import CORE.Mp3;
import GUI.Gui;

public class Reproducao extends Thread {
	double tempo;
	String t; 
	int index;

	public void run() {
		System.out.println("ENTROU");
		if(Gui.tre){
			System.out.println("BOO");
			t = ((Mp3)Gui.table.getSelectionModel().getSelectedItem()).getTempo();
			System.out.println(t);
			index = 0;
			for( ; t.charAt(index) != ':' ; index++ ){}
			tempo = Double.parseDouble(t.substring(0, index)) * 60 + Double.parseDouble(t.substring(0, index)) ;	
			while(Gui.i.Microseconds() <= tempo){
				System.out.println("AEEEEEE");
				System.out.println("REPREPPREPRPEPREP"+Gui.i.Microseconds());
				Gui.a.set(Gui.i.Microseconds());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
}
