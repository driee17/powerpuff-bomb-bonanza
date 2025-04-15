package user;
import elements.GameStage;

import javafx.stage.Stage;
import javafx.application.Application;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args); //starting the JavaFX application
	}

	//application is prepared for use
	public void start(Stage stage)
    {
       GameStage game = new GameStage();
       game.setStage(stage);
    }

}
