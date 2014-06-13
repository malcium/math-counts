/* Controller for the game level page
 * Game_Level.fxml
 */

package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.mysql.jdbc.Connection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class GameLevelControl extends AnchorPane implements Initializable{
	private Main application;
	private Connection con; 
	private int subject;
	private String user;
	private int levelCompleted;
	private Image checkMark; 

	public void setApp(Main application){
		this.application = application;
		this.con = application.getConnection();
		this.subject = application.getSubject();
		this.user = application.getUser();
		this.checkMark = new Image(getClass().getResourceAsStream("correct.png"));
		dataAccess data = new dataAccess(con);
		String[] values = data.gameHome(user);
		levelFruitButton.setDisable(true);
		// if the student selected addition, get their highest level completed
		if(subject == 1){
			levelCompleted = Integer.parseInt(values[2]);
			title.setText("Addition Levels:");
		}
		// if the student selected subtraction, get their highest level completed
		if(subject == 2){
			levelCompleted = Integer.parseInt(values[5]);
			title.setText("Subtraction Levels:");
		}
		// likewise if the student select place value
		if(subject == 3){
			levelCompleted = Integer.parseInt(values[8]);
			title.setText("Place Value Levels:");
		}

		if(levelCompleted == 0){
			levelTwoButton.setDisable(true);
			levelThreeButton.setDisable(true);
			levelFourButton.setDisable(true);
			levelFiveButton.setDisable(true);
		}
		if(levelCompleted == 1){
			levelOne.setImage(checkMark);
			levelThreeButton.setDisable(true);
			levelFourButton.setDisable(true);
			levelFiveButton.setDisable(true);
		}
		if(levelCompleted == 2){
			levelOne.setImage(checkMark);
			levelTwo.setImage(checkMark);
			levelFourButton.setDisable(true);
			levelFiveButton.setDisable(true);
		}
		if(levelCompleted == 3){
			levelOne.setImage(checkMark);
			levelTwo.setImage(checkMark);
			levelThree.setImage(checkMark);
			levelFiveButton.setDisable(true);
		}
		if(levelCompleted == 4){
			levelOne.setImage(checkMark);
			levelTwo.setImage(checkMark);
			levelThree.setImage(checkMark);
			levelFour.setImage(checkMark);
		}
		if(levelCompleted == 5){
			levelOne.setImage(checkMark);
			levelTwo.setImage(checkMark);
			levelThree.setImage(checkMark);
			levelFour.setImage(checkMark);
			levelFive.setImage(checkMark);
		}
		// if student has passed all five levels of simple play, unlock the falling fruit button
		if(Integer.parseInt(values[2]) == 5 && Integer.parseInt(values[2]) == 5 && Integer.parseInt(values[8]) == 5){
			levelFruitButton.setDisable(false);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	// view objects to be set by level score from database
	@FXML private Button levelOneButton;
	@FXML private Button levelTwoButton;
	@FXML private Button levelThreeButton;
	@FXML private Button levelFourButton;
	@FXML private Button levelFiveButton;
	@FXML private Button levelFruitButton;
	@FXML private ImageView levelOne;
	@FXML private ImageView levelFive;
	@FXML private ImageView levelTwo;
	@FXML private ImageView levelFour;
	@FXML private ImageView levelThree;
	@FXML private Label title;

	// handler for the level one button
	@FXML void levelOne(ActionEvent event) {
		application.setLevel(1);
		if(subject == 1 || subject == 2){
			application.goToSimplePlay();
		}
		if(subject == 3){
			application.goToPlaceValuePlay();
		}
	}

	// handler for the level two button
	@FXML void levelTwo(ActionEvent event) {
		application.setLevel(2);
		if(subject == 1 || subject == 2){
			application.goToSimplePlay();
		}
		if(subject == 3){
			application.goToPlaceValuePlay();
		}
	}

	// handler for the level three button
	@FXML void levelThree(ActionEvent event) {
		application.setLevel(3);
		if(subject == 1 || subject == 2){
			application.goToSimplePlay();
		}
		if(subject == 3){
			application.goToPlaceValuePlay();
		}
	}

	// handler for the level four button
	@FXML void levelFour(ActionEvent event) {
		application.setLevel(4);
		if(subject == 1 || subject == 2){
			application.goToSimplePlay();
		}
		if(subject == 3){
			application.goToPlaceValuePlay();
		}
	}

	// handler for the level five button
	@FXML void levelFive(ActionEvent event) {
		application.setLevel(5);
		if(subject == 1 || subject == 2){
			application.goToSimplePlay();
		}
		if(subject == 3){
			application.goToPlaceValuePlay();
		}
	}

	// handler for the falling fruit level button
	@FXML void levelFruit(ActionEvent event) {		
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "fallingfruit.jar");		
		pb.directory(new File("/Users/morganmccoy/Desktop"));		
		try {
			Process p = pb.start();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	// handler for the back button
	@FXML void back(ActionEvent event) {		
		application.goToGame();
	}
}
