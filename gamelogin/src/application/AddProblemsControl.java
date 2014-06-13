/* Controller for the problems page
 * Add_Problems.fxml
 */
package application;

import java.net.URL;
import java.util.ResourceBundle;
import com.mysql.jdbc.Connection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddProblemsControl extends AnchorPane implements Initializable {
	private Main application;	
	private Connection con; 

	// constructs the class
	public void setApp(Main application){
		this.application = application;
		this.con = application.getConnection();	
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// handler for the url link to the common core standards
		link.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	application.getHostServices().showDocument("http://www.corestandards.org/Math/Practice/");            	
            }
        });
	}

	// textfields on the view
	@FXML private TextField addOne;
	@FXML private TextField subTwo;
	@FXML private TextField addTwo;
	@FXML private TextField subOne;
	@FXML private Hyperlink link;

	// method to ensure that the problem corresponds to a level so it will distribute properly
	public boolean levelValidator(int f, int s){
		if(f >= 0 && f <= 19 && s >= 0 && s <= 19)
			return true;
		if(f >= 20 && f <= 39 && s >= 20 && s <= 39)
			return true;
		if(f >= 40 && f <= 59 && s >= 40 && s <= 59)
			return true;
		if(f >= 60 && f <= 79 && s >= 60 && s <= 79)
			return true;
		if(f >= 80 && f <= 99 && s >= 80 && s <= 99)
			return true;
		return false;		
	}

	// handler for the add addition problem button
	@FXML void addProblem(ActionEvent event) {
		String first = addOne.getText();
		String second = addTwo.getText();
		int f = -1;
		int s = -1;
		try{
			f = Integer.parseInt(first);
			s = Integer.parseInt(second);
		}
		catch(NumberFormatException e){
			addOne.setText("");
			addTwo.setText("");
		}
		if(first.equals("") || second.equals("") || f < 0 || f > 99 || s < 0 || s > 99 || !levelValidator(f,s)){
			final Stage newStage = new Stage();
			VBox comp = new VBox();
			comp.setAlignment(Pos.CENTER);
			comp.setStyle("-fx-border-style: solid;"

			                 + "-fx-border-width: 1;"

			                 + "-fx-border-color: red");

			Text nameField = new Text("Please enter two numbers between 0 and 99.");
			Text otherField = new Text("Both numbers should also be within a particular level.");
			Button cont = new Button("Close");
			comp.getChildren().add(nameField);
			comp.getChildren().add(otherField);
			comp.getChildren().add(cont);
			comp.setSpacing(2);
			Scene stageScene = new Scene(comp, 400, 125);
			newStage.setScene(stageScene);
			newStage.show();
			cont.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent arg0) {
					newStage.hide();				
				}
			});	
			return;
		}
		

		else{
			dataAccess data = new dataAccess(con);
			data.insert("insert into addproblems (addfirst, addsecond) Values ("+first+","+second+");");
			addOne.setText("");
			addTwo.setText("");
		}
	}

	// handler for the add subtraction problems button
	@FXML void subProblem(ActionEvent event) {
		String first = subOne.getText();
		String second = subTwo.getText();
		int f = -1;
		int s = -1;
		try{
			f = Integer.parseInt(first);
			s = Integer.parseInt(second);
		}
		catch(NumberFormatException e){
			addOne.setText("");
			addTwo.setText("");
		}
		// if the user hasn't entered anything or entered invalid input, launch a popup
		if(first.equals("") || second.equals("") || f > 99 || s > 99 || f < s || f < 0 || s < 0 || !levelValidator(f,s)){
			final Stage newStage = new Stage();
			VBox comp = new VBox();
			comp.setAlignment(Pos.CENTER);
			comp.setStyle("-fx-border-style: solid;"

			                 + "-fx-border-width: 1;"

			                 + "-fx-border-color: red");

			Text nameField = new Text("Please enter two numbers between 0 and 99.");
			Text otherField = new Text("Both numbers should also be within a particular level.");
			Text line = new Text("First number must be greater than or equal to the second.");
			Button cont = new Button("Close");
			comp.getChildren().add(nameField);
			comp.getChildren().add(otherField);
			comp.getChildren().add(line);
			comp.getChildren().add(cont);
			comp.setSpacing(2);
			Scene stageScene = new Scene(comp, 400, 125);
			newStage.setScene(stageScene);
			newStage.show();
			cont.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent arg0) {
					newStage.hide();				
				}
			});		
			return;
		}
		
		// otherwise add the problem to the database
		else{
			dataAccess data = new dataAccess(con);
			data.insert("insert into subproblems (subfirst, subsecond) Values ("+first+","+second+");");
			subOne.setText("");
			subTwo.setText("");
		}
	}

	// handler for the back button
	@FXML void back(ActionEvent event) {
		application.goToCustomProblems();
	}
}