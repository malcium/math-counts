/* Controller for the retry level page
 * Retry_Level.fxml
 */
package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class RetryLevelControl extends AnchorPane implements Initializable{
	private Main application;
	private int label;
	private Image graphic; 
	private Media sound;

	public void setApp(Main application){
		this.application = application;
		this.label = application.getTempScore();
		this.graphic = new Image(getClass().getResourceAsStream("retry.png"));
		this.sound = new Media(getClass().getResource("retry.mp3").toString());
		score.setText("You scored a "+ label + "0%. An 80% or higher needed to pass a level.");
		image.setImage(graphic);
		playSound();
	}

	// view objects to be set
	@FXML private ImageView image;
	@FXML private Label score;

	// media player for the sound effects
	private void playMedia(Media m){
		if (m != null){
			MediaPlayer mp = new MediaPlayer(m);
			mp.play();
		}
	}

	// the retry level sound
	public void playSound(){
		try{
			playMedia(sound);
		}
		catch(Exception ex){
			System.err.print(ex);
		}
	}


	@FXML void greatJob(ActionEvent event) {
		application.goToLevel();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}
}
