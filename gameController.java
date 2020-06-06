import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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
	public ImageView _boss;
	@FXML
	public AnchorPane _field;
	@FXML
	public Label _bossHp;
	
	LinkedList<ImageView> _bullets = new LinkedList<ImageView>();
	//boss子彈  =>  地獄的開始
	
	private double t = 0;
	private double time = 0;
	private double timer = 0;
	private double boss_x;
	private double boss_y;
	private double x_bound;
	private double y_bound;
	
	private int boss_hp = 100;
	
	private boolean moveLeft = false;
	private boolean moveRight = false;
	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean shooting = false;
	private boolean dead = false;
	private boolean bossCanBeShot = true;
	private boolean randomOrNot = false;
	
	//boss死爽沒
	private void checkBossDead() throws IOException {
		if(boss_hp <= 0) {
			Parent win = FXMLLoader.load(getClass().getResource("winGame.fxml"));
			Scene winScene = new Scene(win);
			winScene.getRoot().requestFocus();
			Start.currentStage.setScene(winScene);
		}
	}
	
	//玩家死爽沒
	private void checkPlayerDead() throws IOException {
		if(dead) {
			Parent lose = FXMLLoader.load(getClass().getResource("loseGame.fxml"));
			Scene loseScene = new Scene(lose);
			loseScene.getRoot().requestFocus();
			Start.currentStage.setScene(loseScene);
		}
	}
	
	//boss閃現
	private void bossMovement() {
		x_bound = _field.getWidth();
		y_bound = _field.getHeight() / 2;
		if(timer < 5) {
			bossCanBeShot = false;
			_boss.setOpacity(_boss.getOpacity() - 0.05);
			if(_boss.getOpacity() < 0) {
				_boss.setOpacity(0);
			}
			if(timer > 4 && !randomOrNot) {
				boss_x = Math.random() * (x_bound + 1);
				boss_y = Math.random() * (y_bound + 1);
				_boss.setLayoutX(boss_x);
				_boss.setLayoutY(boss_y);
				randomOrNot = true;
			}
		}
		if(timer > 5 && timer < 10) {
			bossCanBeShot = true;
			_boss.setOpacity(_boss.getOpacity() + 0.05);
			if(_boss.getOpacity() > 1) {
				_boss.setOpacity(1);
			}
			randomOrNot = false;
		}
}
	
	//條件判斷場所
	private void update() {
		t += 0.016;
		timer += 0.025;
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
		if(timer > 12) {
			timer = 0;
		}
		time += 0.25;
	}
	
	//玩家操作
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


	//初始化
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
				if(b.getLayoutY() < 0) {
					_bullets.remove(b);
					_field.getChildren().remove(b);
				}
				if(bossCanBeShot && b.getLayoutY() < _boss.getLayoutY() + _boss.getFitHeight() && b.getLayoutY() > _boss.getLayoutY()) {
					if(b.getLayoutX() < _boss.getLayoutX() + _boss.getFitWidth() - 1 && b.getLayoutX() + b.getFitWidth() + 14> _boss.getLayoutX()) {
						_bullets.remove(b);
						_field.getChildren().remove(b);
						boss_hp -= 1;
						if(boss_hp < 0) {
							boss_hp = 0;
						}
						String output = "Boss Hp:";				//這裡非常危險
						for(int i = 0; i < boss_hp; i++) {
							output += "=";
						}
						_bossHp.setText(output);
					}
				}
			}
			
			bossMovement();
			
			try {
				checkBossDead();
			} catch (IOException x) {
				// TODO Auto-generated catch block
				x.printStackTrace();
			}
			
			try {
				checkPlayerDead();
			} catch (IOException x) {
				// TODO Auto-generated catch block
				x.printStackTrace();
			}
		}));
		fps.setCycleCount(Timeline.INDEFINITE);
		fps.play();
		
		
		/*PathTransition transition = new PathTransition();//施工
		transition.setNode(_bullet);
		transition.setDuration(Duration.millis(2000));
		transition.setPath(null);//施工*/
	}
}
