/* Controller for the add teacher page
 * Teacher_Info.fxml
 */

package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddTeacherControl extends AnchorPane implements Initializable {
	private Main application;
	private Connection con; 

	// constructs the class
	public void setApp(Main application){
		this.application = application;
		this.con = application.getConnection();
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	// method to check for duplicate usernames in the student table
	public boolean duplicateUsername(String u){
		ArrayList<String> list;
		boolean rv = false;
		dataAccess data = new dataAccess(con);
		list = data.select("select `t-user` from teacher;");
		for(int i = 0; i < list.size(); i++){
			if(u.equals(list.get(i))){
				rv = true;
			}
		}
		return rv;
	}

	// view objects to be set
	@FXML private TextField fnameBox;
	@FXML private TextField unameBox;
	@FXML private TextField emailBox;
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
		String password = pwordBox.getText();

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
		
		// check to see if any fields are left blank, if so launch a popup
		else if(firstname.equals("") || lastname.equals("") || username.equals("") || email.equals("") || password.equals("")){
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
		else{
			dataAccess data = new dataAccess(con);
			data.insert("INSERT INTO teacher (`t-user`,firstname,lastname,password,email) VALUES ('" + unameBox.getText() + "','" + fnameBox.getText() + "','" + lnameBox.getText() + "','" + pwordBox.getText() +"','" + emailBox.getText() + "');");
			application.goToTeacherAccounts();
		}
	}

	// handler for the back button
	@FXML void back(ActionEvent event) {
		application.goToTeacherAccounts();
	}
}
