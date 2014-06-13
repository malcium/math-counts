/* Controller for the custom problems page
 * Custom_Problems.fxml
 */
package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import com.mysql.jdbc.Connection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class CustomProblemsControl extends AnchorPane implements Initializable {
	private Main application;
	private String user;
	private Connection con; 
	private ObservableList<String> addition;
	private ObservableList<String> subtraction;
	private String addSelection;
	private String subSelection;

	// constructs the class
	public void setApp(Main application){
		this.application = application;
		this.user = application.getUser();
		this.con = application.getConnection();

		// get a database connection and get any custom problems, then populate the listview objects
		dataAccess data = new dataAccess(con);
		addition = data.getAddProblems();	
		subtraction = data.getSubProblems();
		addProblems.setItems(addition);
		subProblems.setItems(subtraction);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// overrides needed to add listener for mouse clicks on ListView objects
		addProblems.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {	
				addSelection = newValue;
			}
		});
		subProblems.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {	
				subSelection = newValue;
			}
		});
	}

	// listview objects on the view
	@FXML private ListView<String> subProblems;
	@FXML private ListView<String> addProblems;

	// handler for the delete addition problem button
	@FXML void deleteAdd(ActionEvent event) {
		if(addSelection != null){
			StringTokenizer st= new StringTokenizer(addSelection);   
			String first = (String)st.nextElement();			
			st.nextElement();
			String second = (String)st.nextElement();			
			dataAccess data = new dataAccess(con);
			data.delete("delete from addproblems where addfirst = '" +first+ "' and addsecond = '" +second+ "' limit 1;");
			application.goToCustomProblems();
		}
	}

	// handler for the delete subtraction problem button
	@FXML void deleteSub(ActionEvent event) {
		if(subSelection != null){
			StringTokenizer st= new StringTokenizer(subSelection);   
			String first = (String)st.nextElement();
			st.nextElement();
			String second = (String)st.nextElement();
			dataAccess data = new dataAccess(con);
			data.delete("delete from subproblems where subfirst = '" +first+ "' and subsecond = '" +second+ "' limit 1;");
			application.goToCustomProblems();
		}
	}

	// handler for the add new problems button
	@FXML void addNew(ActionEvent event) {
		application.goToAddProblems();
	}

	// handler for the back button
	@FXML void back(ActionEvent event) {
		application.goToHome(user);
	}
}
