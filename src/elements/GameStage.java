package elements;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameStage {
	//fixed size for the game dimension
	public static final int GAME_WIDTH = 900;
	public static final int GAME_HEIGHT = 700;
	public static final int ICON_WIDTH = 300;
	public static final int ICON_HEIGHT = 500;
	public static final double CELL_WIDTH = GAME_WIDTH / 16.0;
	public static final double CELL_HEIGHT = GAME_HEIGHT / 11.0;
	public static final int TILE_HEIGHT = 59;
	public static final int TILE_WIDTH = 63;
	public static final int PLAYER_HEIGHT = 60;
	public static final int PLAYER_WIDTH = 65;
	public static final int BULLET_SPEED = 6;
	public static final int TIMER = 60; 
	
	//for character names
	public static final String BLOSSOM = "BLOSSOM";
	public static final String BUBBLES = "BUBBLES";
	public static final String BUTTERCUP = "BUTTERCUP";

	//for managing GUI
	private Stage stage;
	private Scene scene;
	private Group group;
	private Canvas canvas;
	private MediaPlayer mediaPlayer;

	//imports of images needed for the GUI
    public final static Image BACKGROUND = new Image("/images/bg.png", GAME_WIDTH, GAME_HEIGHT, false, false);
    public final static Image BACKGROUNDP1 = new Image("/images/player1_bg.png", GAME_WIDTH, GAME_HEIGHT, false, false);
    public final static Image BACKGROUNDP2 = new Image("/images/player2_bg.png", GAME_WIDTH, GAME_HEIGHT, false, false);
    public final static Image ABOUT = new Image("/images/about.png", GAME_WIDTH, GAME_HEIGHT, false, false);
    public final static Image DEVS = new Image("/images/devs.png", GAME_WIDTH, GAME_HEIGHT, false, false);
    //for character selection
    public final static Image ICON_BLOSSOM = new Image("/images/blossom.png", ICON_WIDTH, ICON_HEIGHT, false, false);
    public final static Image ICON_BUBBLES = new Image("/images/bubbles.png", ICON_WIDTH, ICON_HEIGHT, false, false);
    public final static Image ICON_BUTTERCUP = new Image("/images/buttercup.png", ICON_WIDTH, ICON_HEIGHT, false, false);
    //for winner stage
    public final static Image WIN_BLOSSOM = new Image("/images/winner_blossom.png", GAME_WIDTH, GAME_HEIGHT, false, false);
    public final static Image WIN_BUBBLES = new Image("/images/winner_bubbles.png", GAME_WIDTH, GAME_HEIGHT, false, false);
    public final static Image WIN_BUTTERCUP = new Image("/images/winner_buttercup.png", GAME_WIDTH, GAME_HEIGHT, false, false);
    public final static Image DRAW = new Image("/images/tie.png", GAME_WIDTH, GAME_HEIGHT, false, false);
    //for game elements
    public final static Image HEART = new Image("/images/heart.png", CELL_WIDTH, CELL_HEIGHT, false, false);
    public final static Image RAINBOW = new Image("/images/rainbow.png", CELL_WIDTH, CELL_HEIGHT, false, false);
    public final static Image CUPCAKE = new Image("/images/cupcake.png", CELL_WIDTH, CELL_HEIGHT, false, false);
    public final static Image FLOWER = new Image("/images/flower.png", CELL_WIDTH, CELL_HEIGHT, false, false);
    public final static Image MOJOJOJO = new Image("/images/mojo_jojo.png", CELL_WIDTH, CELL_HEIGHT, false, false);
    public final static Image BULLET = new Image("/images/bomb.png", TILE_HEIGHT, TILE_WIDTH, false, false);
    public final static Image EXPLODE = new Image("/images/explode.png", TILE_HEIGHT, TILE_WIDTH, false, false);
    public final static Image PLAYER_BLOSSOM = new Image("/images/player_blossom.png", ICON_WIDTH, ICON_HEIGHT, false, false);
    public final static Image PLAYER_BUBBLES = new Image("/images/player_bubbles.png", ICON_WIDTH, ICON_HEIGHT, false, false);
    public final static Image PLAYER_BUTTERCUP = new Image("/images/player_buttercup.png", ICON_WIDTH, ICON_HEIGHT, false, false);
    //for map elements
    public final static Image BRICK = new Image("/images/brick.png", TILE_HEIGHT, TILE_WIDTH, false, false);
    public final static Image WINDOW = new Image("/images/window.png", TILE_HEIGHT, TILE_WIDTH, false, false);
    public final static Image GRASS = new Image("/images/grass.jpg", TILE_HEIGHT, TILE_WIDTH, false, false);
    //for navigation purposes
    final static int GAME_START = 0;
    final static int ABOUT_PAGE = 1;
    final static int DEVELOPERS = 2;
    final static int SELECT_BLOSSOM = 3;
    final static int SELECT_BUBBLES = 4;
    final static int SELECT_BUTTERCUP = 5;
    final static int DEFAULT = 15;
    //background music
    Media BGM = new Media(getClass().getResource("/images/bgm.mp3").toExternalForm());

	//constructor
	public GameStage(){
		this.group = new Group(); //groups that holds the graphical element
		this.scene = new Scene(group, GAME_WIDTH, GAME_HEIGHT, Color.BLACK);
		this.canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT); //blank page for drawing/importing images
		this.mediaPlayer = new MediaPlayer(BGM);
		this.group.getChildren().add(this.canvas); //adds the canvas to the group
	}

	//setting up of the main window for the application
	public void setStage(Stage stage) {
		StackPane group = new StackPane(); //for stacking of child nodes
		Scene gameScene;

		this.stage = stage;
		stage.setTitle("PowerPuff Bomb Bonanza!"); //title of the application window
		group.getChildren().addAll(this.makeMenu(), this.makeButtons()); //added the bg and buttons to the group that will be displayed
		gameScene = new Scene(group);
		stage.setScene(gameScene); //sets the scene of the stage
		stage.show(); //displays the stage
		mediaPlayer.play();

		handleReturnKey(); //called once the esc button for returning back to menu
	}

	//for setting up the background image of the menu of the game
	private Canvas makeMenu() {
		Canvas c = new Canvas(GAME_WIDTH, GAME_HEIGHT);
		GraphicsContext bg = c.getGraphicsContext2D();
		bg.drawImage(BACKGROUND, 0, 0);
		return c;
	}

	Button createTransparentButton(int width, int height, int feature) {
		//use of css to make the buttons transparent (for visual purposes)
		//reference: https://stackoverflow.com/questions/36566197/javafx-button-with-transparent-background
		Button button = new Button();
		String transparentStyle = "-fx-background-color: transparent; -fx-border-color: transparent;";
		button.setStyle(transparentStyle);
		button.setPrefWidth(width);
		button.setPrefHeight(height);

		// for setting features
		// handles the needed action once the corresponding buttons are clicked
		switch (feature) {
		case GameStage.GAME_START:
	        button.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent e) {
	            	setGameStage(stage);
	            }
	        });
	        break;
		case GameStage.ABOUT_PAGE:
	        button.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent e) {
	            	setAboutDesc(stage);
	            }
	        });
	        break;
		case GameStage.DEVELOPERS:
	        button.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent e) {
	                setDevDesc(stage);
	            }
	        });
	        break;
		case GameStage.SELECT_BLOSSOM:
	        button.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent e) {
                	System.out.println("Player 1 Selected Blossom");
                	CharacterSelection select2 = new CharacterSelection(PLAYER_BLOSSOM, stage, canvas);
	            }
	        });
	        break;
		case GameStage.SELECT_BUBBLES:
	        button.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent e) {
                	System.out.println("Player 1 Selected Bubbles");
                	CharacterSelection select2 = new CharacterSelection(PLAYER_BUBBLES, stage, canvas);
	            }
	        });
	        break;
		case GameStage.SELECT_BUTTERCUP:
	        button.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent e) {
                	System.out.println("Player 1 Selected Buttercup");
                	CharacterSelection select2 = new CharacterSelection(PLAYER_BUTTERCUP, stage, canvas);
	            }
	        });
	        break;
		}
		return button;
	}

	//buttons in the game in vertical layout
	private VBox makeButtons() {

		//vbox buttons with desired spacing, and placements
		VBox vBoxButtons = new VBox();
		vBoxButtons.setAlignment(Pos.BOTTOM_CENTER);
		vBoxButtons.setPadding(new Insets(31));
		vBoxButtons.setSpacing(23);

		//create buttons for the start, about, and developers
		Button buttonStart = createTransparentButton(120, 31, GameStage.GAME_START);
		Button buttonAbout = createTransparentButton(120, 31, GameStage.ABOUT_PAGE);
		Button buttonDev = createTransparentButton(200, 31, GameStage.DEVELOPERS);

		vBoxButtons.getChildren().addAll(buttonStart, buttonAbout, buttonDev); //layouts the buttons in vertical line

		return vBoxButtons;
	}

	private HBox characterButtons() {
		HBox hBoxButtons = new HBox();
		hBoxButtons.setAlignment(Pos.TOP_CENTER);
		hBoxButtons.setPadding(new Insets(225, 0, 0, 35));
		hBoxButtons.setSpacing(40);

		Button selectBlossom = createTransparentButton(250, 400, GameStage.SELECT_BLOSSOM);
		Button selectBubbles = createTransparentButton(250, 400, GameStage.SELECT_BUBBLES);
		Button selectButtercup = createTransparentButton(250, 400, GameStage.SELECT_BUTTERCUP);

		hBoxButtons.getChildren().addAll(selectBubbles, selectBlossom, selectButtercup);
		return hBoxButtons;
	}

	//when the start button is clicked
	public void setGameStage(Stage stage){
		this.stage = stage;
		// Clear the group and add the canvas
		clearCanvas();
	    // add character buttons
	    group.getChildren().add(this.characterButtons());
		this.stage.setScene(this.scene);
		this.stage.show();

		// displays character selection screen
		GraphicsContext start = canvas.getGraphicsContext2D();
		start.drawImage(BACKGROUNDP1, 0, 0);
		start.drawImage(ICON_BLOSSOM, 300, 175);
		start.drawImage(ICON_BUBBLES, 10, 175);
		start.drawImage(ICON_BUTTERCUP, 590, 175);
	}

	//when About button is clicked
	public void setAboutDesc(Stage stage){
		// Clear the group and add the canvas
		clearCanvas();

		this.stage = stage;
		this.stage.setScene(this.scene);
		this.stage.show();

		//displays the image containing information about the game (objectives and instructions of how to play the game)
		GraphicsContext about = canvas.getGraphicsContext2D();
		about.drawImage(ABOUT, 0, 0);
	}

	//when Developers button is clicked
	public void setDevDesc(Stage stage){
		this.stage = stage;
		this.stage.setScene(this.scene);
		this.stage.show();
		// Clear the group and add the canvas
		clearCanvas();

		//displays the image containing information about the developers of the game
		GraphicsContext devs = canvas.getGraphicsContext2D();
		devs.drawImage(DEVS, 0, 0);
	}

	//method that handles the action needed when a button is clicked
	private void handleReturnKey() {
		this.scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
            	KeyCode returnKeyCode = e.getCode();
                returnMain(returnKeyCode); //returns to the Main when the button set for returning is triggered
			}
		});
	}

	//sets the specific keyboard code that allows the user to return to the menu
	private void returnMain(KeyCode key) {
		if(key==KeyCode.ESCAPE){
			setStage(stage);
		} //Escape button is set as the button to be used for returning to the menu
   	}

	//called to start the game after selecting characters for player 1 and 2
	public static void startGame(Stage stage, Image player1Image, Image player2Image) {
        GamePlay gamePlay = new GamePlay(player1Image, 74, 124, player2Image, 820, 560);
        gamePlay.setStage(stage);
    }

	void clearCanvas() {
	    group.getChildren().clear();
	    group.getChildren().add(this.canvas);
	}

}
