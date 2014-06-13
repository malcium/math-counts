/* Controller for the teacher home page
 * Teacher_Home.fxml
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomeScreenControl extends AnchorPane implements Initializable{
	private Main application;
	private String user;
	private ObservableList<String> items;
	private Connection con; 	

	// constructs the class with the connection and user data
	public void setApp(Main application){
		this.application = application;
		this.con = application.getConnection();
		this.user = application.getUser();	
		dataAccess data = new dataAccess(con);
		items = data.teacherHome(user);	
		Collections.sort(items, String.CASE_INSENSITIVE_ORDER);		
		firstName.setItems(items);
		welcome.setText("Student Accounts:");
		unameBox.setEditable(false);
	}

	@FXML ListView<String> firstName;
	@FXML Text welcome;
	@FXML TextField fnameBox;	
	@FXML TextField lnameBox;
	@FXML TextField glevelBox;
	@FXML TextField emailBox;
	@FXML TextField unameBox;
	@FXML TextField pwordBox;

	@FXML private Label addLevel;
	@FXML private Label addScore;
	@FXML private Label addTime;
	@FXML private Label npLevel;
	@FXML private Label npTime;
	@FXML private Label subScore;
	@FXML private Label subTime;
	@FXML private Label npScore;
	@FXML private Label subLevel;

	// initializes the view, sets up a listener on the mouse to define what happens when you click a student name in the ListView
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// An override needed to add listener and events for mouse clicks on ListView object
		// On left-hand side of the screen.
		firstName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {				

				fnameBox.setText(newValue);
				dataAccess data = new dataAccess(con);
				String[] array = data.getStudent(newValue);

				lnameBox.setText(array[1]);
				glevelBox.setText(array[2]);
				emailBox.setText(array[3]);
				unameBox.setText(array[4]);
				pwordBox.setText(array[5]);

				String[] array2 = data.gameHome(unameBox.getText());
				addLevel.setText(array2[2]);
				addScore.setText(array2[3] + "0%");
				String addDate = array2[4].substring(0,2) + "/" + array2[4].substring(3,5) + "/" + array2[4].substring(6,8);
				addTime.setText(addDate);
				subLevel.setText(array2[5]);
				subScore.setText(array2[6] + "0%");
				String subDate = array2[7].substring(0,2) + "/" + array2[7].substring(3,5) + "/" + array2[7].substring(6,8);
				subTime.setText(subDate);
				npLevel.setText(array2[8]);
				npScore.setText(array2[9] + "0%");
				String npDate = array2[10].substring(0,2) + "/" + array2[10].substring(3,5) + "/" + array2[10].substring(6,8);
				npTime.setText(npDate);
			}
		});
	}

	// handler for the save edits button on the teacher home
	@FXML public void save(ActionEvent event){
		String firstname = fnameBox.getText();
		String lastname = lnameBox.getText();
		String uname = unameBox.getText();
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
			comp.setSpacing(2);
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

		// if the fields are empty, launch popup warning
		if(firstname.equals("") || lastname.equals("") || uname.equals("") || email.equals("") || grade.equals("") || password.equals("")){
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
			comp.setSpacing(2);
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
			comp.setSpacing(2);
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

		// otherwise update the student information in the database
		else{
			dataAccess data = new dataAccess(con);		
			String username = unameBox.getText();		
			data.update("update student set firstname ='"+ fnameBox.getText()+"' where `s-user` = '"+username+"';");
			data.update("update student set lastname ='"+ lnameBox.getText()+"' where `s-user` = '"+username+"';");
			data.update("update student set grade_level ='"+ glevelBox.getText()+"' where `s-user` = '"+username+"';");
			data.update("update student set email ='"+ emailBox.getText()+"' where `s-user` = '"+username+"';");
			data.update("update student set password ='"+ pwordBox.getText()+"' where `s-user` = '"+username+"';");
			application.goToHome(user);
		}
	}

	// handler for the delete student button on the teacher home
	@FXML public void delete(ActionEvent event){
		// launch delete confirmation pop window
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
		comp.setSpacing(2);
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
				String username = unameBox.getText();
				String first = fnameBox.getText();

				data.delete("delete from progress where `s-user` = '" +username+ "' limit 1;");
				data.delete("delete from teacher_has_student where `s-user` = '"+username+ "'limit 1;");
				data.delete("delete from student where `s-user` = '" +username+ "' and firstname = '" +first+ "' limit 1;");

				application.goToHome(user);
			}			
		});		
	}

	// handler for the add new student button on teacher home
	@FXML public void addNew(ActionEvent event){
		application.goToAdd();
	}

	// handler for the logout button on teacher home
	@FXML public void logout(ActionEvent event){
		application.goToLogin();
	}

	// handler for Teacher Accounts button
	@FXML public void teacherAccounts(ActionEvent event) {
		application.goToTeacherAccounts();
	}

	// handler for Custom Problems button
	@FXML public void customProblems(ActionEvent event) {
		application.goToCustomProblems();
	}
}