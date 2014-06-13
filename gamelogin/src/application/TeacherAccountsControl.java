/* Controller for the teacher accounts page
 * Teacher_Accounts.fxml
 */
package application;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TeacherAccountsControl extends AnchorPane implements Initializable {
	private Main application;
	private String user;
	private Connection con; 
	private ObservableList<String> items;

	// constructs the class
	public void setApp(Main application){
		this.application = application;
		this.user = application.getUser();
		this.con = application.getConnection();
		dataAccess data = new dataAccess(con);
		items = data.teacherAccounts(user);	
		Collections.sort(items, String.CASE_INSENSITIVE_ORDER);
		teacherList.setItems(items);
		username.setEditable(false);
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// An override needed to add listener and events for mouse clicks on ListView object
		// On left-hide side of the screen.
		teacherList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {				

				firstName.setText(newValue);
				dataAccess data = new dataAccess(con);
				String[] array = data.getTeacher(newValue);

				lastName.setText(array[0]);
				email.setText(array[1]);
				username.setText(array[2]);
				password.setText(array[3]);
			}
		});
	}
	
	// view objects to be set
	@FXML private ListView<String> teacherList;
	@FXML private TextField firstName;
	@FXML private TextField lastName;
	@FXML private TextField password;
	@FXML private TextField email;
	@FXML private TextField username;

	// handler for the back button
	@FXML void back(ActionEvent event) {
		application.goToHome(user);
	}

	// handler for the add new teacher button
	@FXML void addNewTeacher(ActionEvent event) {
		application.goToAddTeacher();
	}
	
	// handler for the save changes button
	@FXML void save(ActionEvent event) {
		String firstname = firstName.getText();
		String lastname = lastName.getText();
		String emailAdd = email.getText();
		String pword = password.getText();
		
		// if any of the editable fields are empty, prompt the user to fill out all fields
		if(firstname.equals("") || lastname.equals("") || emailAdd.equals("") || pword.equals("")){
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
		
		// otherwise, update the changes in the database
		else{
			dataAccess data = new dataAccess(con);		
			String u = username.getText();		
			data.update("update teacher set firstname ='"+ firstName.getText()+"' where `t-user` = '"+u+"';");
			data.update("update teacher set lastname ='"+ lastName.getText()+"' where `t-user` = '"+u+"';");
			data.update("update teacher set email ='"+ email.getText()+"' where `t-user` = '"+u+"';");
			data.update("update teacher set password ='"+ password.getText()+"' where `t-user` = '"+u+"';");
			application.goToTeacherAccounts();
		}
	}
	
	// handler for the delete button on the view, launch a confirmation popup
	@FXML void delete(ActionEvent event) {
		final Stage newStage = new Stage();
		VBox comp = new VBox();
		comp.setAlignment(Pos.CENTER);

		comp.setStyle("-fx-border-style: solid;"

		                 + "-fx-border-width: 1;"

		                 + "-fx-border-color: red");

		Text nameField = new Text("Are you sure?");
		Button cont = new Button("Continue");
		Button dont = new Button("No wait! Go back!");
		comp.getChildren().add(nameField);
		comp.getChildren().add(cont);
		comp.getChildren().add(dont);
		Scene stageScene = new Scene(comp, 150, 150);
		newStage.setScene(stageScene);
		newStage.show();
		dont.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				newStage.hide();				
			}
		});		
		cont.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				newStage.hide();				
				dataAccess data = new dataAccess(con);
				String user = username.getText();
				String first = firstName.getText();

				data.delete("delete from teacher_has_student where `t-user` = '"+user+ "'limit 1;");
				data.delete("delete from teacher where `t-user` = '" +user+ "' and firstname = '" +first+ "' limit 1;");

				application.goToTeacherAccounts();
			}			
		});		
	}
}

