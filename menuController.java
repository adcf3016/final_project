import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class menuController {
	@FXML
	public void onStartPressed() throws IOException{
		Parent game = FXMLLoader.load(getClass().getResource("game.fxml"));
		Scene gameScene = new Scene(game);
		gameScene.getRoot().requestFocus();
		Start.currentStage.setScene(gameScene);
	}
	
	public void onExitPressed() {
		Start.currentStage.close();
	}
}
