import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
/////////////////////////////////////////////////////////////////////
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class gameController implements EventHandler<KeyEvent>, Initializable{
	@FXML
	public ImageView player;
	
	
	private double t = 0;
	private boolean moveLeft = false;
	private boolean moveRight = false;
	private boolean moveUp = false;
	private boolean moveDown = false;

	/////////////////////////////////////////////////////////////////
	final DoubleProperty Velocity = new SimpleDoubleProperty();    
	final LongProperty lastUpdateTime = new SimpleLongProperty();  
	//////////////////////////////////////////////////////////////////
	
	
	
	private void update() {
		t += 0.016;
		if(moveUp) {
			player.setLayoutY(player.getLayoutY() - 5);
		}
		if(moveDown) {
			player.setLayoutY(player.getLayoutY() + 5);
		}
		if(moveLeft) {
			player.setLayoutX(player.getLayoutX() - 5);
		}
		if(moveRight) {
			player.setLayoutX(player.getLayoutX() + 5);
		}
		if(t > 2) {
			t = 0;
		}
	}
	
	@Override
	public void handle(KeyEvent input) {
		// TODO Auto-generated method stub
		if(input.getCode() == KeyCode.LEFT) {
			moveLeft = true;
		}
		if(input.getCode() == KeyCode.RIGHT) {
			moveRight = true;
		}
		if(input.getCode() == KeyCode.UP) {
			moveUp = true;
		}
		if(input.getCode() == KeyCode.DOWN) {
			moveDown = true;
		}
		
	}
	
	
	public void release(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getCode() == KeyCode.LEFT) {
			moveLeft = false;
		}
		if(e.getCode() == KeyCode.RIGHT) {
			moveRight = false;
		}
		if(e.getCode() == KeyCode.UP) {
			moveUp = false;
		}
		if(e.getCode() == KeyCode.DOWN) {
			moveDown = false;
		}
	}



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		final AnimationTimer playerAnimation = new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
				update();
			}
		};
		playerAnimation.start();
	}
}
