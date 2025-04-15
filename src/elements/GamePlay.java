package elements;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GamePlay {
	// attributes
    private Scene scene;
    private Stage stage;
    private Group root;
    private Canvas canvas;
    private GraphicsContext gc;

    // for GameMap
    private GameMap gameMap;
    private GridPane gameGrid;
    private int[][] gameGridMatrix;

    // for characters
    private Character player1;
    private Character player2;
    private ArrayList<Character> characters = new ArrayList<>();
    
    // for GameTimer
    private GameTimer gameTimer;
    private AnimationTimer animator;
    private Font newFont = Font.loadFont(getClass().getResourceAsStream("/images/font.TTF"), 27); // imported font
    
    // for Spawning
    private Spawning spawning;

    // constructor
    public GamePlay(Image player1Image, int player1X, int player1Y, Image player2Image, int player2X, int player2Y) {

        this.root = new Group();
        this.scene = new Scene(root, 948, 693, Color.BEIGE);
        this.canvas = new Canvas(948, 693);
        this.gc = canvas.getGraphicsContext2D();
        this.player1 = new Character(player1Image, player1X, player1Y, "A", "D", "S", "W", "E", 1); // initialize Player 1 character
        this.player2 = new Character(player2Image, player2X, player2Y, "LEFT", "RIGHT", "DOWN", "UP", "SPACE", 2); // initialize Player 2 character
        this.characters.add(player1);
        this.characters.add(player2);

        this.gameTimer = new GameTimer(GameStage.TIMER); // set the game timer to 60 seconds
        this.gameTimer.setFont(newFont); // set timer font and style
        this.gameTimer.setStyle("-fx-text-fill: green;");

        this.gameMap = new GameMap();
        this.gameGrid = gameMap.createMap();
        this.gameGridMatrix = gameMap.getMap();
        root.getChildren().add(gameGrid);

        this.spawning = new Spawning(root, gameGrid, gameGridMatrix, player1, player2);
    }

	public void setStage(Stage stage) {
        this.stage = stage;
        addComponents();
        handleKeyPressEvent();
        this.root.getChildren().add(canvas);
        this.stage.setTitle("Powerpuff Bomb Bonanza!");
        this.stage.setScene(this.scene);
        this.stage.show();
        root.getChildren().add(gameTimer.getTimerLabel());
        gameTimer.start();
    }

    private void addComponents() {
        gc.clearRect(0, 0, 948, 693);
        gc.drawImage(player1.getImage(), player1.getX(), player1.getY(), player1.getWidth(), player1.getHeight());
        gc.drawImage(player2.getImage(), player2.getX(), player2.getY(), player2.getWidth(), player2.getHeight());
    }

    private boolean isCollision(double x, double y) {
        int gridX = (int) (x / GameStage.TILE_HEIGHT); // Adjusted to TILE_SIZE
        int gridY = (int) (y / GameStage.TILE_WIDTH); // Adjusted to TILE_SIZE

        // Ensure the coordinates are within the grid bounds
        if (gridX < 0 || gridX >= gameGridMatrix[0].length || gridY < 0 || gridY >= gameGridMatrix.length) {
            return true; // Out of bounds is considered a collision
        }

        return gameGridMatrix[gridY][gridX] != 0;
    }

    private void handleKeyPressEvent() {
        int pace = 4;
        ArrayList<String> inputPlayer1 = new ArrayList<>();
        ArrayList<String> inputPlayer2 = new ArrayList<>();

        this.scene.setOnKeyPressed(
            new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    if (code.equals("A") || code.equals("D") || code.equals("S") || code.equals("W") || code.equals("SPACE")) {
                        if (!inputPlayer1.contains(code))
                            inputPlayer1.add(code);
                    } else if (code.equals("LEFT") || code.equals("RIGHT") || code.equals("DOWN") || code.equals("UP") || code.equals("ENTER")) {
                        if (!inputPlayer2.contains(code))
                            inputPlayer2.add(code);
                    }
                }
            }
        );

        this.scene.setOnKeyReleased(
            new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    if (code.equals("A") || code.equals("D") || code.equals("S") || code.equals("W") || code.equals("SPACE")) {
                        inputPlayer1.remove(code);
                    } else if (code.equals("LEFT") || code.equals("RIGHT") || code.equals("DOWN") || code.equals("UP") || code.equals("ENTER")) {
                        inputPlayer2.remove(code);
                    }
                }
            }
        );

        handleInput(pace, inputPlayer1, inputPlayer2);
    }

    private void handleInput(int pace, ArrayList<String> inputPlayer1, ArrayList<String> inputPlayer2) {
        final long COOLDOWN = 500_000_000L; // 0.5 seconds in nanoseconds
        final long[] lastShotTime = {0, 0}; // lastShotTime[0] for player1, lastShotTime[1] for player2

        animator = new AnimationTimer() {
            public void handle(long currentNanoTime) {
                // Check if any player's health is zero
                if (player1.getHealth() <= 0 || player2.getHealth() <= 0) {
                    onGameOver();
                    stop();
                    return;
                }

                if (gameTimer.isTimeUp()) {
                    onTimeUp();
                    stop();
                    return;
                }

                for (Character character : characters) {
                    boolean collisionDetected = false;

                    ArrayList<String> input = character == player1 ? inputPlayer1 : inputPlayer2;
                    character.controls(input, pace);

                    // Shooting logic with cooldown for player1
                    if (character == player1) {
                        if (input.contains("SPACE") && currentNanoTime - lastShotTime[0] >= COOLDOWN) {
                            lastShotTime[0] = currentNanoTime; // update last shot time
                            Bullet bullet = new Bullet(GameStage.BULLET, character.getX(), character.getY(), GameStage.BULLET_SPEED);
                            if (input.contains("W")) {  // shoot up
                                bullet.move(gc, "W", player2, spawning);
                            } else if (input.contains("S")) { // shoot down
                                bullet.move(gc, "S", player2, spawning);
                            } else if (input.contains("A")) { // shoot left
                                bullet.move(gc, "A", player2, spawning);
                            } else if (input.contains("D")) { // shoot right
                                bullet.move(gc, "D", player2, spawning);
                            } else {
                                bullet.move(gc, "D", player2, spawning);
                            }
                            
                        }
                    }

                    // Shooting logic with cooldown for player2
                    if (character == player2) {
                        if (input.contains("ENTER") && currentNanoTime - lastShotTime[1] >= COOLDOWN) {
                            lastShotTime[1] = currentNanoTime; // update last shot time
                            Bullet bullet = new Bullet(GameStage.BULLET, character.getX(), character.getY(), GameStage.BULLET_SPEED);
                            if (input.contains("UP")) {  // shoot up
                                bullet.move(gc, "UP", player1, spawning);
                            } else if (input.contains("DOWN")) { // shoot down
                                bullet.move(gc, "DOWN", player1, spawning);
                            } else if (input.contains("LEFT")) { // shoot left
                                bullet.move(gc, "LEFT", player1, spawning);
                            } else if (input.contains("RIGHT")) { // shoot right
                                bullet.move(gc, "RIGHT", player1, spawning);
                            } else {
                                bullet.move(gc, "LEFT", player1, spawning);
                            }
                        }
                    }

                    // Check for collisions
                    if (isCollision(character.getX(), character.getY()) ||
                        isCollision(character.getX() + character.getWidth(), character.getY()) ||
                        isCollision(character.getX(), character.getY() + character.getHeight()) ||
                        isCollision(character.getX() + character.getWidth(), character.getY() + character.getHeight())) {
                        collisionDetected = true;
                    }

                    if (!collisionDetected) {
                        double characterX = character.getX();
                        double characterY = character.getY();
                        int playerNumber = character == player1 ? 1 : 2;
                        spawning.collectPowerUp(characterX, characterY, playerNumber);
                    } else {
                        character.controls(input, -pace);
                    }
                }

                addComponents();
            }
        };
        animator.start();
    }


    public boolean intersects(RectType rect1, RectType rect2) {
        return rect1.getX() < rect2.getX() + rect2.getWidth() &&
               rect1.getX() + rect1.getWidth() > rect2.getX() &&
               rect1.getY() < rect2.getY() + rect2.getHeight() &&
               rect1.getY() + rect1.getHeight() > rect2.getY();
    }


    public void onTimeUp() {
        if (animator != null) {
            animator.stop(); // stop the animator
        }

        Label gameOverLabel = new Label("Game Over!");
        gameOverLabel.setLayoutX(350);
        gameOverLabel.setLayoutY(350);
        gameOverLabel.setFont(newFont); // set the font to the imported font the same as timer label
        gameOverLabel.setStyle("-fx-text-fill: red;");
        root.getChildren().add(gameOverLabel);

        // Create a PauseTransition with a specified delay (e.g., 5 seconds)
        PauseTransition delay = new PauseTransition();
        delay.setOnFinished(event -> {
            Character winningCharacter = determineWinningCharacter();
            WinningStage winningStage = new WinningStage(winningCharacter, stage);
            Scene winningScene = winningStage.getScene();

            // Set the winning scene in the stage
            stage.setScene(winningScene);
        });

        // Start the pause transition
        delay.play();
    }
    
    private void onGameOver() {
        if (animator != null) {
            animator.stop(); // Stop the animator
        }

        Label gameOverLabel = new Label("Game Over!");
        gameOverLabel.setLayoutX(350);
        gameOverLabel.setLayoutY(350);
        gameOverLabel.setFont(newFont); // Set the font to the imported font the same as timer label
        gameOverLabel.setStyle("-fx-text-fill: red;");
        root.getChildren().add(gameOverLabel);

        // Create a PauseTransition with a specified delay (e.g., 5 seconds)
        PauseTransition delay = new PauseTransition();
        delay.setOnFinished(event -> {
            Character winningCharacter = determineWinningCharacter();
            WinningStage winningStage = new WinningStage(winningCharacter, stage);
            Scene winningScene = winningStage.getScene();

            // Set the winning scene in the stage
            stage.setScene(winningScene);
        });

        // Start the pause transition
        delay.play();
    }




    private Character determineWinningCharacter() {
        int player1Score = player1.getScore();
        int player2Score = player2.getScore();
        int player1Health = player1.getHealth();
        int player2Health = player2.getHealth();

        // Check if Player 1's health is zero
        if (player1Health <= 0) {
            return player2; // Player 2 wins if Player 1's health is zero
        }
        // Check if Player 2's health is zero
        else if (player2Health <= 0) {
            return player1; // Player 1 wins if Player 2's health is zero
        }
        // Check scores if both players have health remaining
        else {
            // Determine the player with the highest score
            if (player1Score > player2Score) {
                return player1; // Player 1 has the highest score
            } else if (player2Score > player1Score) {
                return player2; // Player 2 has the highest score
            } else {
            	Character draw = new Character(null, player2Health, player2Health, null, null, null, null, null, player2Health);
                draw.setName("DRAW");
            	return draw;  // It's a tie
            }
        }
    }

}
