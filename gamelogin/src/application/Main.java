/* Drew Wyand, Bart Nadala, Morgan McCoy
 * CMPT 322
 * Math Counts Project
 * 
 * Main class to launch the login GUI and connect to game database
 */

package application;

// imports for MySQL to access game database, currently hosted on localhost
import java.io.InputStream;
import java.sql.*;
import com.mysql.jdbc.Connection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class Main extends Application {
	private Stage mainStage;
	private Connection con;
	private String user;
	private int subject;
	private int level;
	int counter = 0;
	private int tempScore;

	// getters and setters for user information and database connection
	public Connection getConnection(){
		return this.con;
	}
	public Stage getStage(){
		return this.mainStage;
	}
	public String getUser(){
		return this.user;
	}
	public void setLevel(int l){
		this.level = l;
	}
	public void setSubject(int s){
		this.subject = s;
	}
	public void setTempScore(int s){
		this.tempScore = s;
	}
	public int getTempScore(){
		return this.tempScore;
	}
	public int getLevel(){
		return this.level;
	}
	public int getSubject(){
		return this.subject;
	}

	@Override //invoked by main to start application
	public void start(Stage primaryStage) {
		JDBCAccess(); // constructs the database connection
		mainStage = primaryStage;  // constructs the stage
		try {

			// The title of the main windows of the GUI
			mainStage.setTitle("Math Counts");		
			goToLogin();
			
			// Launch popup window warning when user tries to exit the program by closing the window
			// applies to all scenes in the JavaFX side of the game
			Platform.setImplicitExit(false);
			mainStage.setOnCloseRequest(new EventHandler<WindowEvent>(){

				@Override 
				public void handle(WindowEvent event){
					event.consume();
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
					comp.setSpacing(4);
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
							try {
								con.close();
								System.out.println("Successfully disconnected from MySQL server.");
								System.exit(0);
							} 
							catch (SQLException e) {
								e.printStackTrace();
							}
						}			
					});		
				}	
			});
			mainStage.show();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	// connects to the database and constructs the connection (con)
	public void JDBCAccess(){
		String url;
		String username="mmccoy";
		String password="aic4077";
		String port="3306";
		String database="mathcounts";
		url = "jdbc:mysql://localhost:" + port + "/" + database + "?user="+ username + "&password=" +password;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = (Connection) DriverManager.getConnection(url);
			if(!con.isClosed())
				System.out.println("Successfully connected to MySQL server...");
		} 
		catch(Exception b) {
			System.err.println("Exception: " + b.getMessage());
		}
	}  

	// loads the login screen onto the stage
	public void goToLogin() {
		try {
			LogScreenControl login = (LogScreenControl) swapScene("Login_Screen.fxml");
			login.setApp(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// loads the teacher home page
	public void goToHome(String user) {
		try {
			HomeScreenControl home = (HomeScreenControl) swapScene("Teacher_Home.fxml");			
			home.setApp(this);			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// loads the add student page
	public void goToAdd() {
		try {
			AddStudentControl addStudent = (AddStudentControl) swapScene("Student_Info.fxml");				
			addStudent.setApp(this);				
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// loads the add teacher page
	public void goToAddTeacher() {
		try {
			AddTeacherControl addTeacher = (AddTeacherControl) swapScene("Teacher_Info.fxml");				
			addTeacher.setApp(this);				
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// loads the student's game home page
	public void goToGame() {
		try {
			GameScreenControl game = (GameScreenControl) swapScene("Game_Home.fxml");				
			game.setApp(this);				
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// loads the level up view
	public void goToLevel() {
		try {
			GameLevelControl level = (GameLevelControl) swapScene("Game_Level.fxml");				
			level.setApp(this);				
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// loads the simple addition/subtraction game view
	public void goToSimplePlay() {
		try {
			SimplePlayControl play = (SimplePlayControl) swapScene("Simple_Play.fxml");				
			play.setApp(this);				
		} 
		catch (Exception e) {
			e.printStackTrace();
		}		
	}

	// loads the place value game view
	public void goToPlaceValuePlay() {
		try {
			PlaceValuePlayControl accounts = (PlaceValuePlayControl) swapScene("PlaceValue_Play.fxml");				
			accounts.setApp(this);				
		} 
		catch (Exception e) {
			e.printStackTrace();
		}		
	}

	// loads the teacher accounts page
	public void goToTeacherAccounts() {
		try {
			TeacherAccountsControl accounts = (TeacherAccountsControl) swapScene("Teacher_Accounts.fxml");				
			accounts.setApp(this);				
		} 
		catch (Exception e) {
			e.printStackTrace();
		}		
	}

	// loads the level up page
	public void goToLevelUp() {
		try {
			LevelUpControl accounts = (LevelUpControl) swapScene("Level_Up.fxml");				
			accounts.setApp(this);				
		} 
		catch (Exception e) {
			e.printStackTrace();
		}		
	}	

	// loads the retry level page
	public void goToRetryLevel() {
		try {
			RetryLevelControl accounts = (RetryLevelControl) swapScene("Retry_Level.fxml");				
			accounts.setApp(this);				
		} 
		catch (Exception e) {
			e.printStackTrace();
		}		
	}	

	// loads the custom problems page
	public void goToCustomProblems() {
		try {
			CustomProblemsControl accounts = (CustomProblemsControl) swapScene("Custom_Problems.fxml");				
			accounts.setApp(this);				
		} 
		catch (Exception e) {
			e.printStackTrace();
		}		
	}	

	// loads the add problems page
	public void goToAddProblems() {
		try {
			AddProblemsControl accounts = (AddProblemsControl) swapScene("Add_Problems.fxml");				
			accounts.setApp(this);				
		} 
		catch (Exception e) {
			e.printStackTrace();
		}		
	}	

	// performs the login and differentiates between student and teacher
	// users allowed four times to login
	public boolean userLogin(String userId, String password){
		dataAccess data = new dataAccess(con);  // create a new data access object to access SQL DB
		if (data.login(userId, password) == 1) {
			this.user = userId;
			goToHome(userId);
			return true;
		} 
		else if(data.login(userId, password) == 2){
			this.user = userId;
			goToGame();
			return true;
		}
		else {
			// allow four login attempts
			counter++;
			if (counter >= 4)
				System.exit(0);	
			return false;
		}
	}

	// method to switch javafx scenes
	private Initializable swapScene(String fxml) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		//Parent root = FXMLLoader.load(getClass().getResource(fxml));
		InputStream input = Main.class.getResourceAsStream(fxml);
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		loader.setLocation(Main.class.getResource(fxml));
		GridPane loginPage = null;
		AnchorPane allOthers = null;

		try {
			if (fxml.equals("Login_Screen.fxml"))
				loginPage = (GridPane) loader.load(input);
			else
				allOthers = (AnchorPane) loader.load(input);
		} 
		finally {
			input.close();
		} 
		Scene scene = null;
		if (fxml.equals("Login_Screen.fxml"))
			scene = new Scene(loginPage, 500, 500);
		else
			scene = new Scene(allOthers, 640, 490);
		mainStage.setScene(scene);
		mainStage.sizeToScene();
		return (Initializable) loader.getController();
	}

	// launches the JavaFX GUI defined in start(Application)
	public static void main(String[] args) {
		//System.setProperty("javafx.macosx.embedded", "true");
		//java.awt.Toolkit.getDefaultToolkit();
		launch(args);
	}	
}

