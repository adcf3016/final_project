import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class loseGame implements EventHandler<KeyEvent> {

	@Override
	public void handle(KeyEvent input) {
		// TODO Auto-generated method stub
		if(input.getCode() != null) {
			Start.currentStage.close();
		}
	}

}
