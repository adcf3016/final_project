import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
/////////////////////////////////////////////////////////////////////
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class gameController implements EventHandler<KeyEvent>, Initializable{
	@FXML
	public ImageView player;
	@FXML
	public ImageView _bullet;
	@FXML
	public AnchorPane _field;
	
	LinkedList<ImageView> _bullets = new LinkedList<ImageView>();
	
	
	private double t = 0;
	private double time = 0;
	private boolean moveLeft = false;
	private boolean moveRight = false;
	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean shooting = false;

	/////////////////////////////////////////////////////////////////
	final DoubleProperty Velocity = new SimpleDoubleProperty();    
	final LongProperty lastUpdateTime = new SimpleLongProperty();  
	//////////////////////////////////////////////////////////////////
	
	
	
	private void update() {
		t += 0.016;
		if(time > 1) {
			time = 0;
		}
		if(moveUp) {
			if(player.getLayoutY() - 5 > 0) {
				player.setLayoutY(player.getLayoutY() - 5);
			}
		}
		if(moveDown) {
			if(player.getLayoutY() + 5 + player.getFitHeight() < _field.getHeight()){
				player.setLayoutY(player.getLayoutY() + 5);
			}
		}
		if(moveLeft) {
			if(player.getLayoutX() - 5 > 0){
				player.setLayoutX(player.getLayoutX() - 5);
			}
		}
		if(moveRight) {
			if(player.getLayoutX() + 5 + player.getFitWidth() < _field.getWidth()) {
				player.setLayoutX(player.getLayoutX() + 5);
			}
		}
		if(shooting && time == 0) {
			ImageView newBullet = new ImageView(_bullet.getImage());
			newBullet.setLayoutX(player.getLayoutX() + player.getFitWidth() / 2 - _bullet.getFitWidth() / 2);
			newBullet.setLayoutY(player.getLayoutY() - player.getFitHeight()/2);
			_bullets.push(newBullet);
			_field.getChildren().add(newBullet);
		}
		if(t > 1) {
			t = 0;
		}
		time += 0.25;
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
		if(input.getCode() == KeyCode.Z) {
			shooting = true;
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
		if(e.getCode() == KeyCode.Z) {
			shooting = false;
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
		
		Timeline fps = new Timeline(new KeyFrame(Duration.millis(1000/60),(e)-> {
			ArrayList<ImageView> tBullets = new ArrayList<ImageView>(_bullets);
			for(var b : tBullets) {
				b.setLayoutY(b.getLayoutY() - 5);
				if(b.getLayoutY() > _field.getHeight()) {
					_bullets.remove(b);
					_field.getChildren().remove(b);
				}
			}
		}));
		fps.setCycleCount(Timeline.INDEFINITE);
		fps.play();
	}
}
