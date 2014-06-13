package application;

import java.util.ArrayList;

import javafx.collections.ObservableList;

// An interface to send SQL statements to the server
public interface SQLInterface {

	// Logs a user in, and differentiates what type of user. Returns 1 if user is a teacher
	// returns 2 if user is a student, and 0 if the user does not exist.
	public int login(String u, String p);

	// Populates the data on the teacher home page, returns a list to the controller
	public ObservableList<String> teacherHome(String u);

	// A method to fetch student details when a teacher clicks on a student
	public String[] getStudent(String u);
	
	// A method to execute a select statement on the SQL server
	public ArrayList<String> select(String s);

	// A method to execute an update statement on the SQL server
	public void update(String s);

	// A method to execute an insert statement on the SQL server
	public void insert(String s);

	// A method to execute delete statements on the SQL server
	public void delete(String s);

	// A method to fetch student data for home screen and game play
	public String[] gameHome(String u);

	// A method to return teacher account information from the MySQL database
	public ObservableList<String> teacherAccounts(String u);

	// A method to return the custom addition problems in the system
	public ObservableList<String> getAddProblems();

	// A method to return the custom addition problems in the system
	public ObservableList<String> getSubProblems();

}
