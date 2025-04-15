package elements;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Spawning {
    private Group group;
    private Canvas canvas;
    private GraphicsContext gc;
    private Random random;
    private List<RectType> powerUps;
    private List<RectType> rainbows;
    private static final int INITIAL_POWER_UPS = 3;
    private static final int INITIAL_RAINBOWS = 10;
    private static final int MAX_HEALTH = 3;
    private Character player1;
    private Character player2;
    private Image heartImage;
    private Font newFont = Font.loadFont(getClass().getResourceAsStream("/images/font.TTF"), 15); // imported font

    double cellWidth = GameStage.TILE_WIDTH; // Width of a grid cell
    double cellHeight = GameStage.TILE_HEIGHT;

    private GridPane gameGrid;
    private int[][] gameGridMatrix;

    public Spawning(Group group, GridPane gameGrid, int[][] gameGridMatrix, Character p1, Character p2) {
        this.group = group;
        this.gameGrid = gameGrid;
        this.gameGridMatrix = gameGridMatrix;
        this.canvas = new Canvas(GameStage.GAME_WIDTH, GameStage.GAME_HEIGHT);
        this.gc = canvas.getGraphicsContext2D();
        this.random = new Random();
        this.powerUps = new ArrayList<>();
        this.rainbows = new ArrayList<>();
        this.player1 = p1;
        this.player2 = p2;
        this.heartImage = GameStage.HEART;
        group.getChildren().add(canvas);
        spawnInitialPowerUps();
        spawnInitialRainbows();
        renderScores();
        renderHealth();
    }

    private void spawnPowerUp() {
        boolean validPosition = false;
        double x = 0, y = 0;
        Image powerUpImage = random.nextDouble() < 0.5 ? 
            (random.nextDouble() < 0.4 ? GameStage.CUPCAKE : GameStage.FLOWER) :
            GameStage.MOJOJOJO;

        while (!validPosition) {
            int randomRow = random.nextInt(gameGridMatrix.length); // Random row index
            int randomCol = random.nextInt(gameGridMatrix[0].length); // Random column index

            if (gameGridMatrix[randomRow][randomCol] == 0) { // Check if the cell is empty
                // Calculate the top-left coordinates of the random cell
                x = randomCol * cellWidth;
                y = randomRow * cellHeight;

                RectType spawnRect = new RectType(x, y, cellWidth, cellHeight, powerUpImage); // Adjusted size to match grid cell

                if (!intersectsBrickWall(spawnRect) && !intersectsAnyPowerUp(spawnRect) && !intersectsAnyRainbow(spawnRect)) {
                    validPosition = true;
                    powerUps.add(spawnRect);
                }
            }
        }

        gc.setFill(new ImagePattern(powerUpImage));
        gc.fillRect(x, y, cellWidth, cellHeight); // Set the power-up at the top-left of the selected position
    }

    private void spawnRainbow() {
        boolean validPosition = false;
        double x = 0, y = 0;
        Image rainbowImage = GameStage.RAINBOW;

        while (!validPosition) {
            int randomRow = random.nextInt(gameGridMatrix.length); // Random row index
            int randomCol = random.nextInt(gameGridMatrix[0].length); // Random column index

            if (gameGridMatrix[randomRow][randomCol] == 0) { // Check if the cell is empty
                // Calculate the top-left coordinates of the random cell
                x = randomCol * cellWidth;
                y = randomRow * cellHeight;

                RectType spawnRect = new RectType(x, y, cellWidth, cellHeight, rainbowImage); // Adjusted size to match grid cell

                if (!intersectsBrickWall(spawnRect) && !intersectsAnyPowerUp(spawnRect) && !intersectsAnyRainbow(spawnRect)) {
                    validPosition = true;
                    rainbows.add(spawnRect);
                }
            }
        }

        gc.setFill(new ImagePattern(rainbowImage));
        gc.fillRect(x, y, cellWidth, cellHeight); // Set the rainbow at the top-left of the cell
    }

    private void spawnInitialPowerUps() {
        for (int i = 0; i < INITIAL_POWER_UPS; i++) {
            spawnPowerUp();
        }
    }

    private void spawnInitialRainbows() {
        for (int i = 0; i < INITIAL_RAINBOWS; i++) {
            spawnRainbow();
        }
    }

    private boolean intersectsBrickWall(RectType spawnRect) {
        for (int row = 0; row < gameGridMatrix.length; row++) {
            for (int col = 0; col < gameGridMatrix[0].length; col++) {
                if (gameGridMatrix[row][col] == 1 || gameGridMatrix[row][col] == 2 || gameGridMatrix[row][col] == 3) { // Check for brick, window, and boundary walls
                    double rectY = row * cellHeight;
                    double rectX = col * cellWidth;
                    RectType brickWall = new RectType(rectX, rectY, cellWidth, cellHeight, null);
                    if (intersects(spawnRect, brickWall)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean intersects(RectType a, RectType b) {
        return a.getX() < b.getX() + b.getWidth() &&
               a.getX() + a.getWidth() > b.getX() &&
               a.getY() < b.getY() + b.getHeight() &&
               a.getY() + a.getHeight() > b.getY();
    }

    private boolean intersectsAnyPowerUp(RectType rect) {
        for (RectType powerUp : powerUps) {
            if (intersects(rect, powerUp)) {
                return true;
            }
        }
        return false;
    }

    private boolean intersectsAnyRainbow(RectType rect) {
        for (RectType rainbow : rainbows) {
            if (intersects(rect, rainbow)) {
                return true;
            }
        }
        return false;
    }

    public void collectPowerUp(double x, double y, int playerNumber) {
        RectType collectedPowerUp = null;
        boolean isRainbow = false;
        boolean isCupcake = false;
        boolean isMojoJojo = false;

        // Check if the collected power-up is a rainbow
        for (RectType rainbow : rainbows) {
            if (rainbow.contains(x, y)) {
                collectedPowerUp = rainbow;
                isRainbow = true;
                break;
            }
        }

        // If it's not a rainbow, check if it's a regular power-up
        if (!isRainbow) {
            for (RectType powerUp : powerUps) {
                if (powerUp.contains(x, y)) {
                    collectedPowerUp = powerUp;
                    // Check the type of the power-up
                    if (powerUp.getImage() == GameStage.CUPCAKE) {
                        isCupcake = true;
                    } else if (powerUp.getImage() == GameStage.MOJOJOJO) {
                        isMojoJojo = true;
                    }
                    break;
                }
            }
        }

        if (collectedPowerUp != null) {
            // Remove the collected power-up from the respective list
            if (isRainbow) {
                rainbows.remove(collectedPowerUp);
                // Increase the score by 5 if it's a rainbow
                if (playerNumber == 1) {
                    player1.setScore(5);
                    System.out.println("Player 1 score after rainbow: " + player1.getScore());
                } else if (playerNumber == 2) {
                	player2.setScore(5);
                    System.out.println("Player 2 score after rainbow: " + player2.getScore());
                }
                // Spawn a new rainbow
                spawnRainbow();
            } else {
                powerUps.remove(collectedPowerUp);
                if (isCupcake) {
                    // Increase health by 1 if it's a cupcake
                    if (playerNumber == 1 && player1.getHealth() < MAX_HEALTH) {
                    	player1.setHealth(1);
                        System.out.println("Player 1 health after cupcake: " + player1.getHealth());
                    } else if (playerNumber == 2 && player2.getHealth() < MAX_HEALTH) {
                    	player2.setHealth(1);
                        System.out.println("Player 2 health after cupcake: " + player2.getHealth());
                    }
                } else if (isMojoJojo) {
                    // Decrease health by 1 if it's Mojo Jojo
                    if (playerNumber == 1 && player1.getHealth() > 0) {
                    	player1.setHealth(-1);
                        System.out.println("Player 1 health after Mojo Jojo: " + player1.getHealth());
                    } else if (playerNumber == 2 && player2.getHealth() > 0) {
                    	player2.setHealth(-1);
                        System.out.println("Player 2 health after Mojo Jojo: " + player2.getHealth());
                    }
                } else {
                    // Multiply points by 2 if it's a flower
                    if (playerNumber == 1) {
                    	player1.multScore(2);
                        System.out.println("Player 1 score after flower: " + player1.getScore());
                    } else if (playerNumber == 2) {
                    	player2.multScore(2);
                        System.out.println("Player 2 score after flower: " + player2.getScore());
                    }
                }
                // Spawn a new power-up
                spawnPowerUp();
            }

            // Clear the collected power-up from the canvas
            gc.clearRect(collectedPowerUp.getX(), collectedPowerUp.getY(), collectedPowerUp.getWidth(), collectedPowerUp.getHeight());

            // Update the scores and health
            renderScores();
            renderHealth();
        }
    }
    
    void reduceHealth(Character player) {
    	player.setHealth(-1);
        renderHealth(); // Ensure health display is updated

    }

    private void renderHealth() {
        gc.clearRect(240, 0, 200, 60); // Clear the previous health display
        gc.setFill(Color.DARKSALMON);
        gc.setFont(newFont);

        // Draw hearts for Player 1
        gc.fillText("Health: ", 250, 30);
        for (int i = 0; i < player1.getHealth(); i++) {
            gc.drawImage(heartImage, 335 + i * 20, 15, 18, 18);
        }

        // Draw hearts for Player 2
        gc.fillText("Health: ", 250, 45);
        for (int i = 0; i < player2.getHealth(); i++) {
            gc.drawImage(heartImage, 335 + i * 20, 30, 18, 18);
        }
    }

    private void renderScores() {
        gc.clearRect(0, 0, 400, 50); // Clear the previous scores
        gc.setFill(Color.DARKSALMON);
        gc.setFont(newFont);
        gc.fillText("Player 1 Score: " + player1.getScore(), 10, 30);
        gc.fillText("Player 2 Score: " + player2.getScore(), 10, 45);
    }
}
