package elements;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class WinningStage {
    private StackPane pane;
    private Scene scene;
    private GraphicsContext gc;
    private Canvas canvas;
    private Character character;

    WinningStage(Character character, Stage stage) {
        this.character = character;
        this.pane = new StackPane(); // holds the graphical element
        this.scene = new Scene(pane, GameStage.GAME_WIDTH, GameStage.GAME_HEIGHT, Color.WHITE);
        this.canvas = new Canvas(GameStage.GAME_WIDTH, GameStage.GAME_HEIGHT); // blank page for drawing/importing images
        this.gc = canvas.getGraphicsContext2D();
        this.setProperties();
    }

    private void setProperties() {
        Image winImage = null;
        switch (this.character.getName()) {    // checks which character won
            case GameStage.BLOSSOM:
                winImage = GameStage.WIN_BLOSSOM;
                break;
            case GameStage.BUBBLES:
                winImage = GameStage.WIN_BUBBLES;
                break;
            case GameStage.BUTTERCUP:
                winImage = GameStage.WIN_BUTTERCUP;
                break;
            case "DRAW":
            	winImage = GameStage.DRAW;
        }
        if (winImage != null) {
            this.gc.drawImage(winImage, 0, 0);
        }
        pane.getChildren().add(this.canvas);
    }

    Scene getScene() {
        return this.scene;
    }
}
