package servPuissance4;

import java.net.*;
import java.io.*;


public class ClientHandler implements Runnable,AutoCloseable {
	private Socket client;
	public BufferedReader in;
	public PrintWriter out;
	public String pseudo;
	private MatchMaker matchMaking;
	private boolean isInGame;
	
	public ClientHandler(Socket clientSocket,MatchMaker matchMaking) throws IOException {
		this.client=clientSocket;
		this.matchMaking =matchMaking;
		this.in =new BufferedReader(new InputStreamReader(client.getInputStream()));
		this.out=new PrintWriter(client.getOutputStream(), true);
	}
	
	@Override
	public void run() {
		try {
			
			while(true) {
				if(!isInGame) {
					String request = in.readLine();
					// TODO verif si il est pas deja dans la file
					if(request.contains("game")) {
						matchMaking.addJoueur(this);
						out.println("recheche de partie...");
					}else if(request.equals("pseudo!")) {
						this.pseudo=in.readLine();
						out.println("Nouveau pseudo: "+this.pseudo);
					}else if(request.equals("quitter")) {
						close();
						return;
					}					
				}
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean isInGame() {
		return isInGame;
	}

	public void setIsInGame(boolean inGame) {
		this.isInGame = inGame;
	}

	@Override
	public void close() {
		try {
			client.close();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
