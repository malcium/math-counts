/* Controller for the level up page
 * Level_Up.fxml
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

public class LevelUpControl extends AnchorPane implements Initializable{
	private int scoreLabel;
	private Main application;
	private Image correctGraphic;
	private Media sound;

	// constructs the class
	public void setApp(Main application){
		this.application = application;
		this.correctGraphic = new Image(getClass().getResourceAsStream("firework.gif"));
		this.sound = new Media(getClass().getResource("level_up.mp3").toString());
		this.scoreLabel = application.getTempScore();
		score.setText("You scored "+ scoreLabel + "0%!");
		image.setImage(correctGraphic);
		playSound();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	// media player for the sound effects
	private void playMedia(Media m){
		if (m != null){
			MediaPlayer mp = new MediaPlayer(m);
			mp.play();
		}
	}

	// the correct answer level up sound
	public void playSound(){
		try{
			playMedia(sound);
		}
		catch(Exception ex){
			System.err.print(ex);
		}
	}

	// view object to be set
	@FXML private ImageView image;
	@FXML private Label score;

	// handler for the great job button
	@FXML void greatJob(ActionEvent event) {
		application.goToLevel();
	}
}

