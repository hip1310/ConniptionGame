/*Conniption Game
 * Artificial Intelligence - Group Project
 * Harsh Patel
 */

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class minimaxalg{
	private String choice;
	private String player = "max";
    private static int numflipcoinsPC=4;
    private int numflippc; 
    private Stack<node> solution = new Stack<node>();
    private int numfliphuman; //num flips for human or num flips for min 
    private int colAI = 8;
    private int colHum = 8;
	//constructor run the constructor once and each instantiation will decrease numflipcoinsPC if computer does a flip
	public minimaxalg(){
		//empty constructor
	}
	
	//the minimax public algorithm. this algorithm should eventually return a char[][]
	public node minimaxalgorithm(char[][] board, ArrayList<Integer> choose,int numflipsforhuman){
		int movemakerootmax; //this is the move that made the board which was passed to the AI so the computer (max) can decide on a move
		if(choose != null){
				if(choose.get(0)==8 && choose.get(1)==0){
					choice = "f";
					}
				else if(choose.get(0)==8 && choose.get(1)>=1 && choose.get(1)<8 && choose.get(2)==8){
					choice="fcf";
					}
				else if(choose.get(0)>=1 && choose.get(0)<8 && choose.get(1)==8 && choose.get(2)==0){
					choice="cf";
				}
				else if(choose.get(0)==8 && choose.get(1)<8 && choose.get(1)>=1 && choose.get(2)==0){
					choice="fc";
				}
				else if(choose.get(0)>=1 && choose.get(0)<8 && choose.get(1)==0){
					choice="c";
				}
		}
		
		this.numfliphuman=numflipsforhuman;
		this.numflippc=this.numflipcoinsPC;
		node rootnode = new node(board,choice,null,"max",0);
		
	    int depth=6;
		int min = Integer.MIN_VALUE;
		int max = Integer.MAX_VALUE;
		rootnode.sethasChild(true);
	    int util = minmax(rootnode, depth,min,max);	
	    //System.out.println("Utility" + util);
	
	    ArrayList<node> sameutil = new ArrayList<node>();
	    for(int count=0; count<rootnode.getChildArray().size(); count++){
	    	if(rootnode.getChildArray().get(count).getvalue()==util){
	    		sameutil.add(rootnode.getChildArray().get(count));
	    	}
	    }
	    
	    if(sameutil.get(0).getActionmenu()=="f"||sameutil.get(0).getActionmenu()=="cf"){
	    	numflipcoinsPC--;
	    }
	    else if(sameutil.get(0).getActionmenu()=="fcf"){
	    	numflipcoinsPC-=2;
	    }
	    
	    node returned = new node(sameutil.get(0).getBoardstate(),sameutil.get(0).getActionmenu(),null,"max",0);
	    System.out.println("Computer's move : " + sameutil.get(0).getActionmenu());
	    return returned;
	    
	}
	
	//private minimax algorithm 
	private int minmax(node n,int depth, int min, int max){
		node child = null;
		if(n.gethasChild()==false && depth==0){
			//return n.hybrid2(n.getBoardstate(), 'O');
			return n.hybrid3(n.getBoardstate(), 'O', n.getFlipPenalty());
		}
		//node is max
		else if(n.getPlayer()=="max"){
			int v = min;
		    actions(n);
		    for(int i=0; i<n.getChildArray().size(); i++){
		   int vprime=minmax(n.getChildArray().get(i),depth-1,v,max);
		    if(vprime>v){
		    	v=vprime;
		    	n.setEvalFunction(v);
		    }
		    if(v>max){
		    	n.setEvalFunction(max);
		    	return max;
		    }
		    }
		    solution.add(child);
		    return v;
		}
		//node is min
		else{
			 int v = max;
			 actions(n);
			 for(int i =0; i<n.getChildArray().size();i++){
			   int vprime=minmax(n.getChildArray().get(i),depth-1,min,v);
			    if(vprime<v){
			    	v=vprime;
			    	n.setEvalFunction(v);
			    }
			    if(v<min){
			    	n.setEvalFunction(min);
			    	return min;
			    }
			    }
			    return v;
		}
}
	private void actions(node n){
		ArrayList<node> childnodes = new ArrayList<node>();
		if(n.getActionmenu()=="f" || n.getActionmenu()=="fcf" || n.getActionmenu()=="cf"){
			//chip then flip
			actionc(n);
			actioncf(n);
		}
		else if(n.getActionmenu()=="fc" || n.getActionmenu()=="c"){
			actionc(n);
			actioncf(n);
			actionfc(n);
			actionfcf(n);
			actionf(n);
		}	
		else{
			actionc(n);
			actioncf(n);
			actionfcf(n);
			actionfc(n);
			actionf(n);
		}
	}
	private void actionf(node n){
		char [][] childboard = performFlip(n.getBoardstate());
		
		if(n.getPlayer()=="min" && numfliphuman>0){
			node child1 = new node(childboard,"f",n,"max",n.getFlipPenalty());
			numfliphuman--;
			n.addchildtoArray(child1);
			n.sethasChild(true);
		}
		else if(n.getPlayer() == "max" && numflippc>0)
		{
			node child1 = new node(childboard,"f",n,"min",n.getFlipPenalty() + 1);
			numflippc--;
			n.addchildtoArray(child1);
			n.sethasChild(true);
		}
	}
	private void actionfcf(node n){
		char [][] childboard = performFlip(n.getBoardstate());
		ArrayList<char[][]> chararrays = new ArrayList<char[][]>();
		for(int i=1; i<8;i++){
			chararrays.add(insertChip(childboard,n,i-1));
		}
		if(n.getPlayer()=="min" && numfliphuman>1){
			for(int i =0; i<chararrays.size();i++){
				char[][] afterflip =performFlip(chararrays.get(i));
				node child = new node(afterflip,"fcf",n,"max",n.getFlipPenalty());
				n.addchildtoArray(child);
				n.sethasChild(true);
			}
			numfliphuman-=2;
			
		}
		else if(n.getPlayer()=="max" && numflippc>1){
			for(int i =0; i<chararrays.size();i++){
				char[][] afterflip =performFlip(chararrays.get(i));
				node child = new node(afterflip,"fcf",n,"min",n.getFlipPenalty() + 2);
				n.addchildtoArray(child);
				n.sethasChild(true);
			}
			numflippc-=2;
		}
	}
	private void actioncf(node n){
		ArrayList<char[][]> chararrays = new ArrayList<char[][]>();
		for(int i=1; i<8;i++){
			chararrays.add(insertChip(n.getBoardstate(),n,i-1));
		}
		if(n.getPlayer()=="min" && numfliphuman>0){
			for(int i =0; i<chararrays.size();i++){
				char[][] afterflip =performFlip(chararrays.get(i));
				node child = new node(afterflip,"cf",n,"max",n.getFlipPenalty());
				numfliphuman--;
				n.addchildtoArray(child);
				n.sethasChild(true);
			}
			numfliphuman--;
		}
		else if (n.getPlayer()=="max" && numflippc>0){
			for(int i =0; i<chararrays.size();i++){
				char[][] afterflip =performFlip(chararrays.get(i));
				node child = new node(afterflip,"cf",n,"min",n.getFlipPenalty() + 1);
				n.addchildtoArray(child);
				n.sethasChild(true);
			}
		numflippc--;	
		}
	}
	
	private void actionfc(node n){
		char [][] childboard = performFlip(n.getBoardstate());
		ArrayList<char[][]> chararrays = new ArrayList<char[][]>();
		for(int i=1; i<8;i++){
			chararrays.add(insertChip(childboard,n,i-1));
		}
		if(n.getPlayer()=="min" && numfliphuman>0){
			for(int i =0;i<chararrays.size();i++){
			node child = new node(chararrays.get(i),"fc",n,"max",n.getFlipPenalty());
			n.addchildtoArray(child);
			n.sethasChild(true);
	}
			numfliphuman--;
		}
		else if (n.getPlayer()=="max" && numflippc>0){
			for(int i =0;i<chararrays.size();i++){
			node child = new node(chararrays.get(i),"fc",n,"min",n.getFlipPenalty() + 1);
			n.addchildtoArray(child);
			n.sethasChild(true);
	}
		numflippc--;
		}
	}
	
	private void actionc(node n){
		ArrayList<char[][]> chararrays = new ArrayList<char[][]>();
		for(int i=1; i<8;i++){
			chararrays.add(insertChip(n.getBoardstate(),n,i-1));
		}
		if(n.getPlayer()=="min"){
			for(int i =0;i<chararrays.size();i++){
			node child = new node(chararrays.get(i),"c",n,"max",n.getFlipPenalty());
			n.addchildtoArray(child);
			n.sethasChild(true);
			
	}
		}
		else{
			for(int i =0;i<chararrays.size();i++){
			node child = new node(chararrays.get(i),"c",n,"min",n.getFlipPenalty());
			n.addchildtoArray(child);
			n.sethasChild(true);
			}
	}
	}
	
	/**
	 * 
	 * @param board
	 * @return
	 */
	private char[][] performFlip(char[][] board){
		//this first copies the board referenced by the variable board
		 char[][] newboard = new char[6][7];
		 for(int row =0; row <board.length;row++){
				for(int col=0; col<board[row].length;col++){
					newboard[row][col]=board[row][col];
				}
		 }
		int rows = 6;
		int columns = 7;
    	for (int col = 0; col < columns; col++) {
           Stack<Character> temp = new Stack<Character>();
            for (int row = rows-1; row >= 0; row--){
            	if(newboard[row][col] == ' ')
            		break;
           
            	temp.push(board[row][col]);
            }
           int cRow = rows-1; 
           while(!temp.isEmpty()){
        	   	newboard[cRow][col] = temp.pop();
        	   	cRow--;
           }               
        }
    	return newboard;
    }
	
	/**
	 * Inserts chip at the appropriate location in the column 
	 * @param column
	 */
	public char[][] insertChip(char[][] board, node n,int column) {
		//this first copies the board referenced by the variable board
		 char[][] newboard = new char[6][7];
		 for(int row =0; row <board.length;row++){
				for(int col=0; col<board[row].length;col++){
					newboard[row][col]=board[row][col];
				}
		 }
		int rows = 6;
			for (int i = rows-1; i >= 0; i--){
				if (newboard[i][column] == ' ') {
					if(n.getPlayer()=="max"){ //if father is a max then insert a O
					newboard[i][column] = 'O';
					break;
					}
					else{
					newboard[i][column] = 'X';
					break;
					}
				}
			}
			return newboard;
	}
	

}




