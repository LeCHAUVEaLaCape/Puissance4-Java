package cliPuissance4;

import java.io.*;
public class ServHandler implements Runnable {
	private volatile BufferedReader in;
	private volatile PrintWriter out;
	private BufferedReader clavier;
	public ServHandler(BufferedReader in,PrintWriter out,BufferedReader clavier) {
		this.in =in;
		this.out=out;
		this.clavier=clavier;
	}
	@Override
	public void run() {
		
		try {
			while(true) {
				String reponseServer = in.readLine();
				if(reponseServer!=null) {
					if(reponseServer.equals("!fin")) {
						System.out.println("Fin de la partie.\n  Taper 'game' pour chercher une partie.\n  Taper 'pseudo' pour changer de pseudo.\n  Taper 'quitter' pour quitter le client.");
						
					}else if(reponseServer.equals("!column")) {
						String column = clavier.readLine();
						out.println(column);
					}
					else {
						System.out.println(reponseServer);						
					}
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
