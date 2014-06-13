package application;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Color;
import java.lang.Math;
import java.net.URL;
import javax.swing.event.MouseInputAdapter;
import java.awt.Font;

/**
 * Panel that handles the graphics for the game. 
 * Includes loading images and printing them to the screen.
 * 
 * @author Bart Joseph J. Nadala, Drew Wyand, Morgan McCoy
 * @version April 13, 2012
 */
@SuppressWarnings("serial")
public class Fruits extends JPanel{
	private BufferedImage background;
	private BufferedImage background2;
	private BufferedImage boyCatch;
	private BufferedImage boyLeft;
	//private BufferedImage boyRight;
	private BufferedImage apple;
	private BufferedImage retry;
	private BufferedImage firework;
	private BufferedImage exit;
	private BufferedImage done;
	private BufferedImage basket;
	private BufferedImage cloud;
	private BufferedImage splitApple;
	private Vector<Double> xList = new Vector<Double>();
	private Vector<Double> yList = new Vector<Double>();
	private Vector<Integer> xBucket = new Vector<Integer>();
	private Vector<Integer> yBucket = new Vector<Integer>();
	private Vector<Integer> yOffSet = new Vector<Integer>();
	private Vector<Boolean> correct = new Vector<Boolean>();
	private Vector<Boolean> incorrect = new Vector<Boolean>();
	private int bucketApples = 0;
	private int cartX = 0;
	private int cartY = 0;
	private int xBounds = 0;
	private int yBounds = 0;
	private int score = 0;
	private int[] xCloud = new int[3];
	private boolean fail = false;
	private boolean right = true;
	private boolean postGame = false;


	/**
	 * Constructor for the fruits class.
	 * @param mia - handles the closing buttons events for the game.
	 */
	public Fruits(MouseInputAdapter mia){
		//Manual instantiation of the location of the images.
		for(int i=0; i<10; i++){
			xList.add(Math.random());
			yList.add(Math.random());
			yOffSet.add(0);
			correct.add(true);
			incorrect.add(false);
		}
		xBucket.add(100);
		xBucket.add(90);
		xBucket.add(76);
		xBucket.add(63);
		xBucket.add(50);   
		xBucket.add(90);
		xBucket.add(80);
		xBucket.add(70);
		xBucket.add(60);
		xBucket.add(55);
		yBucket.add(115);
		yBucket.add(115);
		yBucket.add(115);
		yBucket.add(115);
		yBucket.add(115);
		yBucket.add(125);
		yBucket.add(125);
		yBucket.add(125);
		yBucket.add(125);
		yBucket.add(125);   
		xCloud[0] = 850;
		xCloud[1] = 330;
		xCloud[2] = -250; 

		//Loads all the images
		try {   
			URL url = Fruits.class.getResource("images/treeBackground.png");
			background = ImageIO.read(url);
			URL url2 = Fruits.class.getResource("images/backround.jpg");
			background2 = ImageIO.read(url2);
			URL url3 = Fruits.class.getResource("images/charCatch.png");
			boyCatch = ImageIO.read(url3);
			URL url4 = Fruits.class.getResource("images/charLeft.png");
			boyLeft = ImageIO.read(url4);
			URL url5 = Fruits.class.getResource("images/apple.png");
			apple = ImageIO.read(url5);
			URL url6 = Fruits.class.getResource("images/retry.png");
			retry = ImageIO.read(url6);
			URL url7 = Fruits.class.getResource("images/firework.gif");
			firework = ImageIO.read(url7);
			URL url8 = Fruits.class.getResource("images/incorrect.png");
			exit = ImageIO.read(url8);
			URL url9 = Fruits.class.getResource("images/exit.png");
			done = ImageIO.read(url9);
			URL url10 = Fruits.class.getResource("images/basket.png");
			basket = ImageIO.read(url10);
			URL url11 = Fruits.class.getResource("images/cloud.png");
			cloud = ImageIO.read(url11);
			URL url12 = Fruits.class.getResource("images/splitApple.png");
			splitApple = ImageIO.read(url12);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		//Loads the mouse listener to enable the exit buttons.
		addMouseListener(mia);
	}

	/**
	 * Increments the specified fruit given the number n to fall.
	 * @param n - fruit number to increment height.
	 */
	public boolean increment(int n){
		if(n>=10||(int)((yBounds*.07)+yList.get(n)*(yBounds*.28))+yOffSet.get(n)>yBounds*.9){
			return false;
		}else{
			yOffSet.set(n,yOffSet.get(n)+1);
			return true;
		}
	}

	/**
	 * Sets the location of the boy image in the graphics.
	 * @param x - location of the boy.
	 * @param dir - direction of the boy facing, true for right and false for left.
	 */
	public void setCart(int x, boolean dir){
		right = dir;
		if(cartX!=x){
			if(cartY>0)
				cartY = 0;
			else
				cartY = 5;
		}
		cartX = x;
		repaint();
	}

	/**
	 * Returns the horizontal location of the boy catching apples. 
	 * @return cartX - location of the boy.
	 */
	public int getCart(){
		return cartX;
	}

	/**
	 * Deletes the image of the apple and may replace it with an image of a broken apple.
	 * @param n - apple number to be deleted.
	 * @param t - true to break the apple and false to just delete it.
	 */
	public void deleteFruit(int n, boolean t){
		correct.setElementAt(false, n);
		if(t){
			incorrect.setElementAt(true, n); 
		}
	}

	/**
	 * Returns the horizontal location of an apple.
	 * @param n - apple number to return the location of.
	 * @return horizontal location of the apple.
	 */
	public int getFruit(int n){
		return (int)((xBounds*.11)+xList.get(n)*(xBounds*.42));
	}

	/**
	 * Returns the vertical location of the apple.
	 * @param n - apple number
	 * @return The height of the apple.
	 */
	public int getFruitY(int n){
		return (int)((yBounds*.07)+yList.get(n)*(yBounds*.28))+yOffSet.get(n);
	}

	/**
	 * Returns the current horizontal size of the screen.
	 * @return the horizontal height of the screen.
	 */
	public int getXBounds(){
		return xBounds;
	}

	/**
	 * Returns the current vertical height of the screen.
	 * @return the vertical height of the screen.
	 */
	public int getYBounds(){
		return yBounds;
	}

	/**
	 * Moves the clouds from left to right with each call.
	 */
	public void incCloud(){
		for(int i=0; i<3; i++){
			if(xCloud[i]>=xBounds){
				xCloud[i]=-500;
			}
			xCloud[i]++;
		}
		repaint();
	}

	/**
	 * Shows the post game board that shows how well the students did.
	 * @param b - determines whether they passed of failed.
	 * @param s - their score.
	 */
	public void activatePostGame(boolean b, int s){
		postGame = true;
		fail = b;
		score = s;
		repaint();
	}

	/**
	 * Sets the apples that show on the bucket.
	 * @param a the number of apples to be shown in the bucket.
	 */
	public void setBucketApples(int a){
		bucketApples = a;
		repaint();
	}

	/**
	 * This method renders the component.
	 * @param g The Graphics object used to render  
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		xBounds = getWidth();
		yBounds = getHeight();

		//Background images that make up the scene.
		g.drawImage(background2, 0, 0, xBounds, yBounds, null);
		g.drawImage(cloud, xCloud[0], 0, 300, 100, null);
		g.drawImage(cloud, xCloud[1], 50, 400, 210, null);
		g.drawImage(cloud, xCloud[2], 25, 500, 175, null);
		g.drawImage(background, 0, 0, xBounds-50, yBounds+60, null);
		for(int i=9; i>=10-bucketApples; i--){
			g.drawImage(apple, xBounds - xBucket.get(i), yBounds-yBucket.get(i), 50, 50, null);
		}
		g.drawImage(basket, xBounds-100,yBounds-100, 100,100,null);

		//Draws the image of the apples. 
		for(int i=0; i<10; i++){
			if(correct.get(i))
				g.drawImage(apple, (int)((xBounds*.11)+xList.get(i)*(xBounds*.42)), (int)((yBounds*.07)+yList.get(i)*(yBounds*.28))+yOffSet.get(i), 50, 50, null);
		}

		//Draws the split apples that represent a wrongly answered question.
		for(int i=0; i<10; i++){
			if(incorrect.get(i))
				g.drawImage(splitApple, (int)((xBounds*.11)+xList.get(i)*(xBounds*.42)), (int)((yBounds*.07)+yList.get(i)*(yBounds*.28))+yOffSet.get(i), 50, 50, null);
		}

		//Draws the boy whether its to his left or right side. 
		if(right){
			g.drawImage(boyCatch,cartX,(yBounds-160)-cartY,150,150,null);
		}else{
			g.drawImage(boyLeft,cartX,(yBounds-160)-cartY,150,150,null);
		}   

		//Exit button on the top right corner of the screen.
		g.drawImage(exit, getWidth()-50, 0, 50, 50, null);

		//Post game images and design.
		if(postGame){
			g.setColor(Color.black);
			g.fillRect((int)(xBounds*.3)-5,(int)(yBounds*.25)-5,(int)(xBounds*.4)+10,(int)(yBounds*.5)+10);
			g.setColor(Color.white);
			g.fillRect((int)(xBounds*.3),(int)(yBounds*.25),(int)(xBounds*.4),(int)(yBounds*.5));

			g.setColor(Color.black);
			g.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
			if(fail){
				g.drawString("You got " + score + "0%", (int)(xBounds*.43), (int)(yBounds*.60));
				g.drawString("You need to do better than that!", (int)(xBounds*.33), (int)(yBounds*.60)+30);
				g.drawImage(retry, (int)(xBounds*.39), (int)(yBounds*.265), (int)(xBounds*.2),(int)(yBounds*.28), null);
			}else{
				g.drawString("You got " + score + "0%", (int)(xBounds*.43), (int)(yBounds*.60));
				g.drawString("Good Job!", (int)(xBounds*.43), (int)(yBounds*.60)+30);
				g.drawImage(firework, (int)(xBounds*.39), (int)(yBounds*.265), (int)(xBounds*.2),(int)(yBounds*.28), null);
			}
			g.drawImage(done, (int)(xBounds*.46), (int)(yBounds*.60)+50, 80,40, null);
		}
	}
}