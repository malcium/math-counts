/* Controller for the simple play
 * addition and subtraction problems
 * Simple_Play.fxml
 */

package application;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.sql.*;
import com.mysql.jdbc.Connection;
import javafx.collections.ObservableList;
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


public class SimplePlayControl extends AnchorPane implements Initializable {
	private Main application;
	private Connection con; 
	private String user;
	private int subject;
	private int level;
	private int questionCounter;
	private int correct;
	private int studentReply;
	private int max;
	private int min;
	private Image correctGraphic;
	private Image incorrectGraphic;
	private Media correctSound;
	private Media incorrectSound;
	private ArrayList<String> first;
	private ArrayList<String> second;
	private ObservableList<String> problems;

	public void setApp(Main application){

		// construct the class and set view objects
		this.application = application;
		this.con = application.getConnection();
		this.user = application.getUser();
		this.subject = application.getSubject();
		this.level = application.getLevel();
		this.correctGraphic  = new Image(getClass().getResourceAsStream("correct.png"));
		this.incorrectGraphic  = new Image(getClass().getResourceAsStream("incorrect.png"));
		this.correctSound = new Media(getClass().getResource("ding.mp3").toString());
		this.incorrectSound = new Media(getClass().getResource("buzzer.mp3").toString());
		score.setText("");
		correctAnswer.setText("");
		questionNumber.setText("Question #1");
		studentAnswerBox.setPromptText("????");
		studentAnswerBox.requestFocus();
		first = new ArrayList<String>();
		second = new ArrayList<String>();

		// set view objects based on subject and level selection
		if (subject == 1){
			operator.setText("+");
			sub.setText("Addition");
			// get a database connection and get any custom addition problems
			dataAccess data = new dataAccess(con);
			problems = data.getAddProblems();
			// if there are problems available, tokenize the numbers and plug into separate lists
			while(!problems.isEmpty()){
				String problem = problems.remove(0);				
				StringTokenizer st = new StringTokenizer(problem);				
				first.add((String) st.nextElement());
				st.nextElement();
				second.add((String) st.nextElement());
			}
		}
		if (subject == 2){
			operator.setText("-");
			sub.setText("Subtraction");
			// get a database connection and get any custom subtraction problems
			dataAccess data = new dataAccess(con);
			problems = data.getSubProblems();
			// if there are problems available, tokenize the numbers and plug into separate lists
			while(!problems.isEmpty()){
				String problem = problems.remove(0);
				StringTokenizer st = new StringTokenizer(problem);
				first.add((String) st.nextElement());
				st.nextElement();
				second.add((String) st.nextElement());
			}			
		}

		// level determination and setting of max and min random problem numbers
		if (level == 1){
			min = 0;
			max = 19;
			lev.setText("Level 1");
		}
		if (level == 2){
			min = 20;
			max = 39;
			lev.setText("Level 2");
		}
		if (level == 3){
			min = 40;
			max = 59;
			lev.setText("Level 3");
		}
		if (level == 4){
			min = 60;
			max = 79;
			lev.setText("Level 4");
		}
		if (level == 5){
			min = 80;
			max = 99;
			lev.setText("Level 5");
		}

		// initialize the view with the first random math problem
		String[] numbers = problemGenerator();
		firstNum.setText(numbers[0]);
		secondNum.setText(numbers[1]);	
	}

	// generates a random int for the problems
	public int numberGenerator(int min, int max){
		Random rand = new Random();
		//int min = 0;
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	// generates a problem so that negative integer answers aren't asked of the student
	public String[] problemGenerator(){
		String[] numbers = new String[2];
		int x = numberGenerator(min, max);
		int y = numberGenerator(min, max);
		// if x is less than y, swap them
		if(x < y){
			int temp = x;
			x = y;
			y = temp;
		}
		numbers[0] = Integer.toString(x);
		numbers[1] = Integer.toString(y);
		return numbers;
	}

	// determines if student answered correctly
	private boolean isCorrect() {
		boolean rt = false;
		// get the numbers from the problem
		int first = Integer.parseInt(firstNum.getText());
		int second = Integer.parseInt(secondNum.getText());

		// if the student's doing addition
		if(subject == 1){
			int correctAnswer = first + second;
			if(correctAnswer == studentReply){
				rt = true;
			}
		}
		// if the student's doing subtraction
		if(subject == 2){
			int correctAnswer = first - second;
			if(correctAnswer == studentReply){
				rt = true;
			}
		}
		return rt;
	}

	// media player for the sound effects
	private void playMedia(Media m){
		if (m != null){
			MediaPlayer mp = new MediaPlayer(m);
			mp.play();
		}
	}

	// the correct answer "ding" sound
	public void playCorrectSound(){
		try{			
			playMedia(correctSound);
		}
		catch(Exception ex){
			System.err.print(ex);
		}
	}

	// the incorrect "buzzer" sound
	public void playInCorrectSound(){
		try{		
			playMedia(incorrectSound);
		}
		catch(Exception ex){
			System.err.print(ex);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	// FXML items on the view to be manipulated
	@FXML private TextField studentAnswerBox;
	@FXML private Label secondNum;
	@FXML private ImageView imageView;
	@FXML private Label operator;
	@FXML private Label firstNum;
	@FXML private Label score;
	@FXML private Label correctAnswer;
	@FXML private Label questionNumber;
	@FXML private Button enterButton;
	@FXML private Label sub;
	@FXML private Label lev;

	// handler for the back button on the view
	@FXML void back(ActionEvent event) {
		application.goToLevel();
	}

	// handler for the logout button on the view
	@FXML void logout(ActionEvent event) {
		application.goToLogin();
	}

	// handler for the enter button and when the student hits the 'enter' key
	@FXML void enter(ActionEvent event) {
		// disable the enter button until next question is pressed
		enterButton.setDisable(true);
		// increment the number of unique questions replied to
		questionCounter++;
		String response = studentAnswerBox.getText();
		// attempt to parse student's answer to an int, catch as incorrect answer if garbage
		try{
			studentReply = Integer.parseInt(response);
		}
		catch(NumberFormatException e){
			playInCorrectSound();
			studentAnswerBox.setEditable(false);
			imageView.setImage(incorrectGraphic);
			studentReply = -1;
		}
		// if the response is correct, increment the # correct, and display the graphic, play sound
		if(isCorrect()){
			playCorrectSound();
			correct++;
			score.setText("Score: "+ Integer.toString(correct) +"/10");
			imageView.setImage(correctGraphic);
			studentAnswerBox.setEditable(false);

		}
		// if the response is not correct, display graphic, play sound
		else{
			playInCorrectSound();
			int x = Integer.parseInt(firstNum.getText());
			int y = Integer.parseInt(secondNum.getText());
			if(subject == 1){
				int z = x + y;
				correctAnswer.setText("Correct answer: " + Integer.toString(z));
			}
			if(subject == 2){
				int z = x - y;
				correctAnswer.setText("Correct answer: " + Integer.toString(z));
			}
			studentAnswerBox.setEditable(false);
			imageView.setImage(incorrectGraphic);
		}
	}	

	// handler for the new question button
	@FXML void newQuestion(ActionEvent event) {
		// enable the enter button so the student can answer new question
		enterButton.setDisable(false);
		// set the view text so the student knows what question they are on and set some view objects
		questionNumber.setText("Question #" + Integer.toString(questionCounter + 1));
		correctAnswer.setText("");
		studentAnswerBox.setEditable(true);
		studentAnswerBox.requestFocus();
		imageView.setImage(null);
		studentAnswerBox.clear();

		// if the student has answered all 10 questions on the level
		if(questionCounter == 10){
			dataAccess data = new dataAccess(con);
			String[] values = data.gameHome(user);
			// if the student gets 80% or better then they level up
			if(correct >= 8){
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				DateFormat dateFormat = new SimpleDateFormat("MM:dd:yy");
				String text = dateFormat.format(timestamp);
				if(subject == 1){				

					if(Integer.parseInt(values[2]) < level){
						data.update("update progress set addHighestLevel ='"+ Integer.toString(level)+"' where `s-user` = '"+user+"';");
						data.update("update progress set addTimeScore ='"+ text +"' where `s-user` = '"+user+"';");
						data.update("update progress set addCurrentPercent ='"+ Integer.toString(correct) +"' where `s-user` = '"+user+"';");
					}
				}
				if(subject == 2){
					if(Integer.parseInt(values[5]) < level){
						data.update("update progress set subHighestLevel ='"+ Integer.toString(level) +"' where `s-user` = '"+user+"';");
						data.update("update progress set subTimeScore ='"+ text +"' where `s-user` = '"+user+"';");
						data.update("update progress set subCurrentPercent ='"+ Integer.toString(correct) +"' where `s-user` = '"+user+"';");
					}
				}
				application.setTempScore(correct);
				application.goToLevelUp();
			}
			
			// otherwise the student's score is updated to their latest attempt, with no level up
			else{
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				DateFormat dateFormat = new SimpleDateFormat("MM:dd:yy");
				String text = dateFormat.format(timestamp);
				if(subject == 1){
					data.update("update progress set addTimeScore ='"+ text +"' where `s-user` = '"+user+"';");
					data.update("update progress set addCurrentPercent ='"+ Integer.toString(correct) +"' where `s-user` = '"+user+"';");
				}
				if(subject == 2){
					data.update("update progress set subTimeScore ='"+ text +"' where `s-user` = '"+user+"';");
					data.update("update progress set subCurrentPercent ='"+ Integer.toString(correct) +"' where `s-user` = '"+user+"';");
				}
				application.setTempScore(correct);
				application.goToRetryLevel();
			}
		}

		// if it's not the 10th question, load new math problem onto the view
		else{
			// if there are custom problems available
			if(!first.isEmpty() && !second.isEmpty()){
				int index = -1;

				// iterate through the lists of numbers for the problems
				for(int i = 0; i < first.size();i++){
					String f = first.get(i);
					String s = second.get(i);
					// if the number is less than or equal to the level max and greater than or equal to level min, store the index and break out of loop
					if(Integer.parseInt(f) <= max && Integer.parseInt(f) >= min && Integer.parseInt(s) <= max && Integer.parseInt(s) >= min){
						index = i;
						break;
					}
				}

				// if there was a valid index found for appropriate questions, remove the problem and use it
				if(index != -1){
					firstNum.setText(first.remove(index));
					secondNum.setText(second.remove(index));
				}

				// if no valid index found, then get a random problem
				else{
					String[] numbers = problemGenerator();
					firstNum.setText(numbers[0]);
					secondNum.setText(numbers[1]);	
				}
			}

			// get a random problem if the custom problem lists are empty and set the view
			else{
				String[] numbers = problemGenerator();
				firstNum.setText(numbers[0]);
				secondNum.setText(numbers[1]);	
			}
		}
	}
}
