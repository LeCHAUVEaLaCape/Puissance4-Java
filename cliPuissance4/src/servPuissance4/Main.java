package servPuissance4;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Main {
	
//	BufferedReader clavier =new BufferedReader(new InputStreamReader(System.in));
	
	private static volatile ArrayList<ClientHandler> clients = new ArrayList<>();
	private static volatile ArrayList<Game> games = new ArrayList<>();
	private static ExecutorService pool = Executors.newFixedThreadPool(8);
	
	private static ServerSocket clientListener ;
	
	public static void main(String[] args) throws IOException {
		clientListener=new ServerSocket(4999);
		MatchMaker matchMakingListener =new MatchMaker(games,pool);
		// lancement du service de matchMaking
		pool.execute(matchMakingListener);
		
		while(true) {
			Socket client = clientListener.accept();
			ClientHandler clientThread = new ClientHandler(client,matchMakingListener);
			
			clients.add(clientThread);
			pool.execute(clientThread);
		}
	
	}
	
}
