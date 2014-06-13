/* Controller for the simple play
 * place value problems
 * PlaceValue_Play.fxml
 */

package application;

import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.ResourceBundle;
import com.mysql.jdbc.Connection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PlaceValuePlayControl extends AnchorPane implements Initializable {
	private Main application;
	private Connection con; 
	private String user;	
	private int level;
	private int questionCounter;
	private int correct;	
	private int min;
	private int max;
	private String currentProblem;
	private Image correctGraphic;
	private Image incorrectGraphic;
	private Media correctSound;
	private Media incorrectSound;

	// construct the class and set the view objects based on level
	public void setApp(Main application){
		this.application = application;
		this.con = application.getConnection();
		this.user = application.getUser();		
		this.level = application.getLevel();
		this.correctGraphic = new Image(getClass().getResourceAsStream("correct.png"));
		this.incorrectGraphic = new Image(getClass().getResourceAsStream("incorrect.png"));
		this.correctSound = new Media(getClass().getResource("ding.mp3").toString());
		this.incorrectSound = new Media(getClass().getResource("buzzer.mp3").toString());
		sub.setText("Place Value");
		questionNumber.setText("Question #1");
		correctAnswer.setText("");
		score.setText("");

		if(level == 1){
			lev.setText("Level 1");
			min = 10;
			max = 50;
			hundredsPrompt.setText("");
			hundredsAnswer.setVisible(false);
			tensAnswer.requestFocus();
		}
		if(level == 2){
			lev.setText("Level 2");
			min = 50;
			max = 99;
			hundredsPrompt.setText("");
			hundredsAnswer.setVisible(false);
			tensAnswer.requestFocus();
		}
		if(level == 3){
			lev.setText("Level 3");
			min = 100;
			max = 150;
			hundredsAnswer.requestFocus();
		}
		if(level == 4){
			lev.setText("Level 4");
			min = 150;
			max = 200;
			hundredsAnswer.requestFocus();
		}
		if(level == 5){
			lev.setText("Level 5");
			min = 200;
			max = 250;	
			hundredsAnswer.requestFocus();
		}
		currentProblem = Integer.toString(numberGenerator(min,max));
		number.setText(currentProblem);		
	}

	// generates a random int for the problems
	public int numberGenerator(int min, int max){
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	// determines if student answered correctly
	private boolean isCorrect() {
		boolean rt = false;
		if(level == 1 || level == 2){
			int first = 0;
			int second = 0;
			try{
				first = Integer.parseInt(tensAnswer.getText());
				second = Integer.parseInt(onesAnswer.getText());
			}
			catch(NumberFormatException e){
				rt = false;
			}

			int parseFirst = Character.getNumericValue(currentProblem.charAt(0));
			int parseSecond = Character.getNumericValue(currentProblem.charAt(1));
			if(first == parseFirst && second == parseSecond){
				rt = true;
			}
		}
		if(level > 2){
			int first = 0;
			int second = 0;
			int third = 0;
			try{
				first = Integer.parseInt(hundredsAnswer.getText());
				second = Integer.parseInt(tensAnswer.getText());
				third = Integer.parseInt(onesAnswer.getText());
			}
			catch(NumberFormatException e){
				rt = false;
			}
			int parseFirst = Character.getNumericValue(currentProblem.charAt(0));
			int parseSecond = Character.getNumericValue(currentProblem.charAt(1));
			int parseThird = Character.getNumericValue(currentProblem.charAt(2));
			if(first == parseFirst && second == parseSecond && third == parseThird){
				rt = true;
			}
		}
		return rt;
	}

	// media player for sound effects
	private void playMedia(Media m){
		if (m != null){
			MediaPlayer mp = new MediaPlayer(m);
			mp.play();
		}
	}

	// plays the correct sound if student answered correctly
	public void playCorrectSound(){
		try{
			playMedia(correctSound);
		}
		catch(Exception ex){
			System.err.print(ex);
		}
	}

	// plays the incorrect buzzer if student answered incorrectly
	public void playInCorrectSound(){
		try{			
			playMedia(incorrectSound);
		}
		catch(Exception ex){
			System.err.print(ex);
		}
	}

	// view objects to be manipulated
	@FXML private TextField tensAnswer;
	@FXML private Label sub;
	@FXML private Label number;
	@FXML private Label score;
	@FXML private Button enterButton;
	@FXML private TextField onesAnswer;
	@FXML private ImageView imageView;
	@FXML private Label lev;
	@FXML private Label correctAnswer;
	@FXML private Label questionNumber;
	@FXML private TextField hundredsAnswer;
	@FXML private Label hundredsPrompt;

	// handler for the back button
	@FXML void back(ActionEvent event) {
		application.goToLevel();
	}

	// handler for the logout button
	@FXML void logout(ActionEvent event) {
		application.goToLogin();
	}

	// handler for the enter button
	@FXML void enter(ActionEvent event) {
		// disable enter button until new question
		enterButton.setDisable(true);
		// increment the number of unique questions replied to
		questionCounter++;
		// if the student is correct
		if(isCorrect()){
			playCorrectSound();
			correct++;
			score.setText("Score: "+ Integer.toString(correct) +"/10");
			imageView.setImage(correctGraphic);

		}
		// if the student is incorrect
		else{
			playInCorrectSound();
			imageView.setImage(incorrectGraphic);
			if(level <= 2){
				correctAnswer.setText("Tens = " + currentProblem.charAt(0) + ", Ones = " + currentProblem.charAt(1));
			}
			if(level > 2){
				correctAnswer.setText("Hundreds = "+ currentProblem.charAt(0)+", Tens = " + currentProblem.charAt(1) + ", Ones = " + currentProblem.charAt(2));
			}
		}
	}

	// handler for the new question button
	@FXML void newQuestion(ActionEvent event) {
		// enable the enter button
		enterButton.setDisable(false);
		questionNumber.setText("Question #" + Integer.toString(questionCounter + 1));
		correctAnswer.setText("");
		imageView.setImage(null);
		hundredsAnswer.clear();
		tensAnswer.clear();
		onesAnswer.clear();
		// place the cursor in the right box for the next question
		if(level == 1 || level == 2){
			tensAnswer.requestFocus();
		}
		if(level > 2){
			hundredsAnswer.requestFocus();
		}
		// if the student just answered the tenth question
		if(questionCounter == 10){
			dataAccess data = new dataAccess(con);
			String[] values = data.gameHome(user);
			// and they scored an 80% or above
			if(correct >= 8){
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				DateFormat dateFormat = new SimpleDateFormat("MM:dd:yy");
				String text = dateFormat.format(timestamp);

				if(Integer.parseInt(values[8]) < level){
					data.update("update progress set npHighestLevel ='"+ Integer.toString(level)+"' where `s-user` = '"+user+"';");
					data.update("update progress set npTimeScore ='"+ text +"' where `s-user` = '"+user+"';");				
					data.update("update progress set npCurrentPercent ='"+ Integer.toString(correct) +"' where `s-user` = '"+user+"';");
				}

				application.setTempScore(correct);
				application.goToLevelUp();
			}
			// otherwise there is no level up and the student is directed to the retry level view,
			// score is updated if it's higher than their last attempt
			else{
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				DateFormat dateFormat = new SimpleDateFormat("MM:dd:yy");
				String text = dateFormat.format(timestamp);				
				data.update("update progress set npCurrentPercent ='"+ Integer.toString(correct) +"' where `s-user` = '"+user+"';");
				data.update("update progress set npTimeScore ='"+ text +"' where `s-user` = '"+user+"';");

				application.setTempScore(correct);
				application.goToRetryLevel();
			}
		}
		// otherwise create new problem and display it on the view
		else{
			currentProblem = Integer.toString(numberGenerator(min,max));
			number.setText(currentProblem);	
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
}