package application;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.io.IOException;
import java.lang.Runnable;
import java.awt.BorderLayout;
import java.lang.Math;
import java.util.Vector;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import sun.audio.*;

/**
 * Controller class for the game. 
 * This handles all calculations for the game and also has full control over the graphics panel that draws the images.
 * 
 * @author Bart Joseph J. Nadala, Drew Wyand, Morgan McCoy
 * @version April 13, 2014
 */
public class FallingFruit
{
	private JFrame window = new JFrame("Math Counts");
	private Fruits tree = new Fruits(new exitButtonHandler());
	private JTextField answer = new JTextField(3);
	private Vector<String> problemSet;
	private Vector<Integer> answerSet;
	private Vector<Integer> site = new Vector<Integer>();
	private Vector<Boolean> correct = new Vector<Boolean>();
	private JLabel problem = new JLabel("Ready");
	private CheckMark checkMark = new CheckMark();
	private boolean close = true;
	private boolean done = false;
	private int count;

	private AudioPlayer soundPlayer = AudioPlayer.player;
	private AudioStream mainSong;
	private AudioStream retry;
	private AudioStream levelUp;

	/**
	 * Constructor for objects of class FallingFruit
	 */
	public FallingFruit(){
		//Window setup
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt){
				close = true;
				System.exit(0);
			}
		} );
		window.add(tree);

		try {
			this.mainSong = new AudioStream(FallingFruit.class.getResourceAsStream("sounds/main_song.wav"));
			this.retry = new AudioStream(FallingFruit.class.getResourceAsStream("sounds/retry.wav"));
			this.levelUp = new AudioStream(FallingFruit.class.getResourceAsStream("sounds/level_up.wav"));
		} catch (IOException e) {
			e.printStackTrace();
		} 

		//Question and answer board for the game. 
		JPanel set = new JPanel();
		answer.addKeyListener(new chatKeyHandler());
		problem.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
		answer.setPreferredSize(new Dimension(30,30));
		answer.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		set.add(problem);
		set.add(answer);
		set.add(checkMark);

		//Window finalization and game starter.
		window.add(set,BorderLayout.SOUTH);
		window.setUndecorated(true);
		window.setVisible(true);
		new startHandler();
	}

	/**
	 * This class handles the running of the entire game including the clouds and apples.
	 */
	class startHandler implements Runnable  {
		public startHandler() {
			count = 0;
			problemSet = new Vector<String>();
			answerSet = new Vector<Integer>();
			//Creates 10 problems and answers for the game randomly between addition, subtraction and number placement.
			for(int i = 0; i<10; i++){
				int choice = (int)(Math.random()*3);
				if(choice == 0){
					int a = (int)(Math.random()*899)+100;
					int place = (int)(Math.random()*3);
					if(place == 0){ //hundreds
						problemSet.add("Hundreds place of " + a + "? ");
						answerSet.add((int)(a/100));
					}else if(place == 1){//tens
						problemSet.add("Tens place of " + a + "? ");
						answerSet.add((int)(a%100)/10);
					}else{//ones
						problemSet.add("Ones place of " + a + "? ");
						answerSet.add((int)(a%10));
					}
				}else if(choice == 1){
					int a = (int)(Math.random()*99);
					int b = (int)(Math.random()*99);
					if(a>b){
						problemSet.add(a + " - " + b + " = ");
						answerSet.add(a-b);
					}else{
						problemSet.add(b + " - " + a + " = ");
						answerSet.add(b-a);
					}
				}else{
					int a = (int)(Math.random()*50);
					int b = (int)(Math.random()*50);
					problemSet.add(a + " + " + b + " = ");
					answerSet.add(a+b);
				}
				correct.add(false);
			}
			Thread t = new Thread(this);
			t.start();
			window.setVisible(true);
		}

		public void run(){
			//Pre-game count down to give the gamer time to mentally prepare.
			problem.setText("Ready");
			
			try{
				Thread.sleep(2000);
			} catch(InterruptedException e){
			}
			
			problem.setText("Set");
			
			try{
				Thread.sleep(2000);
			} catch(InterruptedException e){
			}
			
			problem.setText("GO!");
			
			try{
				Thread.sleep(2000);
			} catch(InterruptedException e){
			}
			
			soundPlayer.start(mainSong);

			//Starts the game.
			for(int i=0; i<10; i++){
				if(tree.getFruit(i)<75){
					site.add(0);
				}else{
					site.add(tree.getFruit(i)-75);
				}
			}
			problem.setText(problemSet.get(count));
			close = false;
			new runCartMan();
			new cloudManager();

			//Runs the apples to fall or disappear.
			while(!done && !close){
				while(tree.increment(count)){
					if(correct.get(count) && tree.getFruitY(count)>(int)(tree.getYBounds()*.8)){
						tree.deleteFruit(count, false);
					}
					try{
						Thread.sleep(25);
					} catch(InterruptedException e){
					}
					tree.repaint();
				}
				if(!correct.get(count)){
					tree.deleteFruit(count, true);
				}
				checkMark.setAnswered(false);
				if(count<9)
					count++;
				else{
					done = true;
				}
				if(!done){
					problem.setText(problemSet.get(count));
					checkMark.setText("Previous:"+problemSet.get(count-1) + answerSet.get(count-1));
				}
				answer.setText("");
				window.setVisible(true);
			}
			close = true;

			//Post-game that tells the gamer the how well they did.
			int score = 0;
			for(int i=0; i<10; i++){
				if(correct.get(i)){
					score++;
				}
			}
			tree.activatePostGame(score<8, score);
			if(score<8){
				soundPlayer.stop(mainSong);
				soundPlayer.start(retry);
			}
			else{
				soundPlayer.stop(mainSong);
				soundPlayer.start(levelUp);
			}
		}

		/**
		 * This class is a seperate thread that handles the movement of the cloud so long as the game is still running.
		 */
		class cloudManager implements Runnable{
			public cloudManager(){
				Thread t = new Thread(this);
				t.start();
			}

			public void run(){
				while(!close){
					tree.incCloud();
					try{
						Thread.sleep(50);
					} catch(InterruptedException e){
					}
				}
			}
		}
	}

	/**
	 * KeyListener that allows the gamers to enter their answers
	 * Also checks if the answers were correct or incorrect.
	 */
	class chatKeyHandler implements KeyListener{
		/**
		 * This is invoked when the user presses
		 * the ENTER key.
		 */
		public void keyPressed(KeyEvent e) { 
			if(e.getKeyCode() == KeyEvent.VK_ENTER && !close){  // if the enter key is pressed
				try{
					if(!done){
						if(!answer.getText().equals("") && Integer.parseInt(answer.getText())==answerSet.get(count)){
							correct.setElementAt(true, count);
							checkMark.setCorrect(true);
							checkMark.setAnswered(true);
						}else{
							checkMark.setCorrect(false);
							checkMark.setAnswered(true);
						}
					}
				}catch(NumberFormatException ex){
					checkMark.setCorrect(false);
					checkMark.setAnswered(true);
				}
				answer.setText("");
				window.setVisible(true);
			}
		}
		/** Not implemented */
		public void keyReleased(KeyEvent e) { }

		/** Not implemented */
		public void keyTyped(KeyEvent e) {  }
	}

	/**
	 * Seperate runnable thread that handles the movements of the boy.
	 * Allows the boy to run to the apple if the answer was correct.
	 * Allows the boy to run back to the bucket whent the apples were caught.
	 */
	class runCartMan implements Runnable{
		public runCartMan(){
			Thread t = new Thread(this);
			t.start();
		}

		public void run(){
			int n=0;
			while(!done &&!close){
				if(correct.get(count)){//Correct answer so the boy runs to the apple.
					if(site.get(count)> tree.getCart()+12 || site.get(count)< tree.getCart()-12){
						if(((int)(tree.getYBounds()*.8)-tree.getFruitY(count))!= 0){
							n = (int)Math.abs((tree.getCart()-site.get(count)))/((int)(tree.getYBounds()*.8)-tree.getFruitY(count));
						}
						if(n==0){
							n=10;
						}
						if(n<0){
							n=50;
						}
						if(site.get(count)>tree.getCart() && correct.get(count)){
							tree.setCart(tree.getCart()+n, true);
						}
						if(site.get(count)<tree.getCart()-70 && correct.get(count)){
							tree.setCart(tree.getCart()-n, false);
						}
					}
				}else{//Incorrect answer so the boy runs back to the bucket.
					if(tree.getXBounds()-200>=tree.getCart()){
						tree.setCart(tree.getCart()+10, true);
					}else{
						int counter = 0;
						for(int i=0; i<10; i++){
							if(correct.get(i)){
								counter++;
							}
						}
						tree.setBucketApples(counter);
					}
				}
				try{
					Thread.sleep(30);
				}catch(InterruptedException e){
				}
			}
		}
	}

	/**
	 * Main method that runs the entire game. 
	 * 
	 * @param  unnecessary
	 */
	public static void main(String args[]){
		new FallingFruit();
	}

	/**
	 * This class detects whether the mouse was clicked on an exit button at the right time to closse the game.
	 * @param MouseEvent e the values of the mouse when it clicked.
	 */
	class exitButtonHandler extends MouseInputAdapter{
		private int bSelected = 0;
		public void mousePressed(MouseEvent e){
			int y = e.getY();
			int x = e.getX();
			if(x>=tree.getXBounds()-50 && x<tree.getXBounds() && y>=0 && y<50){
				bSelected = 1;
			}
			if(x>=tree.getXBounds()*.46 && x<(tree.getXBounds()*.46)+80 && y>=tree.getYBounds()*.6+50 && y<tree.getYBounds()*.6+90){
				bSelected = 2;
			}
		}
		public void mouseReleased(MouseEvent e){
			if(bSelected == 1){
				close = true;
				System.exit(0);
				window.dispose();
			}
			if(bSelected ==2){
				close = true;
				System.exit(0);
				window.dispose();
			}
			bSelected = 0;
		}
		public void mouseMoved(MouseEvent e){
		}
	}
}
