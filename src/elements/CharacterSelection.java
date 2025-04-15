package elements;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

//separate class for character selection for second player
public class CharacterSelection extends GameStage{
	Image selectedCharacterPlayer1;

	CharacterSelection(Image p1Char, Stage stage, Canvas canvas){
		this.selectedCharacterPlayer1 = p1Char;
		setupSecondPlayerSelection(stage, canvas, selectedCharacterPlayer1);
	}

    //retrieve image for Player 1 based on selected character
    public static Image getImageForPlayer1(String selectedCharacterPlayer1) {
        switch (selectedCharacterPlayer1) {
            case "Blossom":
                return GameStage.ICON_BLOSSOM;
            case "Bubbles":
                return GameStage.ICON_BUBBLES;
            case "Buttercup":
                return GameStage.ICON_BUTTERCUP;
            default:
                return null; //catch error
        }
    }    
    
    //set up selection interface for Player2 based on Player 1�s selection
    public void setupSecondPlayerSelection(Stage stage, Canvas canvas, Image selectedCharacterPlayer1) {
        //horizontal box for character selection buttons
        HBox hBoxButtons = new HBox();
        hBoxButtons.setAlignment(Pos.TOP_LEFT);
        hBoxButtons.setPadding(new Insets(225, 0, 0, 125));
        hBoxButtons.setSpacing(140);

        //buttons for each character
		Button selectBlossom = createTransparentButton(250, 400, GameStage.DEFAULT);
		Button selectBubbles = createTransparentButton(250, 400, GameStage.DEFAULT);
		Button selectButtercup = createTransparentButton(250, 400, GameStage.DEFAULT);

        //adjust button layout based on Player 1�s selection
        //different cases of buttons depending on Player 1's character selection (for catching the proper event handler for second player)
        if (selectedCharacterPlayer1 == GameStage.PLAYER_BLOSSOM) {
        	hBoxButtons.getChildren().addAll(selectBubbles, selectButtercup);    	
        } else if (selectedCharacterPlayer1 == GameStage.PLAYER_BUBBLES) {
        	hBoxButtons.getChildren().addAll(selectBlossom, selectButtercup);
        } else if (selectedCharacterPlayer1 == GameStage.PLAYER_BUTTERCUP) {
        	hBoxButtons.getChildren().addAll(selectBlossom, selectBubbles);
        }

       //create main pane for scene
        Pane mainPane = new Pane();
        mainPane.getChildren().add(canvas);
        mainPane.getChildren().add(hBoxButtons);


        //set up the scene with main pane
        Scene scene = new Scene(mainPane, GameStage.GAME_WIDTH, GameStage.GAME_HEIGHT);
        stage.setScene(scene);
        stage.show();

        GraphicsContext start = canvas.getGraphicsContext2D();
        start.drawImage(GameStage.BACKGROUNDP2, 0, 0);

       //display character icons based on Player 1�s selection
        if (selectedCharacterPlayer1 == GameStage.PLAYER_BLOSSOM) {
            start.drawImage(GameStage.ICON_BUBBLES, 100, 175);
            start.drawImage(GameStage.ICON_BUTTERCUP, 490, 175);
            this.selectedCharacterPlayer1 = GameStage.PLAYER_BLOSSOM;  	
        } else if (selectedCharacterPlayer1 == GameStage.PLAYER_BUBBLES) {
            start.drawImage(GameStage.ICON_BLOSSOM, 100, 175);
            start.drawImage(GameStage.ICON_BUTTERCUP, 490, 175);
            this.selectedCharacterPlayer1 = GameStage.PLAYER_BUBBLES;
        } else if (selectedCharacterPlayer1 == GameStage.PLAYER_BUTTERCUP) {
            start.drawImage(GameStage.ICON_BLOSSOM, 100, 175);
            start.drawImage(GameStage.ICON_BUBBLES, 490, 175);
            this.selectedCharacterPlayer1 = GameStage.PLAYER_BUTTERCUP;
        }

       //event handlers for character selection buttons for Player 2
        selectBlossom.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.out.println("Player 2 Selected Blossom");
                GameStage.startGame(stage, selectedCharacterPlayer1, GameStage.PLAYER_BLOSSOM); //starts the game after player 2' character selection
            }
        });

        selectBubbles.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.out.println("Player 2 Selected Bubbles");
                GameStage.startGame(stage, selectedCharacterPlayer1, GameStage.PLAYER_BUBBLES); //starts the game after player 2' character selection
            }
        });

        selectButtercup.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.out.println("Player 2 Selected Buttercup");
                GameStage.startGame(stage, selectedCharacterPlayer1, GameStage.PLAYER_BUTTERCUP); //starts the game after player 2' character selection
            }
        });
    }


}
