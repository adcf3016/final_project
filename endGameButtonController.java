import javafx.fxml.FXML;

public class endGameButtonController {
	@FXML
	public void onExitPressed() {
		Start.currentStage.close();
	}
}
