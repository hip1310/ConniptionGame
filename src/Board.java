/*Conniption Game
 * Artificial Intelligence - Group Project
 * Harsh Patel - Amir Khadivi - Kifah Issa
 */

import java.util.ArrayList;
import java.util.Stack;


public class Board {
	public final static int rows = 6;
	public final static int columns = 7;
	public static char board[][] = new char[rows][columns];
	private static char CHECKER0 = 'X';     // Indicate the first player's checker
	private static char CHECKER1 = 'O';     // Indicate second player's checker
	private static char [] CHECKERS = {CHECKER0, CHECKER1};
	public static ArrayList<Character> winners = new ArrayList<Character>();
	public static boolean checkTie = false;
	
	
	/** empty constructor that intializes board
	 *  and displays the empty board
	 */
	public Board() {
		initializeBoard();
		displayBoard();
	}
	
	/** this method initializes an 
	 * empty board
	 */
	public void initializeBoard() {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				board[i][j] = ' ';
	}
	
	/** displays the board
	 * 
	 */
	public static void displayBoard() {
		System.out.println("1  2  3  4  5  6  7");
		for (int j = 0; j < rows; j++) {
			for (int k = 0; k < columns; k++)
				System.out.print("[" + board[j][k] + "]");
			System.out.println();
		}
		System.out.println("---------------------------------------------------");
	}
	
	/**
	 * Inserts chip at the appropriate location in the column 
	 * @param column
	 */
	public void insertChip(int column) {
			for (int i = rows-1; i >= 0; i--){
				if (board[i][column] == ' ') {
					board[i][column] = CHECKERS[Player.getCurrentPlayer()];
					break;
				}
			}
	}
	
	/** 
	 * adds winners based on the state of the board to the winners arraylist (does not return anything)
	 */
	public void getWinner() {
		//check for win horizontally
		for (int row=0; row<rows; row++){ 
		    for (int col=0; col<columns-3; col++){
		    	if (board[row][col] != ' ' &&
		    		board[row][col] == board[row][col+1] &&  
					board[row][col] == board[row][col+2] && 
					board[row][col] == board[row][col+3])
		    		
					winners.add(board[row][col]);
		    }
		}   
		//check for win vertically
		for (int row = 0; row < rows-3; row++){
		    for (int col = 0; col < columns; col++){
				if (board[row][col] != ' ' &&
					board[row][col] == board[row+1][col] &&
					board[row][col] == board[row+2][col] &&
					board[row][col] == board[row+3][col])
					winners.add(board[row][col]);
		    }	
		}		
		//check for win diagonally (upper left to lower right)
		for (int row = 0; row < rows-3; row++){ 
		    for (int col = 0; col < columns-3; col++){ 
				if (board[row][col] != ' ' &&
					board[row][col] == board[row+1][col+1] &&
					board[row][col] == board[row+2][col+2] &&
					board[row][col] == board[row+3][col+3]) 
					winners.add(board[row][col]);
		    }
		}   
		//check for win diagonally (lower left to upper right)
		for (int row = 3; row < rows; row++){ 
		    for (int col = 0; col < columns-3; col++){ 
				if (board[row][col] != ' ' &&
					board[row][col] == board[row-1][col+1] &&
					board[row][col] == board[row-2][col+2] &&
					board[row][col] == board[row-3][col+3])
					winners.add(board[row][col]);
		    }
		}
	}
	
	/**determines whos a winner and adds it to the winners arraylist
	 * 
	 * @return boolean true if there is a winner returns true if there is a tie
	 *
	 */
	public boolean evaluateBoard(){
		winners.clear();
		getWinner();
		
		if(!winners.isEmpty()){
			return true;
		}
		//Checking for board is full or not so that noone can win 
		boolean checkTie = isTie();
		
		return checkTie;	
	}
	
	
	/**
	* checks for a tie for the case when the board is actually full and noone has one
	* @return
	*/
	public boolean isTie() {
		for (int j = 0; j < columns; j++)
				if (board[0][j] == ' ')
					return false;
		
		checkTie = true;
		return true;
	}
	
	/**
	 * performs a flip of the board
	 */
	public void performFlip(){
    	for (int col = 0; col < columns; col++) {
           Stack<Character> temp = new Stack<Character>();
            for (int row = rows -1; row >= 0; row--){
            	if(board[row][col] == ' ')
            		break;
            	temp.push(board[row][col]);
            }
           int cRow = rows-1; 
           while(!temp.isEmpty()){
        	   	board[cRow][col] = temp.pop();
        	   	cRow--;
           }               
        }
    }
	
	/** determines if the move is legal
	 * 
	 * @param column
	 * @return a boolean 
	 */
	public static boolean isLegalMove(int column) {
		if (column > 6 || column < 0 || board[0][column] != ' ')
			return false;
		return true;
	}
	
	/**
	 * prints the result
	 */
	public void getResult(){
		if(!winners.isEmpty()){
			if(winners.contains(CHECKERS[0])){
					System.out.println(Player.players[0] + " Win!");
			}
			
			if(winners.contains(CHECKERS[1])){
				System.out.println(Player.players[1] + " Win!");
			}
		}
		else{
			if(checkTie == true){
				System.out.println("Match Draw");
			}
		}
	}
	
	//get results for AI vs. AI
	public int getResultAI(){
		if(!winners.isEmpty()){
			if(winners.contains(CHECKERS[0])){
					return 0;
			}
			
			if(winners.contains(CHECKERS[1])){
					return 1;
			}
		}
		else{
			if(checkTie == true){
					return 4;
			}
		}
		
		return 4;
	}
	

	/**
	 * sets the state of the board by setting board to an array
	 */
	public void setBoardstate(char [][] board){
		Board.board=board;
	}
	
	public void changeCheckers(){
		CHECKERS[0] = CHECKER1;
		CHECKERS[1] = CHECKER0;
	}
	
	public char getChecker(int player){
		return CHECKERS[player];
	}
	
}
