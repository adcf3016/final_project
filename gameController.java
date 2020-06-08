import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
	public ImageView _bossBullet1;
	@FXML
	public AnchorPane _field;
	@FXML
	public Label _bossHp;
	
    LinkedList<Bullet_1> boss_bullets1 = new LinkedList<Bullet_1>();    //type1子彈
    LinkedList<Bullet_2> boss_bullets2 = new LinkedList<Bullet_2>();	//type2子彈
    LinkedList<Bullet_1> boss_bullets3 = new LinkedList<Bullet_1>();	//type3子彈
    LinkedList<Bullet_1> boss_bullets4 = new LinkedList<Bullet_1>();	//type4子彈
	LinkedList<ImageView> _bullets = new LinkedList<ImageView>();
	//boss子彈  =>  地獄的開始
	
	private double t = 0;
	private double time = 0;
	private double timer = 0;
	private double atkTime = 0;
	private double boss_x;
	private double boss_y;
	private double x_bound;
	private double y_bound;
	private double atkTime1 = 0;
	
	private int spr = 0;
	private int boss_hp = 100;
	private int attakMod = 0;
	
	private boolean moveLeft = false;
	private boolean moveRight = false;
	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean shooting = false;
	private boolean dead = false;
	private boolean bossCanBeShot = true;
	private boolean randomOrNot = false;
	private boolean change = true;
	
	//boss死爽沒
	private void checkBossDead() throws IOException {
		if(boss_hp <= 0) {
			Parent win = FXMLLoader.load(getClass().getResource("winGame.fxml"));
			Scene winScene = new Scene(win);
			winScene.getRoot().requestFocus();
			Start.currentStage.setScene(winScene);
		}
	}
	
	//算兩者距離方法
	private double distance(ImageView boss_bullet, ImageView target) {
		double bullet_x = boss_bullet.getLayoutX() + boss_bullet.getFitWidth() / 2;
		double bullet_y = boss_bullet.getLayoutY() + boss_bullet.getFitHeight() / 2;
		double player_x = target.getLayoutX() + target.getFitWidth() / 2;
		double player_y = target.getLayoutY() + target.getFitHeight() / 2;
		double tmp = Math.sqrt(Math.pow(player_x - bullet_x, 2) + Math.pow(player_y - bullet_y, 2));
		return tmp;
	}
	
	//玩家死爽沒
	private void checkPlayerDead() throws IOException {
		if(dead) {
			Parent lose = FXMLLoader.load(getClass().getResource("loseGame.fxml"));
			Scene loseScene = new Scene(lose);
			Start.currentStage.setScene(loseScene);
		}
	}
	
	
	//持續施工中
	private void bossAttak() {
		switch(attakMod) {
		case 1:
			if(atkTime1 == 1.5 && timer <= 8 ) {
				for(int i = 0; i < 32; ++i) {
					double degree = (Math.PI / 16) * i;
					ImageView new_bossbullet = new ImageView(_bossBullet1.getImage());
					new_bossbullet.setLayoutX(_boss.getLayoutX() + _boss.getFitWidth() / 2 + Math.cos(degree) * _boss.getFitWidth());
					new_bossbullet.setLayoutY(_boss.getLayoutY() + _boss.getFitHeight() / 2 + Math.sin(degree) * _boss.getFitHeight());
					_field.getChildren().add(new_bossbullet);
					Bullet_1 new_b1 = new Bullet_1(new_bossbullet, degree);
					boss_bullets1.push(new_b1);
				}
			}
			break;
		case 2:
			if(atkTime1 == 2 && timer <= 8 ) {
				for(int i = 0; i < 32; ++i) {
					double degree = (Math.PI / 16) * i;
					ImageView new_bossbullet = new ImageView(_bossBullet1.getImage());
					new_bossbullet.setLayoutX(_boss.getLayoutX() + _boss.getFitWidth() / 2 + Math.cos(degree) * _boss.getFitWidth());
					new_bossbullet.setLayoutY(_boss.getLayoutY() + _boss.getFitHeight() / 2 + Math.sin(degree) * _boss.getFitHeight());
					_field.getChildren().add(new_bossbullet);
					Bullet_1 new_b4 = new Bullet_1(new_bossbullet, degree);
					boss_bullets4.push(new_b4);
				}
			}
			break;
		case 3:
			if(atkTime1 == 1 && timer <= 10) {
				for(int i = 0; i < 24; i++) {
					double degree = (Math.PI / 12) * (i + spr * 1.17);
					ImageView new_bossbullet = new ImageView(_bossBullet1.getImage());
					new_bossbullet.setLayoutX(_boss.getLayoutX() + _boss.getFitWidth() / 2 + Math.cos(degree) * _boss.getFitWidth());
					new_bossbullet.setLayoutY(_boss.getLayoutY() + _boss.getFitHeight() / 2 + Math.sin(degree) * _boss.getFitHeight());
					_field.getChildren().add(new_bossbullet);
					Bullet_1 new_b3 = new Bullet_1(new_bossbullet, degree);
					boss_bullets3.push(new_b3);
				}
				spr++;
			}
			break;
		case 4:
			if(atkTime ==  0 ) {
				ImageView new_bossbullet_type2 = new ImageView(_bossBullet1.getImage());
				new_bossbullet_type2.setLayoutX(_boss.getLayoutX() + _boss.getFitWidth() / 2);
				new_bossbullet_type2.setLayoutY(_boss.getLayoutY() + _boss.getFitHeight());
				double _x = (player.getLayoutX() + player.getFitWidth() / 2 - new_bossbullet_type2.getLayoutX()) / distance(new_bossbullet_type2, player);
				double _y = (player.getLayoutY() + player.getFitHeight() / 2 - new_bossbullet_type2.getLayoutY()) / distance(new_bossbullet_type2, player);
				_field.getChildren().add(new_bossbullet_type2);
				Bullet_2 new_Bullet_2 = new Bullet_2(new_bossbullet_type2, _x, _y);
				boss_bullets2.push(new_Bullet_2);
			}
			break;
		}
	}
	
	//boss閃現
	private void bossMovement() {
		
		x_bound = _field.getWidth();
		y_bound = _field.getHeight() / 2;
		if(timer < 3) {
			bossCanBeShot = false;
			_boss.setOpacity(_boss.getOpacity() - 0.05);
			if(_boss.getOpacity() < 0) {
				_boss.setOpacity(0);
			}
			if(timer > 2 && !randomOrNot) {
				boss_x = Math.random() * (x_bound + 1);
				boss_y = Math.random() * (y_bound + 1);
				_boss.setLayoutX(boss_x);
				_boss.setLayoutY(boss_y);
				randomOrNot = true;
			}
			if(!change) {
				change = true;
			}
		}
		if(timer > 3 && timer < 10) {
			bossCanBeShot = true;
			_boss.setOpacity(_boss.getOpacity() + 0.05);
			if(_boss.getOpacity() > 1) {
				_boss.setOpacity(1);
			}
			bossAttak();
			randomOrNot = false;
		}
		if(boss_hp < 50 && change) {
			attakMod = ThreadLocalRandom.current().nextInt(1, 4);
			change = false;
		}
}
	
	//條件判斷場所
	private void update() {
		//計時區
		t += 0.016;
		time += 0.25;
		timer += 0.025;
		atkTime += 0.25;
		atkTime1 += 0.25;
		
		//彈幕區
		ArrayList<Bullet_1> t1_bullets = new ArrayList<Bullet_1>(boss_bullets1);
		for(var b : t1_bullets) {
			b.get_bullet().setLayoutX(b.get_bullet().getLayoutX() + 5 * Math.cos(b.get_rad()));
			b.get_bullet().setLayoutY(b.get_bullet().getLayoutY() + 5 * Math.sin(b.get_rad()));
			double x_check_1 = b.get_bullet().getLayoutX() + b.get_bullet().getFitWidth() / 2;
			double y_check_1 = b.get_bullet().getLayoutY() + b.get_bullet().getFitHeight() / 2;
			if(x_check_1 < 0 || y_check_1 < 0 || x_check_1 > _field.getWidth() || y_check_1 > _field.getHeight()) {
				boss_bullets1.remove(b);
				_field.getChildren().remove(b.get_bullet());
			}
		}
		
		ArrayList<Bullet_2> t2_bullets = new ArrayList<Bullet_2>(boss_bullets2);
		for(var c : t2_bullets) {
			c.get_bullet().setLayoutX(c.get_bullet().getLayoutX() + c.get_x_v() * 7);
			c.get_bullet().setLayoutY(c.get_bullet().getLayoutY() + c.get_y_v() * 7);
			if(c.get_bullet().getLayoutX() < 0 || c.get_bullet().getLayoutX() > _field.getWidth() + 10 || c.get_bullet().getLayoutY() < 0 || c.get_bullet().getLayoutY() > _field.getHeight() + 10) {
				boss_bullets2.remove(c);
				_field.getChildren().remove(c.get_bullet());
			}
		}
		
		ArrayList<Bullet_1> t3_bullets = new ArrayList<Bullet_1>(boss_bullets3);
		for(var d : t3_bullets) {
			d.get_bullet().setLayoutX(d.get_bullet().getLayoutX() + 5 * Math.cos(d.get_rad()));
			d.get_bullet().setLayoutY(d.get_bullet().getLayoutY() + 5 * Math.sin(d.get_rad()));
			double x_check_1 = d.get_bullet().getLayoutX() + d.get_bullet().getFitWidth() / 2;
			double y_check_1 = d.get_bullet().getLayoutY() + d.get_bullet().getFitHeight() / 2;
			if(x_check_1 < 0 || y_check_1 < 0 || x_check_1 > _field.getWidth() || y_check_1 > _field.getHeight()) {
				boss_bullets1.remove(d);
				_field.getChildren().remove(d.get_bullet());
			}
		}
		
		ArrayList<Bullet_1> t4_bullets = new ArrayList<Bullet_1>(boss_bullets4);
		for(var b : t4_bullets) {
			b.get_bullet().setLayoutX(b.get_bullet().getLayoutX() + 5 * Math.cos(b.get_rad()));
			b.get_bullet().setLayoutY(b.get_bullet().getLayoutY() - 5 * Math.sin(b.get_rad()));
			double x_check_1 = b.get_bullet().getLayoutX() + b.get_bullet().getFitWidth() / 2;
			double y_check_1 = b.get_bullet().getLayoutY() + b.get_bullet().getFitHeight() / 2;
			if(x_check_1 < 0 || y_check_1 < 0 || x_check_1 > _field.getWidth() || y_check_1 > _field.getHeight()) {
				boss_bullets1.remove(b);
				_field.getChildren().remove(b.get_bullet());
			}
		}
		
		
		//死亡判定
		for(var b : t1_bullets) {
			if(distance(b.get_bullet(), player) < player.getFitWidth() / 5) {
				dead = true;
			}
		}
		
		for(var c : t2_bullets) {
			if(distance(c.get_bullet(), player) < player.getFitWidth() / 5) {
				dead = true;
			}
		}
		
		for(var d : t3_bullets) {
			if(distance(d.get_bullet(), player) < player.getFitWidth() / 5) {
				dead = true;
			}
		}
		
		for(var b : t4_bullets) {
			if(distance(b.get_bullet(), player) < player.getFitWidth() / 5) {
				dead = true;
			}
		}
		
		
		//操作區
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
		if(shooting && time == 1) {
			ImageView newBullet = new ImageView(_bullet.getImage());
			newBullet.setLayoutX(player.getLayoutX() + player.getFitWidth() / 2 - _bullet.getFitWidth() / 2);
			newBullet.setLayoutY(player.getLayoutY() - player.getFitHeight()/2);
			_bullets.push(newBullet);
			_field.getChildren().add(newBullet);
		}
		
		//重設時間區
		if(t > 1) {
			t = 0;
		}
		if(timer > 12) {
			timer = 0;
		}
		if(atkTime > 1.5) {
			atkTime = 0;
		}
		if(time > 1) {
			time = 0;
		}
		if(atkTime1 > 3) {
			atkTime1 = 0;
		}
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
		attakMod = 4;
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
						String output = "Boss Hp:";				
						String repeated = "=".repeat(boss_hp);
						output += repeated;
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
