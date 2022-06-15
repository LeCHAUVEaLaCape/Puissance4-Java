package servPuissance4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PuissanceQuatre {
	enum Couleur {
		VIDE, ROUGE, JAUNE;

		public char getCouleur() {
			switch (this) {
			case ROUGE:
				return 'R';
			case JAUNE:
				return 'J';
			default:
				return ' ';
			}
		}
		
	}
	private static final byte NBR_RANGEES = 6;
	private static final byte NBR_COLONNES = 7;
	private static final byte NBR_DIRECTION = 4;
	
	private ClientHandler joueur1;
	private ClientHandler joueur2;
	
	// array of incremental values for each direction
	private static byte directions[][] = {
			// x, y
			{ -1, 1 }, // NW
			{  0, 1 }, // N
			{  1, 1 }, // NE
			{  1, 0 }  // E
	};
	
	// 2d array to represent plateau
	private Couleur plateau[][];
	
	// possible directions of finding a match at each grid square
	private List<byte[]> possibleDirs[][];
	
	// CONSTRUCTEUR
	public PuissanceQuatre(ClientHandler joueur1,ClientHandler joueur2) {
		this.joueur1=joueur1;
		this.joueur2=joueur2;
	}
	
	@SuppressWarnings("unchecked")
	public void init() {
		// instantiate arrays
		plateau = new Couleur[NBR_RANGEES][NBR_COLONNES];
		possibleDirs = new List[NBR_RANGEES][NBR_COLONNES];
		
		for (byte r = 0; r < NBR_RANGEES; r++) {
			for (byte c = 0; c < NBR_COLONNES; c++) {
				plateau[r][c] = Couleur.VIDE;
				
				// setup possible directions
				possibleDirs[r][c] = new ArrayList<byte[]>();
				for (byte i = 0; i < NBR_DIRECTION; i++) {
					// determine if space to have 4 consecutive pieces (3 further)
					if (
							c + 3 * directions[i][0] >= 0 &&
							c + 3 * directions[i][0] <= 6 &&
							r + 3 * directions[i][1] >= 0 &&
							r + 3 * directions[i][1] <= 5
							) {
						possibleDirs[r][c].add(directions[i]);
					}
						
				}
			}
		}
	}
	
	// prints out the plateau
	public void printPlateau() {
		/*
		 * +-+-+-+-+-+-+-+
		 * | | | | | | | |
		 * +-+-+-+-+-+-+-+
		 */
		String result= "";
		
		String rangee = "+-+-+-+-+-+-+-+";
		
		result+="\n=================\n\n";
		result+=rangee+"\n";
		for (byte r = NBR_RANGEES - 1; r >= 0; r--) {
			result+="|";
			for (byte c = 0; c < NBR_COLONNES; c++) {
				result+=plateau[r][c].getCouleur() + "|";
			}
			result+="\n" + rangee+"\n";
		}
		result+="\n\n=================\n\n";
		joueur1.out.println(result);
		joueur2.out.println(result);
	}
	
	public void play() {
		byte tour = 0;
		boolean running = true;
		
		// Ne peut pas depasser 42 tours car plateau rempli
		while (tour < NBR_RANGEES * NBR_COLONNES && running) {
			
			Couleur currentCouleur = (tour & 1) == 1 ? Couleur.ROUGE : Couleur.JAUNE;
			ClientHandler currentJoueur =(tour & 1) == 1 ? joueur1 : joueur2;
			byte col = 0;
		
			while (true) {
				// get input
				currentJoueur.out.println("\nPlayer " +currentJoueur.pseudo+"("+ currentCouleur + ") enter the column (1-7): ");
				currentJoueur.out.println("!column");
				String in = null;
				try {
					in = currentJoueur.in.readLine();
				} catch (IOException e1) {
					e1.printStackTrace();
					currentJoueur.close();
					return;
				}
				
				try {
					col = Byte.parseByte(in);
				} catch (Exception e) {
					currentJoueur.out.println("Please input a valid digit (1-7).");
					continue;
				}
				
				if (insert(currentCouleur, (byte)(col - 1))) {
					break;
				}
				
				currentJoueur.out.println("Please input a valid number (1-7).");
			}
			
			printPlateau();
			
			// check for winner
			boolean gagnant = false;
			for (byte r = 0; r < NBR_RANGEES; r++) {
				for (byte c = 0; c < NBR_COLONNES; c++) {
					// determine if spot should be checked
					// don't check VIDE or other Couleur because cannot win if not its tour
					if (plateau[r][c] == currentCouleur) {
						// iterate through each possible direction
						for (byte i = 0; i < possibleDirs[r][c].size(); i++) {
							// determine if connection of >= 4 pieces
							if (countConsecutive(r, c, possibleDirs[r][c].get(i), currentCouleur) >= 4) {
								gagnant = true;
								break;
							}
						}
					}
				}
				
				if (gagnant)  {
					break;
				}
			}
			
			// stop loop
			if (gagnant) {
				joueur1.out.println("Player " +currentJoueur.pseudo+"("+ currentCouleur + ") wins!");
				joueur2.out.println("Player " +currentJoueur.pseudo+"("+ currentCouleur + ") wins!");
				running = false;
				break;
			}
			
			// increment tour
			tour++;
		}
		
		if (running) {
			// winner not found
			joueur1.out.println("Nobody won :(");
			joueur2.out.println("Nobody won :(");
		}
	}
	// inserts a piece of the Couleur into the column
		public boolean insert(Couleur Couleur, byte col) {
			if (col < 0 || col >= NBR_COLONNES) {
				// out of bounds
				return false;
			}

			byte r;
			for (r = NBR_RANGEES - 1; r >= 0; r--) {
				if (plateau[r][col] != Couleur.VIDE) {
					// insert above
					r++;
					break;
				}
			}

			if (r == NBR_RANGEES) {
				// piece present in top of column
				return false;
			} else if (r == -1) {
				// no pieces in column, insert at bottom
				r++;
			}

			// set value
			plateau[r][col] = Couleur;

			return true;
		}

		// counts consecutive pieces starting at the spot
		public byte countConsecutive(byte row, byte col, byte dir[], Couleur Couleur) {
			// out of bounds check
			if (row < 0 || row >= NBR_RANGEES || col < 0 || col >= NBR_COLONNES) {
				return 0;
			}

			// Couleur match check
			if (plateau[row][col] != Couleur) {
				return 0;
			}

			// match found, add to string
			return (byte) (1 + countConsecutive((byte) (row + dir[1]), // increment row
					(byte) (col + dir[0]), // increment column
					dir, // pass in same direction
					Couleur));
		}
}