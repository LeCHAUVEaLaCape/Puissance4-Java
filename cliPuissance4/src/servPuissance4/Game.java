package servPuissance4;

public class Game implements Runnable , AutoCloseable{
	public ClientHandler joueur1;
	public ClientHandler joueur2;
	private PuissanceQuatre partie;
	
	public Game(ClientHandler joueur1,ClientHandler joueur2) {
		this.joueur1=joueur1;
		this.joueur2=joueur2;
		this.partie=new PuissanceQuatre(joueur1,joueur2);
	}
	@Override
	public void run() {
		joueur1.out.println("PARTIE TROUVEE\n        Votre adversaire est "+joueur2.pseudo);
		joueur2.out.println("PARTIE TROUVEE\n        Votre adversaire est "+joueur1.pseudo);
		
		this.partie.init();
		this.partie.play();
		
		close();
		
	}
	@Override
	public void close() {
		joueur1.out.println("!fin");
		joueur2.out.println("!fin");
	}
}
