import javafx.scene.image.ImageView;

public class Bullet_2 {
	private double x_v;
	private double y_v;
	private ImageView bullet;
	
	public Bullet_2() {}
	
	public Bullet_2(ImageView bullet, double x_v, double y_v) {
		this.bullet = bullet;
		this.x_v = x_v;
		this.y_v = y_v;
	}
	
	public ImageView get_bullet() {
		return bullet;
	}
	
	public double get_x_v() {
		return x_v;
	}
	
	public double get_y_v() {
		return y_v;
	}
}
