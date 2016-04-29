/*Conniption Game
 * Artificial Intelligence - Group Project
 * Harsh Patel
 */

import java.util.Arrays;
import java.util.Scanner;

public class Player {
	public static String[] players = new String[2];
	private static int currentPlayer = 0;
	private static int[] currentTurn = new int[3];
	private  boolean flipPossible = true;
	private int[] maxFlips = new int[]{4,4};
	
	/** Stores players into an array of players called "players"
	 * 
	 * @param player1
	 * @param player2
	 */
	public void storePlayers(String player1, String player2){
		players[0] = player1;
		players[1] = player2;
	}
	
	/** Gets the move the human player chooses
	 * prints menu
	 *   System.out.println(playerName + "'s turn:");
	 *	System.out.println("Flip            8");
	 *	System.out.println("Choose Column   1-7");
	 *	System.out.println("Finish the Turn 0");
	 * @param moveNum
	 * @return
	 */
	public int getMove(int moveNum){
		Scanner column;
		int choose;
		
		String playerName = players[currentPlayer];
		
		System.out.println(playerName + "'s turn:");
		System.out.println("Flip            8");
		System.out.println("Choose Column   1-7");
		System.out.println("Finish the Turn 0");
		
		column = new Scanner(System.in);
		
		// validity checks
		while(true) {
		    if(!column.hasNextInt()){ 
		    	
		    	Board.displayBoard();
		        System.out.println("Integers from 0 to 8 allowed."); 
		        System.out.println("Flip            8");
				System.out.println("Choose Column   1-7");
				System.out.println("Finish the Turn 0");
		        column.next(); // discard
		        continue;
		    } 
		    choose = column.nextInt();
		    if( (choose < 0) || (choose > 8) ) {
				System.out.println("Invalid Move - Please try again");
				System.out.println("Flip            8");
				System.out.println("Choose Column   1-7");
				System.out.println("Finish the Turn 0");
		        continue;
		    }
		    if(moveNum == 0){
			    if(choose == 0){
			    	System.out.println("You must have to put a chip or flip before you finish your turn.");
			    	System.out.println("Enter your choice : ");
			    	continue;
			    }
			    else if(choose == 8){
			                        
			    	if(maxFlips[currentPlayer] > 0){
				    		if(flipPossible == true){
					    		currentTurn[moveNum] = 8;
					    		flipPossible = false;
					    		maxFlips[currentPlayer]--;
				    		}
				    		else{
				    			System.out.println("No Flip - Another player just did Flip");
				    			System.out.println("Enter your choice : ");
				    			continue;
				    		}
			    	}
			    	else{
			    		System.out.println("Maximum Flip");
		    			System.out.println("Enter another choice : ");
		    			continue;
			    	}
			    }	
			    else{
			    		boolean isValidMove = Board.isLegalMove(choose-1);
					    if(!isValidMove){
							System.out.println("This column is Full. Choose another column :");
							continue;
						}
			    		currentTurn[moveNum] = 1;
			    		flipPossible = true;
			    }
			}
		   
		    if(moveNum == 1){
		    	if(choose == 0){
		    		Arrays.fill(currentTurn, 0);
		    		break;
		    	}
		    	else if(choose == 8){
		    		if(maxFlips[currentPlayer] > 0){
				    		if(flipPossible){
				    			currentTurn[moveNum] = 8;
				    			flipPossible = false;
				    			maxFlips[currentPlayer]--;
				    		}
				    		else{
				    			System.out.println("You can't do two continuous Flip in single turn.");
				    			System.out.println("Enter your choice : ");
				    			continue;
				    		}
		    		}		
		    		else{
			    		System.out.println("Maximum Flip");
		    			System.out.println("Enter another choice : ");
		    			continue;
			    	}
		    	}
		    	else{
		    		if(currentTurn[0] == 1){
		    			System.out.println("You can either Flip or Finish your turn");
		    			System.out.println("Enter your choice : ");
		    			continue;
		    		}
		    		else{
		    			boolean isValidMove = Board.isLegalMove(choose-1);
					    if(!isValidMove){
							System.out.println("This column is Full. Choose another column :");
							continue;
						}
			    		currentTurn[moveNum] = 1;
			    		flipPossible = true;
		    		}
		    	}
		    }
			 
		    if(moveNum == 2){
		    	if(choose == 0){
		    		Arrays.fill(currentTurn, 0);
		    		break;
		    	}
		    	else if(choose == 8){
		    		if(maxFlips[currentPlayer] > 1){
				    		if(flipPossible){
				    			currentTurn[moveNum] = 8;
				    			flipPossible = false;
				    			maxFlips[currentPlayer]--;
				    		}
				    		else{
				    			System.out.println("You can't do two continuous Flip in single turn.");
				    			System.out.println("Enter your choice : ");
				    			continue;
				    		}
		    		}
		    		else{
			    		System.out.println("Maximum Flip");
		    			System.out.println("Enter another choice : ");
		    			continue;
			    	}
		    	}
		    	else{
		    		System.out.println("You can either Flip or Finish your turn");
	    			System.out.println("Enter your choice : ");
	    			continue;
		    	}
		    }		    
		    break;
		}
		
		return choose;
	}
	
	/** This chooses which player is playing
	 * 
	 */
	public void flipPlayer(){
			if(currentPlayer == 0) currentPlayer = 1;
			else currentPlayer = 0;
	}
	
	/**returns the player that is currently playing 
	 * 
	 * @return 0 or 1 (currentplayer)
	 */
	public static int getCurrentPlayer(){
		return currentPlayer;
	}
	
	
	/**
	 * clears the menu takes no parameter. 
	 */
	public final static void clearConsole()
	{
		for(int clear = 0; clear < 50; clear++) {
		    System.out.println("\n") ;
		}

	}
	
	
	/**
	 * returns number of flips for human player
	 *
	 */
	public int numflips(){
		return maxFlips[currentPlayer];
	}
	
	/**
	 * Sets the flippossible for human to false because computer chose a flip move 
	 */
	public void flippossible(boolean example){
	   flipPossible=example;
	}

	public void changeMaxFlips(int flipDeduction){
		maxFlips[currentPlayer] = maxFlips[currentPlayer] - flipDeduction;
	}
}
