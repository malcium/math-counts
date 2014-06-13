package application;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Color;
import java.net.URL;
import java.awt.Font;
import java.awt.Dimension;

/**
 * Post question identifier that tells the gamer what the answer to the previous question was.
 * 
 * @author Bart Joseph Nadala, Drew Wyand and Morgan McCoy
 * @version April 13, 2014
 */
@SuppressWarnings("serial")
public class CheckMark extends JPanel{
	private BufferedImage check;
	private BufferedImage ex;
	private boolean correct = false;
	private boolean answered = false;
	private String problem = "";

	/**
	 * Setup for the panel design. Loads images.
	 */
	public CheckMark(){
		setPreferredSize(new Dimension(300,30));
		setOpaque(false);
		try {  
			URL url = Fruits.class.getResource("images/correct.png");        	
			check = ImageIO.read(url);
			URL url1 = Fruits.class.getResource("images/incorrect.png");
			ex = ImageIO.read(url1);
		} catch (IOException ex) {
		}
	}

	/**
	 * Determines the image to draw.
	 * @param true if the answer was correct and false otherwise.
	 */
	public void setCorrect(boolean b){
		correct = b;
		repaint();
	}

	/**
	 * Determines whether the new question has been answered yet.
	 * @param true if the question has been answered yet and false otherwise.
	 */
	public void setAnswered(boolean b){
		answered = b;
		repaint();
	}

	/**
	 * Sets the contents of the previous problem.
	 * @param The formated question and answer to be printed.
	 */
	public void setText(String s){
		problem = s;
		repaint();
	}

	/**
	 * This method renders the component.
	 * @param g The Graphics object used to render  
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Draws either the check mark or the x mark to note if the answer was right or wrong.
		if(correct && answered){
			g.drawImage(check, 0,0,30,30,null);
		}else if(!correct && answered){
			g.drawImage(ex, 0,0,30,30,null);
		}

		//Draws the previous question along with the answer.
		g.setColor(Color.black);
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		g.drawString(problem, 30,25);
	}
}
