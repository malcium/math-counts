/* Controller for the student home page
 * Game_Home.fxml
 */

package application;

import java.net.URL;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class GameScreenControl extends AnchorPane implements Initializable {
	private Main application;
	private Connection con; 
	private String user;
	private String[] values;

	// constructs the class
	public void setApp(Main application){
		this.application = application;
		this.con = application.getConnection();
		this.user = application.getUser();
		userName.setText(user);
		dataAccess data = new dataAccess(con);
		values = data.gameHome(user);
		studentName.setText(values[0] +" "+ values[1]);
		String addDate = values[4].substring(0,2) + "/" + values[4].substring(3,5) + "/" + values[4].substring(6,8);
		String subDate = values[7].substring(0,2) + "/" + values[7].substring(3,5) + "/" + values[7].substring(6,8);
		String npDate = values[10].substring(0,2) + "/" + values[10].substring(3,5) + "/" + values[10].substring(6,8);
		additionLabel.setText("Level: "+ values[2] +" Score: "+ values[3] + "0%" +" Date: "+ addDate);
		subtractionLabel.setText("Level: "+ values[5] +" Score: "+ values[6] + "0%" +" Date: "+ subDate);
		placeValueLabel.setText("Level: "+ values[8] +" Score: "+ values[9] + "0%" +" Date: "+ npDate);

		// if the student doesn't have any scores yet
		if(Integer.parseInt(values[2]) == 0 && Integer.parseInt(values[5]) == 0 && Integer.parseInt(values[8]) == 0 )
		{
			bestSubject.setText("None");
			score.setText("None");
			time.setText("None");
		}
		// if they do have scores then determine their best subject or if there's a tie (based off of highest level achieved)
		else{
			//System.out.println(Integer.parseInt(values[2]) +" "+ Integer.parseInt(values[5]) +" " + Integer.parseInt(values[8]));
			if(Integer.parseInt(values[2]) > Integer.parseInt(values[5]) && Integer.parseInt(values[2]) > Integer.parseInt(values[8])){
				bestSubject.setText("Addition");
				score.setText(values[3] +"0%");
				time.setText(addDate);
			}
			if(Integer.parseInt(values[5]) > Integer.parseInt(values[2]) && Integer.parseInt(values[5]) > Integer.parseInt(values[8])){
				bestSubject.setText("Subtraction");
				score.setText(values[6] +"0%");
				time.setText(subDate);
			}
			if(Integer.parseInt(values[8]) > Integer.parseInt(values[2]) && Integer.parseInt(values[8]) > Integer.parseInt(values[5])){
				bestSubject.setText("Place Value");
				score.setText(values[9] +"0%");
				time.setText(npDate);
			}
			else if(Integer.parseInt(values[2]) > Integer.parseInt(values[8]) && Integer.parseInt(values[5]) > Integer.parseInt(values[8]) && Integer.parseInt(values[2]) == Integer.parseInt(values[5])){
				bestSubject.setText("Tie!");
				score.setText("");
				time.setText("");
			}
			else if(Integer.parseInt(values[2]) > Integer.parseInt(values[5]) && Integer.parseInt(values[8]) > Integer.parseInt(values[5]) && Integer.parseInt(values[2]) == Integer.parseInt(values[8])){
				bestSubject.setText("Tie!");
				score.setText("");
				time.setText("");
			}
			else if(Integer.parseInt(values[5]) > Integer.parseInt(values[2]) && Integer.parseInt(values[8]) > Integer.parseInt(values[2]) && Integer.parseInt(values[5]) == Integer.parseInt(values[8])){
				bestSubject.setText("Tie!");
				score.setText("");
				time.setText("");
			}
			else if(Integer.parseInt(values[2]) == Integer.parseInt(values[5]) && Integer.parseInt(values[2]) == Integer.parseInt(values[8])){
				bestSubject.setText("Tie!");
				score.setText("");
				time.setText("");
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle bundle) {

	}
	// view objects to be set from database
	@FXML private Label placeValueLabel;
	@FXML private Label subtractionLabel;
	@FXML private Label score;
	@FXML private Label studentName;
	@FXML private Label bestSubject;
	@FXML private Label additionLabel;
	@FXML private Label time;
	@FXML private Label userName;

	// handler for the addition button
	@FXML void addition(ActionEvent event) {
		application.setSubject(1);
		application.goToLevel();
	}

	// handler for the subtraction button
	@FXML void subtraction(ActionEvent event) {
		application.setSubject(2);
		application.goToLevel();
	}

	// handler for the place value button
	@FXML void placeValue(ActionEvent event) {
		application.setSubject(3);
		application.goToLevel();
	}

	// handler for the logout button
	@FXML void logout(ActionEvent event) {		
		application.goToLogin();
	}
}