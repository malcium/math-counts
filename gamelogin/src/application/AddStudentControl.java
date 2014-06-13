/* Controller for the add student page
 * Student_Info.fxml
 */
package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class AddStudentControl extends AnchorPane implements Initializable{
	private Main application;
	private Connection con; 

	public void setApp(Main application){
		this.application = application;
		this.con = application.getConnection();
	}

	// method to check for duplicate usernames in the student table
	public boolean duplicateUsername(String u){
		ArrayList<String> list;
		boolean rv = false;
		dataAccess data = new dataAccess(con);
		list = data.select("select `s-user` from student;");
		for(int i = 0; i < list.size(); i++){
			if(u.equals(list.get(i))){
				rv = true;
			}
		}
		return rv;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	// view objects to be set
	@FXML private TextField fnameBox;
	@FXML private TextField unameBox;
	@FXML private TextField emailBox;
	@FXML private TextField glevelBox;
	@FXML private TextField lnameBox;
	@FXML private Button backButton;
	@FXML private TextField pwordBox;
	@FXML private Button saveButton;
	@FXML private Text welcome;

	// handler for the save button
	@FXML void save(ActionEvent event) {
		String firstname = fnameBox.getText();
		String lastname = lnameBox.getText();
		String username = unameBox.getText();
		String email = emailBox.getText();
		String grade = glevelBox.getText();
		grade.trim();
		String password = pwordBox.getText();

		// try to parse the grade integer
		int gradeInt = -1;
		try{
			gradeInt = Integer.parseInt(grade);
		}
		catch(NumberFormatException e){
			final Stage newStage = new Stage();
			VBox comp = new VBox();
			comp.setAlignment(Pos.CENTER);
			comp.setStyle("-fx-border-style: solid;"

					                 + "-fx-border-width: 1;"

					                 + "-fx-border-color: red");

			Text nameField = new Text("Grade level must be '1' or '2'.");
			Button cont = new Button("Close");
			comp.getChildren().add(nameField);
			comp.getChildren().add(cont);
			Scene stageScene = new Scene(comp, 300, 100);
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

		// check to see if the username is already taken, launch popup if it is
		if(duplicateUsername(username)){
			final Stage newStage = new Stage();
			VBox comp = new VBox();
			comp.setAlignment(Pos.CENTER);

			comp.setStyle("-fx-border-style: solid;"

			                 + "-fx-border-width: 1;"

			                 + "-fx-border-color: red");

			Text nameField = new Text("The username '"+username+"' has already been taken!");
			Button cont = new Button("Close");
			comp.getChildren().add(nameField);
			comp.getChildren().add(cont);
			Scene stageScene = new Scene(comp, 350, 100);
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

		// check to see if the fields are empty, if so launch a popup
		else if(firstname.equals("") || lastname.equals("") || username.equals("") || email.equals("") || grade.equals("") || password.equals("")){
			final Stage newStage = new Stage();
			VBox comp = new VBox();
			comp.setAlignment(Pos.CENTER);

			comp.setStyle("-fx-border-style: solid;"

			                 + "-fx-border-width: 1;"

			                 + "-fx-border-color: red");

			Text nameField = new Text("All fields must be completed!");
			Button cont = new Button("Close");
			comp.getChildren().add(nameField);
			comp.getChildren().add(cont);
			Scene stageScene = new Scene(comp, 300, 100);
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

		// if an invalid grade is entered launch popup
		else if(gradeInt > 2 || gradeInt < 1){
			final Stage newStage = new Stage();
			VBox comp = new VBox();
			comp.setAlignment(Pos.CENTER);
			comp.setStyle("-fx-border-style: solid;"

					                 + "-fx-border-width: 1;"

					                 + "-fx-border-color: red");

			Text nameField = new Text("Grade level must be '1' or '2'.");
			Button cont = new Button("Close");
			comp.getChildren().add(nameField);
			comp.getChildren().add(cont);
			Scene stageScene = new Scene(comp, 300, 100);
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

		// otherwise add student to the database with zero scores
		else{
			dataAccess data = new dataAccess(con);
			data.insert("INSERT INTO student (`s-user`,firstname,lastname,password,grade_level,email) VALUES ('" + unameBox.getText() + "','" + fnameBox.getText() + "','" + lnameBox.getText() + "','" + pwordBox.getText() +"','"+ glevelBox.getText() + "','" + emailBox.getText() + "');");
			data.insert("insert into progress (`s-user`, addHighestLevel, addCurrentPercent, addTimeScore, subHighestLevel, subCurrentPercent, subTimeScore, npHighestLevel, npCurrentPercent, npTimeScore) Values ('"+unameBox.getText()+"',0,0,'0:00',0,0,'0:00',0,0,'0:00');");

			application.goToHome(application.getUser());
		}
	}

	// handler for the back button
	@FXML void back(ActionEvent event) {
		application.goToHome(application.getUser());
	}
}


