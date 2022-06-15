package cliPuissance4;

import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

public class Main {
	static Scanner scan = new Scanner(System.in);
	static String inputStr;
	static String pseudo;
	static BufferedReader clavier =new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws IOException{
		
		Socket socket= new Socket("localhost",4999);
		
		BufferedReader input =new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out =new PrintWriter(socket.getOutputStream(),true);
		ServHandler servListener =new ServHandler(input,out,clavier);
		ExecutorService pool = Executors.newFixedThreadPool(2);
		
		pool.execute(servListener);
		setPseudo(out);
		
		while(true) {
			String commande = clavier.readLine();
			if(commande.equals("quitter")) {
				out.println("quitter");
				break;
			}
			if(commande.equals("next")) continue;
			if(commande.equals("pseudo")) {
				setPseudo(out);
			}
			else {
				out.println(commande);				
			}
			
		}
		socket.close();
		input.close();
		out.close();
		pool.shutdownNow();
        Thread.currentThread().interrupt();
		System.exit(0);
	}
	// modifie le pseudo localement et sur le serveur
	public static void setPseudo(PrintWriter out) {
		System.out.println("Entrer votre nom pseudo:");
		try {
			pseudo=clavier.readLine();
			out.println("pseudo!");
			out.println(pseudo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
