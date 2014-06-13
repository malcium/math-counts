package application;

import static org.junit.Assert.*;
import java.sql.DriverManager;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

import com.mysql.jdbc.Connection;

import org.junit.Test;

public class Tests {

    @Test // a test to ensure proper application launch
    public void startApp() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        new Main().start(new Stage()); 
                        
                    }
                });
            }
        });
        thread.start();// Initialize the thread
        Thread.sleep(10000); 
    }
    
    @Test // test for proper database functionality
    public void dataTest(){
    	String url;
		String username="mmccoy";
		String password="aic4077";
		String port="3306";
		String database="mathcounts";
		url = "jdbc:mysql://localhost:" + port + "/" + database + "?user="+ username + "&password=" +password;
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = (Connection) DriverManager.getConnection(url);
			if(!con.isClosed())
				System.out.println("Successfully connected to MySQL server...");
		} 
		catch(Exception b) {
			System.err.println("Exception: " + b.getMessage());
		}
    	
    	dataAccess data = new dataAccess(con);
    	data.insert("INSERT INTO teacher (`t-user`,firstname,lastname,password,email) VALUES ('m','o','r','g','a');");
    	String[] returnArray = data.getTeacher("o");
    	assertEquals(returnArray[0], "r");
    	assertEquals(returnArray[1], "a");
    	assertEquals(returnArray[2], "m");
    	assertEquals(returnArray[3], "g");
    	data.delete("Delete from teacher where `t-user` = 'm' limit 1;");
    }
}