/*Conniption Game
 * Artificial Intelligence - Group Project
 * Harsh Patel - Amir Khadivi - Kifah Issa
 */
import java.util.ArrayList;
import java.util.Scanner;

public class ConniptionMain {
	static int countWin1 = 0;
	static int countWin2 = 0;
	
	public static void main(String [] args){
		Scanner input1;
		int gameVersion;
		
		System.out.println("Hello! Welcome to the game CONNIPTION!!");
		System.out.println("Choose from the menu: ");
		System.out.println("Enter 1 for Human Vs. Human ");
		System.out.println("Enter 2 for Human Vs. AI");
		System.out.println("Enter 3 for AI Vs. AI");
		
		input1 = new Scanner(System.in);
		
		while(true){
			if(!input1.hasNextInt()){
				System.out.println("Invalid Input");
				System.out.println("Enter 1 for Human Vs. Human ");
				System.out.println("Enter 2 for Human Vs. AI");
				System.out.println("Enter 3 for AI Vs. AI");
				
				input1.next();
				continue;
			}
			
			gameVersion = input1.nextInt();
			
			if(gameVersion < 1 || gameVersion > 3){
				System.out.println("Invalid Input");
				System.out.println("Enter 1 for Human Vs. Human ");
				System.out.println("Enter 2 for Human Vs. AI");
				System.out.println("Enter 3 for AI Vs. AI");
				continue;
			}
			
			break;
		}
			
		Scanner input2 = new Scanner(System.in);
		Player pl = new Player();
		
		String player1 = "";
		String player2 = ""; 
		
		if(gameVersion == 1){
			
			System.out.println("Player1 starts the game with chip 'X'");
			System.out.println("Enter the name of Player1");
			player1 = input2.nextLine();
			
			System.out.println("Player2 uses chip 'O'");
			System.out.println("Enter the name of Player2");
			player2 = input2.nextLine();
			
			pl.storePlayers(player1, player2);
			humanvshuman(pl);
		}
		else if(gameVersion == 2){
			
			System.out.println("Do you want to play first? yes/no");
			String playFirst;
			while(true){
				
				playFirst = input2.nextLine();

				if(playFirst.equalsIgnoreCase("yes")){
						break;
				}
				else if(playFirst.equalsIgnoreCase("no")){
					break;
				}
				else{
					System.out.println("Invalid Input");
					System.out.println("Do you want to play first? yes/no");
				}				
			}
			
			if(playFirst.equalsIgnoreCase("yes")){
				System.out.println("Player1 starts the game with chip 'X'");
				System.out.println("Enter the name of Player1");
				player1 = input2.nextLine();
				pl.storePlayers(player1, "AI");
			}
			else{
				System.out.println("Player2 uses chip 'X'");
				System.out.println("Enter the name of Player2");
				player2 = input2.nextLine();
				pl.storePlayers("AI", player2);
			}
			
			humanvsai(pl);
		}
		else if(gameVersion == 3){
			pl.storePlayers("AI-1", "AI-2");
			aivsai(pl);
		}
				
	}
		
//This is the human vs human game if the user decides to choose this option. 
		public static void humanvshuman(Player pl){
		Scanner input = new Scanner(System.in);
		Board state = new Board();
		
		do{
			int count = 0;
			while(count < 3){
				int move = pl.getMove(count); //displays the menu 
				
					if(move == 8){
						state.performFlip(); //flips the board
						state.displayBoard(); //then display board
					}
					else if(move == 0){  //0 is for finishing the turns 
						break; 
					}
					else{
						state.insertChip(move-1); //inserts a chip into the right column 
						state.displayBoard();    //displays the state of the board 
					}
			   count++;
			}
			pl.flipPlayer(); //change the player that is playing 
			state.displayBoard(); 
		}while(!state.evaluateBoard());
		state.getResult(); //get result of the game 
	}
				
//This is the Human vs. AI option which calls  minimxalg class to optimize  AI's ability vs. human
	public static void humanvsai(Player pl){
		ArrayList<Integer> humanmoves = null;
		Board state = new Board();
		
		if((pl.players[0]).equals("AI")){
			state.changeCheckers();
		}
		//minimax class constructor
		minimaxalg start = new minimaxalg();
		do{
			if((pl.players[pl.getCurrentPlayer()]).equals("AI")){
				//*****Need code here for where the computer moves****
					node newstate=start.minimaxalgorithm(state.board, humanmoves,pl.numflips());  //eventually will return a boardstate
					state.setBoardstate(newstate.getBoardstate());
					Board.displayBoard(); 
					
					if(newstate.getActionmenu()=="f" || newstate.getActionmenu()=="fcf" || newstate.getActionmenu()=="cf"){
			            pl.flippossible(false);
			            System.out.println("computer's move choice: " + newstate.getActionmenu());
					}
					else{
						pl.flippossible(true);
					}
			}
			else{
					int count = 0;
					int move;
					humanmoves = new ArrayList<Integer>(); // I need this arraylist which instantiates every turn of the do while to know what moves the AI is capable of when i run minimax
					while(count < 3){
						    move = pl.getMove(count); //displays the menu 			
							if(move == 8){
								state.performFlip(); //flips the board
								 humanmoves.add(move);
								Board.displayBoard(); //then display board
							}
							else if(move == 0){  //0 is for finishing the turns 
								 humanmoves.add(move);
								break; 
							}
							else{
								state.insertChip(move-1); //inserts a chip into the right column 
								 humanmoves.add(move);
								Board.displayBoard();    //displays the state of the board 
							}
					   count++;
					}
			}
			pl.flipPlayer(); //change the player that is playing
		
		}while(!state.evaluateBoard());
		state.getResult(); //get result of the game 
	}
	

    public static void aivsai(Player pl){
    	int totalGames = 100;
    	for(int i = 0; i < totalGames; i++){
    	
			    	Board state = new Board();
			    	
			    	//minimax class constructor
			    	minimaxalg2 start = new minimaxalg2();
			    	String choice = null;
			    	
			    	do{
							//*****Need code here for where the computer moves****
						node newstate=start.minimaxalgorithm(state.board, choice, pl.numflips(), state.getChecker(pl.getCurrentPlayer()));  //eventually will return a boardstate
						state.setBoardstate(newstate.getBoardstate());
						
						choice = newstate.getActionmenu();
						System.out.println(pl.players[pl.getCurrentPlayer()] + "'s move : " + choice);
						
						if(choice == "f" || choice == "cf" || choice == "fc"){
					    	pl.changeMaxFlips(1);
					    }
					    else if(choice == "fcf"){
					    	pl.changeMaxFlips(2);
					    }
						
						System.out.println(pl.players[pl.getCurrentPlayer()] + "'s remaining Flips: " + pl.numflips());
						
						Board.displayBoard(); 
			     		pl.flipPlayer(); //change the player that is playing
					
					}while(!state.evaluateBoard());
			    state.getResult();	
				int result = state.getResultAI(); //get result of the game
					
				if(result == 0) countWin1++;
				if(result == 1) countWin2++;
    	}
    	
    	System.out.println("AI-1 : " + countWin1 + "Percentage : " + (countWin1 * 100)/totalGames);
    	System.out.println("AI-2 : " + countWin2 + "Percentage : " + (countWin2 * 100)/totalGames);
    	
    }
    	
}

