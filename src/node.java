/*Conniption Game
 * Artificial Intelligence - Group Project
 * Harsh Patel
 */

import java.util.ArrayList;

//the node class will be what we use when creating children based on a set of actions. It is used to represent
// the state of a game before and after a move as well and number of flip coins have been used by the players max and min each
//in our game min is the Human and Max is the AI 
//This can be made using Board and the Player class; 
import java.util.ArrayList;
import java.util.Stack;
public class node {
	private char[][] boardstate;
	private String actionmenu;	    // this is the action done that was used to create the node. This variable will help when doing all the actions to produce all child nodes. 
	private node parent = null;
	private String player;   //max or min node  
	private boolean haschild=false;
	 private int value;
	private ArrayList<node> children = new ArrayList<node>(); //this is the arraylist that holds all of the children of this node
	private static char curChecker;
	private static char oppChecker;
	private int flipPenalty = 0;

	private static int[][] evaluationTable ={{3, 4, 5, 7, 5, 4, 3}, 
											{4, 6, 8, 10, 8, 6, 4},
											{5, 8, 11, 13, 11, 8, 5}, 	
											{5, 8, 11, 13, 11, 8, 5},
											{4, 6, 8, 10, 8, 6, 4},
											{3, 4, 5, 7, 5, 4, 3}};
	
	/**constructor for the node class takes these variables
	 * @param boardstate-char array
	 * @param actionmenu-which action made the boardstate
	 * @param parent-a reference to the parent node
	 * @param child-a reference to the child node once an action has been made
	 * @param player-is this a max or min node (computer will represent "max" in minimax algorithm)
	 */
	public node(char[][] boardstate, String actionmenu,node parent, String player, int flippenalty){
		this.boardstate = boardstate;
		this.actionmenu = actionmenu;
		this.parent = parent;
		this.player = player;
		this.flipPenalty = flippenalty;
	}
	/**
	 * sets the action that made the boardstate
	 * @param actionnum
	 */
	public void setActionmenu(String actionnum){
		this.actionmenu = actionnum;
	
	}
	/**
	 * get the action from menu that was used to make this boardstate
	 * @return
	 */
	public String getActionmenu(){
		return actionmenu;
	}
	/**
	 * gets the parent of this node
	 * @return
	 */
	public node getParent(){
		return this.parent;
	}
	/**
	 * sets a reference to the parent of this node
	 * @param myparent
	 */
	public void setParent(node myparent){
		this.parent=myparent;
	}
	/**
	 * sets the board state by taking in a char[][]
	 * @param state
	 */
	public  void setBoardstate(char[][] state){
		this.boardstate=state;
	}
	/**
	 * returns a board state which is a char[][]
	 * @return
	 */
	public char [][] getBoardstate(){
		
		return this.boardstate;
	}
	
	/**
	 * returns how many flip done until this node starting from the root
	 * @return
	 */
	public int getFlipPenalty(){
		
		return this.flipPenalty;
	}
	
	public int hybrid1(char[][] board, char curchecker){
		
		this.curChecker = curchecker;
		
		if(curChecker == 'O') this.oppChecker = 'X';
		if(curChecker == 'X') this.oppChecker = 'O';
		
		int priority1 = 0;
		char winner = isGoal(board);
		if(winner != ' '){
			if(winner == curChecker) priority1 = 5;
			if(winner == oppChecker) priority1 = -5;
		}
		int priority2 = is3inRow(board);
		
		int priority3 = eval1(board, curChecker);
	
		return (2 * priority1) + priority2 + priority3;
		//return  (3 * priority1) + (2 * priority2);
	}
	
	public int hybrid2(char[][] board, char curchecker){
		
		this.curChecker = curchecker;
		
		if(curChecker == 'O') this.oppChecker = 'X';
		if(curChecker == 'X') this.oppChecker = 'O';
		
		int priority1 = 0;
		char winner = isGoal(board);
		if(winner != ' '){
            if(winner == 'O') priority1 = 1000;
            if(winner == 'X') priority1 = -1000;
		}
		
		int priority2 = is3inRow(board);
		int priority3=topstack();
		int priority4 = eval1(board, curChecker);
        
		return (priority1) + (priority2) + (priority3) + priority4;
	}
	
	public int hybrid3(char[][] board, char curchecker, int flipPenalty){
		
		this.curChecker = curchecker;
		
		if(curChecker == 'O') this.oppChecker = 'X';
		if(curChecker == 'X') this.oppChecker = 'O';
		
		int priority1 = 0;
		char winner = isGoal(board);
		if(winner != ' '){
            if(winner == 'O') priority1 = 400;
            if(winner == 'X') priority1 = -400;
		}
		
		int priority2 = is3inRow(board);
		int priority3 = topstack();
		int priority4 = eval1(board, curChecker);
    
		return (priority1) + (priority2) + (priority3) + priority4 - (3 * flipPenalty);
	
	}
	//using evalution table 
	public int eval1(char[][] board, char curchecker){
			
			this.curChecker = curchecker;
			
			if(curChecker == 'O') this.oppChecker = 'X';
			if(curChecker == 'X') this.oppChecker = 'O';
			
			
			int utility = 138;
			
			int sum = 0;
			for (int i = 0; i < 6; i++){
				for (int j = 0; j < 7; j++){
					if (board[i][j] == curChecker)
						sum += evaluationTable[i][j];
					else if (board[i][j] == oppChecker)
						sum -= evaluationTable[i][j];
				}	
			}
		
			return utility + sum;
			//return  (3 * priority1) + (2 * priority2);
	}
	
	//checking for winning (+ for current player - for opponent)
	public int eval2(char[][] board, char curchecker){
			
			this.curChecker = curchecker;
			
			if(curChecker == 'O') this.oppChecker = 'X';
			if(curChecker == 'X') this.oppChecker = 'O';
			
			int priority1 = 0;
			char winner = isGoal(board);
			if(winner != ' '){
				if(winner == curChecker) priority1 = 5;
				if(winner == oppChecker) priority1 = -5;
			}
			
			int priority2 = is3inRow(board);
			
			return (4 * priority1) + (3 * priority2);
			//return  (3 * priority1) + (2 * priority2);
	}
	
	/**
    top stack eval function
    **/
   public int topstack(){
       int rows = 6;
       int columns = 7;
       int numOattop=0;
       int numXattop=0;
       for (int col = 0; col < columns; col++) {
           Stack<Character> temp = new Stack<Character>();
           for (int row = rows-1; row >= 0; row--){
               if(this.boardstate[row][col] == ' ')
                   break;
               temp.push(this.boardstate[row][col]);
           }
           if(temp.size()>0 && temp.peek()=='O')
               numOattop++;
           else if(temp.size()>0 && temp.peek()=='X')
               numXattop++;
       }
       
       return 7*numOattop - 3*numXattop;
   }
	
	public char isGoal(char[][] board) {
		int rows = 6;
		int columns = 7;
		//check for win horizontally
		for (int row=0; row<rows; row++){ 
		    for (int col=0; col<columns-3; col++){
		    	if (board[row][col] != ' ' &&
		    		board[row][col] == board[row][col+1] &&  
					board[row][col] == board[row][col+2] && 
					board[row][col] == board[row][col+3]) 
					return board[row][col];
		    }
		}   
		//check for win vertically
		for (int row = 0; row < rows-3; row++){
		    for (int col = 0; col < columns; col++){
				if (board[row][col] != ' ' &&
					board[row][col] == board[row+1][col] &&
					board[row][col] == board[row+2][col] &&
					board[row][col] == board[row+3][col])
					return board[row][col];
		    }	
		}		
		//check for win diagonally (upper left to lower right)
		for (int row = 0; row < rows-3; row++) 
		    for (int col = 0; col < columns-3; col++) 
				if (board[row][col] != ' ' &&
					board[row][col] == board[row+1][col+1] &&
					board[row][col] == board[row+2][col+2] &&
					board[row][col] == board[row+3][col+3]) 
					return board[row][col];
		//check for win diagonally (lower left to upper right)
		for (int row = 3; row < rows; row++) 
		    for (int col = 0; col < columns-3; col++) 
				if (board[row][col] != ' ' &&
					board[row][col] == board[row-1][col+1] &&
					board[row][col] == board[row-2][col+2] &&
					board[row][col] == board[row-3][col+3])
					return board[row][col];
		return ' ';
	}
	
	//checking for 3 consecutive 'O' and 'X' and fourth one should be blank 
	//checking for 2 consecutive 'O' and 'X' and another two should be blank
	public int is3inRow(char[][] board) {
		int count3AI = 0;
		int count3Hu = 0;
		int count2AI = 0;
		int count2Hu = 0;
		int rows = 6;
		int columns = 7;
		
		//check for win horizontally
		for (int row=0; row<rows; row++){ 
		    for (int col=0; col<columns-3; col++){
		    	
		    	//3 in row and a blank
		    	if (board[row][col] != ' ' &&
		    		board[row][col] == board[row][col+1] &&  
					board[row][col] == board[row][col+2] && 
					board[row][col+3] == ' '){
		    		
					if((row-1) >= 0){
						if(board[row-1][col+3] != ' '){
							if(board[row][col] == curChecker) count3AI++;
							if(board[row][col] == oppChecker) count3Hu++;
						}
					}
					
					if((row-1) < 0){
						if(board[row][col] == curChecker) count3AI++;
						if(board[row][col] == oppChecker) count3Hu++;
					}
		    	}
		    	
		    	if (board[row][col+3] != ' ' &&
			    		board[row][col+3] == board[row][col+1] &&  
						board[row][col+3] == board[row][col+2] && 
						board[row][col] == ' '){
			    		
					if((row-1) >= 0){
						if(board[row-1][col] != ' '){
							if(board[row][col+3] == curChecker) count3AI++;
							if(board[row][col+3] == oppChecker) count3Hu++;
						}
					}
					
					if((row-1) < 0){
						if(board[row][col+3] == curChecker) count3AI++;
						if(board[row][col+3] == oppChecker) count3Hu++;
					}
			    }
		    	
		    	if (board[row][col] != ' ' &&
			    		board[row][col] == board[row][col+1] &&  
						board[row][col] == board[row][col+3] && 
						board[row][col+2] == ' '){
			    		
					if((row-1) >= 0){
						if(board[row-1][col+2] != ' '){
							if(board[row][col] == curChecker) count3AI++;
							if(board[row][col] == oppChecker) count3Hu++;
						}
					}
					
					if((row-1) < 0){
						if(board[row][col] == curChecker) count3AI++;
						if(board[row][col] == oppChecker) count3Hu++;
					}
			    }
		    	
		    	if (board[row][col] != ' ' &&
			    		board[row][col] == board[row][col+2] &&  
						board[row][col] == board[row][col+3] && 
						board[row][col+1] == ' '){
			    		
					if((row-1) >= 0){
						if(board[row-1][col+1] != ' '){
							if(board[row][col] == curChecker) count3AI++;
							if(board[row][col] == oppChecker) count3Hu++;
						}
					}
					
					if((row-1) < 0){
						if(board[row][col] == curChecker) count3AI++;
						if(board[row][col] == oppChecker) count3Hu++;
					}
			    }
		    	
		    	//2 in row and a blank
		    	if (board[row][col] != ' ' &&
			    		board[row][col] == board[row][col+1] &&  
						board[row][col+2] == ' ' && 
						board[row][col+3] == ' '){
			    		
					if((row-1) >= 0){
						if(board[row-1][col+2] != ' ' && board[row-1][col+3] != ' '){
							if(board[row][col] == curChecker) count2AI++;
							if(board[row][col] == oppChecker) count2Hu++;
						}
					}
					
					if((row-1) < 0){
						if(board[row][col] == curChecker) count2AI++;
						if(board[row][col] == oppChecker) count2Hu++;
					}
			    }
		    	
		    	if (board[row][col+3] != ' ' &&
			    		board[row][col+3] == board[row][col+2] &&  
						board[row][col+1] == ' ' && 
						board[row][col] == ' '){
			    		
					if((row-1) >= 0){
						if(board[row-1][col] != ' ' && board[row-1][col+1] != ' '){
							if(board[row][col+3] == curChecker) count2AI++;
							if(board[row][col+3] == oppChecker) count2Hu++;
						}
					}
					
					if((row-1) < 0){
						if(board[row][col+3] == curChecker) count2AI++;
						if(board[row][col+3] == oppChecker) count2Hu++;
					}
			    }
		    	
		    	if (board[row][col+1] != ' ' &&
			    		board[row][col+1] == board[row][col+2] &&  
						board[row][col+3] == ' ' && 
						board[row][col] == ' '){
			    		
					if((row-1) >= 0){
						if(board[row-1][col+3] != ' ' && board[row-1][col] != ' '){
							if(board[row][col+1] == curChecker) count2AI++;
							if(board[row][col+1] == oppChecker) count2Hu++;
						}
					}
					
					if((row-1) < 0){
						if(board[row][col+1] == curChecker) count2AI++;
						if(board[row][col+1] == oppChecker) count2Hu++;
					}
			    }
		    	
		    	if (board[row][col] != ' ' &&
			    		board[row][col] == board[row][col+2] &&  
						board[row][col+3] == ' ' && 
						board[row][col+1] == ' '){
			    		
					if((row-1) >= 0){
						if(board[row-1][col+3] != ' ' && board[row-1][col+1] != ' '){
							if(board[row][col] == curChecker) count2AI++;
							if(board[row][col] == oppChecker) count2Hu++;
						}
					}
					
					if((row-1) < 0){
						if(board[row][col] == curChecker) count2AI++;
						if(board[row][col] == oppChecker) count2Hu++;
					}
			    }
		    	
		    	if (board[row][col+3] != ' ' &&
			    		board[row][col+3] == board[row][col+1] &&  
						board[row][col] == ' ' && 
						board[row][col+2] == ' '){
			    		
					if((row-1) >= 0){
						if(board[row-1][col] != ' ' && board[row-1][col+2] != ' '){
							if(board[row][col+3] == curChecker) count2AI++;
							if(board[row][col+3] == oppChecker) count2Hu++;
						}
					}
					
					if((row-1) < 0){
						if(board[row][col+3] == curChecker) count2AI++;
						if(board[row][col+3] == oppChecker) count2Hu++;
					}
		    		 
		    	}
		    	
		    	if (board[row][col+3] != ' ' &&
			    		board[row][col+3] == board[row][col] &&  
						board[row][col+1] == ' ' && 
						board[row][col+2] == ' '){
			    		
					if((row-1) >= 0){
						if(board[row-1][col+1] != ' ' && board[row-1][col+2] != ' '){
							if(board[row][col+3] == curChecker) count2AI++;
							if(board[row][col+3] == oppChecker) count2Hu++;
						}
					}
					
					if((row-1) < 0){
						if(board[row][col+3] == curChecker) count2AI++;
						if(board[row][col+3] == oppChecker) count2Hu++;
					}
		    		 
		    	}
		    	
		 }   
		}    
		
		//check for win vertically
		for (int row = 0; row < rows-3; row++){
		    for (int col = 0; col < columns; col++){
		    	//count 3 in row
		    	if (board[row][col] != ' ' &&
						board[row][col] == board[row+1][col] &&
						board[row][col] == board[row+2][col] &&
						board[row+3][col] == ' '){
						
						if(board[row][col] == curChecker) count3AI++;
			    		if(board[row][col] == oppChecker) count3Hu++;				
				}
		   	
		    	//count 2 in row
		    	if (board[row][col] != ' ' &&
						board[row][col] == board[row+1][col] &&
						board[row+2][col] == ' ' &&
						board[row+3][col] == ' '){
						
						if(board[row][col] == curChecker) count2AI++;
			    		if(board[row][col] == oppChecker) count2Hu++;				
				}
		    	
		    }	
		 }		
		
		//check for win diagonally (upper left to lower right)
		for (int row = 0; row < rows-3; row++) {
		    for (int col = 0; col < columns-3; col++){
		    	//count 3 in row
				if (board[row][col] != ' ' &&
					board[row][col] == board[row+1][col+1] &&
					board[row][col] == board[row+2][col+2] &&
				    board[row+3][col+3] == ' ' &&
				    board[row+2][col+3] != ' ') {
					
				}
				
				if (board[row+3][col+3] != ' ' &&
						board[row+3][col+3] == board[row+1][col+1] &&
						board[row+3][col+3] == board[row+2][col+2] &&
					    board[row][col] == ' ') {
						
					if((row-1) >= 0){
						if(board[row-1][col] != ' '){
							if(board[row+3][col+3] == curChecker) count3AI++;
							if(board[row+3][col+3] == oppChecker) count3Hu++;
						}
					}
					
					if((row-1) < 0){
						if(board[row+3][col+3] == curChecker) count3AI++;
						if(board[row+3][col+3] == oppChecker) count3Hu++;
					}
				}
				
				if (board[row][col] != ' ' &&
						board[row][col] == board[row+2][col+2] &&
						board[row][col] == board[row+3][col+3] &&
					    board[row+1][col+1] == ' ' &&
					    board[row][col+1] != ' ') {
						
						if(board[row][col] == curChecker) count3AI++;
						if(board[row][col] == oppChecker) count3Hu++;	
				}
				
				if (board[row][col] != ' ' &&
						board[row][col] == board[row+1][col+1] &&
						board[row][col] == board[row+3][col+3] &&
					    board[row+2][col+2] == ' ' &&
					    board[row+1][col+2] != ' ') {
						
						if(board[row][col] == curChecker) count3AI++;
						if(board[row][col] == oppChecker) count3Hu++;	
				}
				
				//count 2 in row
				if (board[row][col] != ' ' &&
					board[row][col] == board[row+1][col+1] &&
					board[row+2][col+2] == ' ' &&
				    board[row+3][col+3] == ' ' &&
				    board[row+1][col+2] != ' ' &&
				    board[row+2][col+3] != ' ') {
					
					if(board[row][col] == curChecker) count2AI++;
					if(board[row][col] == oppChecker) count2Hu++;	
				}
				
				if (board[row+3][col+3] != ' ' &&
						board[row+3][col+3] == board[row+2][col+2] &&
						board[row+1][col+1] == ' ' &&
					    board[row][col] == ' ') {
						
					if((row-1) >= 0){
						if(board[row-1][col] != ' ' && board[row][col+1] != ' '){
							if(board[row+3][col+3] == curChecker) count2AI++;
							if(board[row+3][col+3] == oppChecker) count2Hu++;
						}
					}
					
					if((row-1) < 0){
						if(board[row+3][col+3] == curChecker) count2AI++;
						if(board[row+3][col+3] == oppChecker) count2Hu++;
					}
				}
				
				if (board[row][col] != ' ' &&
						board[row][col] == board[row+2][col+2] &&
						board[row+1][col+1] == ' ' &&
					    board[row+3][col+3] == ' ' &&
					    board[row][col+1] != ' ' &&
					    board[row+2][col+3] != ' ') {
						
						if(board[row][col] == curChecker) count2AI++;
						if(board[row][col] == oppChecker) count2Hu++;	
				}
				
				if (board[row][col] != ' ' &&
						board[row][col] == board[row+3][col+3] &&
						board[row+1][col+1] == ' ' &&
					    board[row+2][col+2] == ' ' &&
					    board[row][col+1] != ' ' &&
					    board[row+1][col+2] != ' ') {
						
						if(board[row][col] == curChecker) count2AI++;
						if(board[row][col] == oppChecker) count2Hu++;	
				}
				
				if (board[row+1][col+1] != ' ' &&
						board[row+1][col+1] == board[row+2][col+2] &&
						board[row+3][col+3] == ' ' &&
					    board[row][col] == ' ') {
						
					if((row-1) >= 0){
						if(board[row-1][col] != ' ' && board[row+2][col+3] != ' '){
							if(board[row+1][col+1] == curChecker) count2AI++;
							if(board[row+1][col+1] == oppChecker) count2Hu++;
						}
					}
					
					if((row-1) < 0){
						if(board[row+1][col+1] == curChecker) count2AI++;
						if(board[row+1][col+1] == oppChecker) count2Hu++;
					}
				}
				
				if (board[row+3][col+3] != ' ' &&
						board[row+3][col+3] == board[row+1][col+1] &&
						board[row+2][col+2] == ' ' &&
					    board[row][col] == ' ') {
						
					if((row-1) >= 0){
						if(board[row-1][col] != ' ' && board[row+1][col+2] != ' '){
							if(board[row+3][col+3] == curChecker) count2AI++;
							if(board[row+3][col+3] == oppChecker) count2Hu++;
						}
					}
					
					if((row-1) < 0){
						if(board[row+3][col+3] == curChecker) count2AI++;
						if(board[row+3][col+3] == oppChecker) count2Hu++;
					}
				}
		    }
		}
		
		//check for win diagonally (lower left to upper right)
		for (int row = 3; row < rows; row++) {
		    for (int col = 0; col < columns-3; col++){ 
		    	//count 3 in row
				if (board[row][col] != ' ' &&
					board[row][col] == board[row-1][col+1] &&
					board[row][col] == board[row-2][col+2] &&
					board[row-3][col+3] == ' '){
					
					if((row-4) >= 0){
						if(board[row-4][col+3] != ' '){
							if(board[row][col] == curChecker) count3AI++;
						    if(board[row][col] == oppChecker) count3Hu++;	
						}
					}	
					
					if((row-4) < 0){
						if(board[row][col] == curChecker) count3AI++;
					    if(board[row][col] == oppChecker) count3Hu++;	
					}

				}
				
				if (board[row-3][col+3] != ' ' &&
						board[row-3][col+3] == board[row-1][col+1] &&
						board[row-3][col+3] == board[row-2][col+2] &&
						board[row][col] == ' ' &&
						board[row-1][col] != ' '){
			
						if(board[row-3][col+3] == curChecker) count3AI++;
					    if(board[row-3][col+3] == oppChecker) count3Hu++;	
				}
				
				if (board[row-3][col+3] != ' ' &&
						board[row-3][col+3] == board[row][col] &&
						board[row-3][col+3] == board[row-2][col+2] &&
						board[row-1][col+1] == ' ' &&
						board[row-2][col+1] != ' '){
			
						if(board[row-3][col+3] == curChecker) count3AI++;
					    if(board[row-3][col+3] == oppChecker) count3Hu++;	
				}
				
				if (board[row-3][col+3] != ' ' &&
						board[row-3][col+3] == board[row][col] &&
						board[row-3][col+3] == board[row-1][col+1] &&
						board[row-2][col+2] == ' ' &&
						board[row-3][col+2] != ' '){
			
						if(board[row-3][col+3] == curChecker) count3AI++;
					    if(board[row-3][col+3] == oppChecker) count3Hu++;	
				}
				
				//count 2 in row
				if (board[row][col] != ' ' &&
					board[row][col] == board[row-1][col+1] &&
					board[row-2][col+2] == ' ' &&
					board[row-3][col+3] == ' '){
		
					if((row-4) >= 0){
						if(board[row-3][col+2] != ' ' && board[row-4][col+3] != ' '){
							if(board[row][col] == curChecker) count2AI++;
							if(board[row][col] == oppChecker) count2Hu++;
						}
					}
					
					if((row-4) < 0){
						if(board[row][col] == curChecker) count2AI++;
						if(board[row][col] == oppChecker) count2Hu++;
					}
				}
				
				if (board[row-3][col+3] != ' ' &&
						board[row-3][col+3] == board[row-2][col+2] &&
						board[row-1][col+1] == ' ' &&
						board[row][col] == ' ' &&
						board[row-2][col+1] != ' ' &&
						board[row-1][col] != ' '){
			
						if(board[row-3][col+3] == curChecker) count2AI++;
					    if(board[row-3][col+3] == oppChecker) count2Hu++;	
				}
				
				if (board[row][col] != ' ' &&
						board[row][col] == board[row-2][col+2] &&
						board[row-1][col+1] == ' ' &&
						board[row-3][col+3] == ' '){
			
						if((row-4) >= 0){
							if(board[row-2][col+1] != ' ' && board[row-4][col+3] != ' '){
								if(board[row][col] == curChecker) count2AI++;
								if(board[row][col] == oppChecker) count2Hu++;
							}
						}
						
						if((row-4) < 0){
							if(board[row][col] == curChecker) count2AI++;
							if(board[row][col] == oppChecker) count2Hu++;
						}
				}
				
				if (board[row-2][col+2] != ' ' &&
						board[row-2][col+2] == board[row-1][col+1] &&
						board[row][col] == ' ' &&
						board[row-3][col+3] == ' '){
			
						if((row-4) >= 0){
							if(board[row-1][col] != ' ' && board[row-4][col+3] != ' '){
								if(board[row-2][col+2] == curChecker) count2AI++;
								if(board[row-2][col+2] == oppChecker) count2Hu++;
							}
						}
						
						if((row-4) < 0){
							if(board[row-2][col+2] == curChecker) count2AI++;
							if(board[row-2][col+2] == oppChecker) count2Hu++;
						}
				}
				
				if (board[row-3][col+3] != ' ' &&
						board[row-3][col+3] == board[row][col] &&
						board[row-1][col+1] == ' ' &&
						board[row-2][col+2] == ' ' &&
						board[row-2][col+1] != ' ' &&
						board[row-3][col+2] != ' '){
			
						if(board[row-3][col+3] == curChecker) count2AI++;
					    if(board[row-3][col+3] == oppChecker) count2Hu++;	
				}
				
				if (board[row-3][col+3] != ' ' &&
						board[row-3][col+3] == board[row-1][col+1] &&
						board[row-2][col+2] == ' ' &&
						board[row][col] == ' ' &&
						board[row-3][col+2] != ' ' &&
						board[row-1][col] != ' '){
			
						if(board[row-3][col+3] == curChecker) count2AI++;
					    if(board[row-3][col+3] == oppChecker) count2Hu++;	
				}
		    }
		} 
		
		//return (((2 * count3AI )+(count2AI)) - ((5 * count3Hu) + (2 * count2Hu)));
		return (3*count3AI + count2AI) - ((3 * count3Hu) + (2 * count2Hu));
	}
	
	/**
	 * tells the node if it has a child or not to determine if the node is a leaf or not in the algorithm 
	 * @param example
	 */
   public void sethasChild(boolean example){
  	 this.haschild=example;
   }
	
   
   /**
    * determines if this node has a child or not. if an action on this board produces a child then after the action has been completed and child is made this boolean is set to true;
    * @return
    */
   public boolean gethasChild(){
  	 return this.haschild;
   }
   
	/**
	* 
	* @param player
	*/
   public void setPlayer(String player){
  	 this.player=player;
   }
   
   
   /**
    * 
    * @return the player (most likely max/min)
    */
   public String getPlayer(){
  	 return this.player;
   }
   
   /**
    * 
    * @return
    */
   public ArrayList<node> getChildArray(){
  	 return children;
   }
   
   /** Adds child to Array of Children and changes boolean for haschild to true
    * 
    * @param n
    */
   public void addchildtoArray(node n){
  	 children.add(n);
  	 sethasChild(true);
   }
   
   /**
    * sets the evaluation function value for a node (used when recursing up the tree in the minimax algorithm)
    * @param value
	*/
   public void setEvalFunction(int value){
  	 this.value=value;
   }
   
   /**
    * 
    * @return
    */
   public int getvalue(){
  	 return value;
   }
}
