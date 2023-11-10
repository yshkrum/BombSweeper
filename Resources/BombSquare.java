import java.util.*;

import javax.swing.JOptionPane;
public class BombSquare extends GameSquare
{
	private boolean thisSquareHasBomb = false;
	public static final int MINE_PROBABILITY = 10;
	private int NumOfBombsAround;
	private boolean checked = false;
	private static ArrayList<BombSquare> bombs = new ArrayList<>();
	private ArrayList<BombSquare> neighbours = new ArrayList<>();
	private int boardWitdth = (board.getWidth()-20)/20;
	private int boardHeight = (board.getHeight()-20)/20;
	private int numOfSquares = boardHeight * boardWitdth;
	private static ArrayList<BombSquare> checkedSquares = new ArrayList<>();
	private boolean gameWon = false;

	/**
	 * Constructor of BombSquare
	 * @param x
	 * @param y
	 * @param board
	 */
	public BombSquare(int x, int y, GameBoard board)
	{
		super(x, y, "images/blank.png", board);

		Random r = new Random();
		thisSquareHasBomb = (r.nextInt(MINE_PROBABILITY) == 0);
		if(this.thisSquareHasBomb){
			bombs.add(this);
		}
	}

	/**
	 * Function which checks all neighbours of the square recursively until reaches the edge or is adjacent tot a bomb
	 */
	public void checkNeighbours(){
		if(!this.checked){
			this.checked = true;
			checkedSquares.add((this));
			BombSquare evBS = (BombSquare)board.getSquareAt(xLocation, yLocation);
			// East
			if(this.xLocation+1 < boardWitdth){
				evBS = (BombSquare)board.getSquareAt(xLocation+1, yLocation);
				neighbours.add(evBS);
			}
			// West
			if(this.xLocation > 0){
				evBS = (BombSquare)board.getSquareAt(xLocation-1, yLocation);
				neighbours.add(evBS);
			}
			// North
			if(this.yLocation > 0){
				evBS = (BombSquare)board.getSquareAt(xLocation, yLocation-1);
				neighbours.add(evBS);
			}
			// South
			if(this.yLocation+1 < boardHeight){
				evBS = (BombSquare)board.getSquareAt(xLocation, yLocation+1);
				neighbours.add(evBS);
			}
			// North-West
			if(this.yLocation > 0 && this.xLocation > 1){
				evBS = (BombSquare)board.getSquareAt(xLocation-1, yLocation-1);
				neighbours.add(evBS);
			}
			// North-East
			if(this.yLocation > 0 && (this.xLocation+1 < boardWitdth)){
				evBS = (BombSquare)board.getSquareAt(xLocation+1, yLocation-1);
				neighbours.add(evBS);
			}
			// South-West
			if(this.yLocation+1 < boardHeight && this.xLocation>0){
				evBS = (BombSquare)board.getSquareAt(xLocation-1, yLocation+1);
				neighbours.add(evBS);
			}
			// South-East
			if(this.yLocation+1 < boardHeight && (this.xLocation+1 < boardWitdth)){
				evBS = (BombSquare)board.getSquareAt(xLocation+1, yLocation+1);
				neighbours.add(evBS);
			}

			// Count the number of bombs around
			for (BombSquare neighbour : neighbours) {
				if(neighbour.thisSquareHasBomb){
					this.NumOfBombsAround++;
				}
			}

			if(!thisSquareHasBomb){
				// If the square has a bomb around then just show the number of bombs and 
				if(this.NumOfBombsAround > 0 ){
					this.setImage("images/" + this.NumOfBombsAround + ".png");
					return;
				}
				// If all neighbours are empty then set square blank and wait for others to be checked 
				else{
					this.setImage("images/" + this.NumOfBombsAround + ".png");
				}
			}
			
			//
			for (BombSquare neighbour : neighbours) {
				if(neighbour.checked == true){
					continue;
				}
				else{
					neighbour.checkNeighbours();	
					
				}
			}
		}		
	}

	/**
	 * Implemented function to check the nature of the clicked square. offers win and lose scenarios and an option to play the game again or leave.
	 */
	public void clicked(){
		if(!gameWon){
			// Show all bombs if clicked on the bomb square
			if (thisSquareHasBomb) {
				for (BombSquare bomb : bombs) {
					bomb.setImage("images/bomb.png");
				}
				int playAgain = JOptionPane.showConfirmDialog(null, "Boom! You swept " + checkedSquares.size() + " out of " + numOfSquares + " Squares! Play Again?", "BombSweeper", JOptionPane.YES_NO_OPTION);
				if(playAgain == JOptionPane.YES_OPTION){
					bombs.clear();
					GameBoard b = new GameBoard(board.getTitle(), boardHeight, boardWitdth);
					board.setVisible(false);
					board.dispose();
					checkedSquares.clear();
				}else{
					board.setVisible(false);
					board.dispose();
				}
			}

			try {
				this.checkNeighbours();
			} catch (Exception e) {
				e.printStackTrace();
			}


			// Check if won
			System.out.println(" Opened Squares " + checkedSquares.size()  + " Bombs" + bombs.size());
			if((checkedSquares.size() + bombs.size()) == numOfSquares){
				for (BombSquare bomb : bombs) {
				bomb.setImage("images/bomb.png");	
			}
			
			System.out.println("Victory!");
			int playAgain = JOptionPane.showConfirmDialog(null, "You Won! Play Again?", "BombSweeper", JOptionPane.YES_NO_OPTION);
			if(playAgain == JOptionPane.YES_OPTION){
				GameBoard b = new GameBoard(board.getTitle(), boardHeight, boardWitdth);
				board.setVisible(false);
				board.dispose();
				checkedSquares.clear();
				bombs.clear();
				gameWon = false;
			}else{
				board.setVisible(false);
				board.dispose();
			}
			}
		}
			
	}
}
