import javafx.scene.image.ImageView;

public class Bullet_1 {
	private double rad;
	private ImageView bullet;
	
	public Bullet_1() {}
	
	public Bullet_1(ImageView bullet, double rad) {
		this.rad = rad;
		this.bullet = bullet;
	}
	
	public ImageView get_bullet() {
		return bullet;
	}
	
	public double get_rad() {
		return rad;
	}
}
