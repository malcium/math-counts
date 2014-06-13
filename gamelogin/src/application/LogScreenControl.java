/* Controller for the login screen
 * Login_Screen.fxml
 */

package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class LogScreenControl extends GridPane implements Initializable{
	private Main application;

	// objects and event handler for the login screen
	@FXML private Text actiontarget;
	@FXML private TextField userName;
	@FXML private PasswordField passwordField;
	@FXML private GridPane anchor;

	// constructs the class
	public void setApp(Main application){
		this.application = application;
	}

	// initializes the view, username prompt text not working for some reason
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		userName.setPromptText("username");
		passwordField.setPromptText("password");
	}

	// handler for login button
	@FXML public void handleLogin(ActionEvent event) {
		if(application == null){
			actiontarget.setText("Hello " + userName.getText());
		}

		else{ 
			if (!application.userLogin(userName.getText(), passwordField.getText()))  // 
				actiontarget.setText("Login Failure!"); 
		}
	}
}
