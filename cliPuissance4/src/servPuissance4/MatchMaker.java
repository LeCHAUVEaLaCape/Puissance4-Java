package servPuissance4;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class MatchMaker implements Runnable {
	private volatile ArrayList<Game> games;
	private static volatile ArrayList<ClientHandler> joueurs = new ArrayList<>();
	private ExecutorService pool;
	
	public MatchMaker(ArrayList<Game> games,ExecutorService pool) {
		this.games=games;
		this.pool =pool;
	}
	
	public void addJoueur(ClientHandler client) {
		joueurs.add(client);
	}
	
	public int readJoueurs() {
		return joueurs.size();
	}
	
	@Override
	public void run() {
		System.out.println("thread is running");
		while(true) {
			if(readJoueurs()>1) {
				ClientHandler joueur1=joueurs.remove(0);
				ClientHandler joueur2=joueurs.remove(0);
				joueur1.setIsInGame(true);
				joueur2.setIsInGame(true);
				Game partie= new Game(joueur1, joueur2);
				games.add(partie);
				pool.execute(partie);
			}
			
		}
	}
	
}
