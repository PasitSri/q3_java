import java.awt.*;
import javax.swing.*;
import java.util.Scanner;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;


public class App extends JFrame{
	String player; 
	boolean winner;
	int turnCount, row, col;
	String[][] boardArray = {{"-", "-", "-"}, {"-", "-", "-"}, {"-", "-", "-"}};	
	int w=900, h=900;
	int size = 300;

	public void paint(Graphics g){
		g.setFont(new Font("Ubuntu", Font.PLAIN, 70));
		if(checkWinner()){
			g.setColor(Color.white);
			g.fillRect(0,0,w,h);
			g.setColor(Color.BLACK);
			String winner = "Player "+player+" are winner";
			g.drawString(winner, 100, 300);
			return ;
		}
		else if(turnCount >=9){
			g.setColor(Color.white);
			g.fillRect(0,0,w,h);
			g.setColor(Color.BLACK);
			String winner = "Draw ";
			g.drawString(winner, 100, 300);
			return ;
		}

		g.setColor(Color.white);
		g.fillRect(0,0,w,h);
		g.setColor(Color.WHITE);	
		g.setColor(Color.BLACK);
		int x=100,y=100;
		for(int r=0; r<3; r++){
			g.drawLine(0, r*size, w, r*size);
			for(int c=0; c<3; c++){
				g.drawLine(c*size, 0, c*size, h);
				g.drawString(boardArray[r][c], x, y);
				x+=300;
			}
			x=100;
			y+=300;
		}
	}

	public void changePlayer(){
		if (this.player == "o"){
			player = "x";
		}else{
			player = "o";
		}
	}

	public void displayBoard(){
		System.out.println("\n");
		for(int i=0; i<3; i++){
			System.out.println(""+boardArray[i][0]+"|"+boardArray[i][1]+"|"+boardArray[i][2]);
		}
		System.out.println("\n");
	}

	public boolean addPosition(){
		if (boardArray[row][col] != "-"){
			System.out.println("");
			System.out.println("This position is not empty");
			return false;
		}
		boardArray[row][col] = player;
		turnCount += 1;
		return true;
	}

	public boolean checkWinner(){
		for(int i=0; i<3; i++){
			if (boardArray[i][0]==player && boardArray[i][1]==player && boardArray[i][2]==player){
				winner = true;
				displayBoard();
				System.out.println("Player "+player+" winner");
				return true;
			}
		}
		for(int i=0; i<3; i++){
			if (boardArray[0][i]==player && boardArray[1][i]==player && boardArray[2][i]==player){
				winner = true;
				displayBoard();
				System.out.println("Player "+player+" winner");
				return true;
			}
		}
		if (boardArray[0][0]==player && boardArray[1][1]==player && boardArray[2][2]==player){
			winner = true;
			displayBoard();
			System.out.println("Player "+player+" winner");
			return true;
		}else if (boardArray[0][2]==player && boardArray[1][1]==player && boardArray[2][0]==player){
			winner = true;
			displayBoard();
			System.out.println("Player "+player+" winner");
			return true;
		}
		return false;
	}

	public void playGame(){
		inputProcessor inputPro = new inputProcessor();
		while(turnCount<9){
			displayBoard();
			System.out.println("player "+player);
			if ( inputPro.inputChecker() ){
				int[] pos = inputPro.getPosition();
				row = pos[0];
				col = pos[1];
				if(addPosition()){
					checkWinner();
					changePlayer();
				}
			}
			if(winner){
				break;
			}
		}
		if (turnCount >= 9 && winner == false){
			System.out.println("Draw");
		}
	}

	public void checkPosition(int x, int y){
		row = y/size;
		col = x/size;
		System.out.printf("%d %d\n", row, col);
		if (addPosition()){
			if (checkWinner()){
				repaint();
			}
			changePlayer();
		}
	}

	public App(){
		player = "o";
		winner = false;
		turnCount = 0;
		//playGame();
		setSize(900, 900);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				checkPosition(e.getX(), e.getY());
				repaint();
			}
		});
		setVisible(true);
	}

	public static void main(String[] args){
		new App();
	}
}

class inputProcessor{
	private int[]position = new int[2];
	boolean useNumpad = false;

	inputProcessor(){
		selectNumpad();
	}

	public void selectNumpad(){
		System.out.println("select (1)numpad mode or (2)phonepad mode (1 or 2): ");
		Scanner input = new Scanner(System.in);
		int c = input.nextInt();
		if (c==1){
			useNumpad = true;
		}
	}
		
	public int[] getPosition(){
		return position;
	}

	public int convertNumpad(int num){
		if(num==9){return 3;}
		else if(num==8){return 2;}
		else if(num==7){return 1;}
		else if(num==1){return 7;}
		else if(num==2){return 8;}
		else if(num==3){return 9;}
		else if(num==9){return 3;}
		else{return num;}
	}

	public int[] convertNumber(int num){
		int[] pos = new int[2];
		pos[0] = (int) num/3;
		if (num%3 != 0){
			pos[0] += 1; 
		}
		pos[1] = num%3;
		if (pos[1] == 0){
			pos[1] = 3;
		}
		return pos;
	}

	public boolean inputChecker(){
		Scanner input = new Scanner(System.in);
		int num;
		try{
			System.out.println("Select your position (1-9):");
			num = input.nextInt();
		}catch(Exception e){
			System.out.println("");
			System.out.println("Input value not integer");
			//input.close();
			return false;
		}
		if(useNumpad){
			num=convertNumpad(num);
		}
		int row, col;
		int[] pos = convertNumber(num);
		row = pos[0]-1;
		col = pos[1]-1;
		if ((row > 2 || row < 0) || (col > 2 || col <0)){
			System.out.println("");
			System.out.println(("Row or Column is out of range!"));
			return false;
		}
		position[0] = row;
		position[1] = col;
		return true;
	}

}
