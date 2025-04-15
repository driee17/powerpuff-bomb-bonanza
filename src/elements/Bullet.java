package elements;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
public class Bullet extends Sprite{
    private double x;
    private double y;
    private double speed;
    private AnimationTimer animationTimer;

    public Bullet(Image imagePath, double x, double y, double speed) {
    	super(imagePath, GameStage.TILE_HEIGHT, GameStage.TILE_WIDTH, x, y);
    	this.x = x;
    	this.y = y;
        this.speed = speed;
    }

    public void move(GraphicsContext gc, String direction, Character character, Spawning spawning) {
    	// create new animation timer for bullets
    	this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
		       
		        switch (direction) {
		            case "W":
		            case "UP":
		                y -= speed;
		                break;
		            case "S":
		            case "DOWN":
		                y += speed;
		                break;
		            case "A":
		            case "LEFT":
		                x -= speed;
		                break;
		            case "D":
		            case "RIGHT":
		                x += speed;
		                break;
		        }
		
		        gc.drawImage(image, x, y);
		        if (intersects(character)) { // for attacking
		        	animationTimer.stop();
		        	gc.drawImage(GameStage.EXPLODE, x, y);
		        	spawning.reduceHealth(character);
		        	System.out.println("Player " + (character.getPlayerNum() == 1 ? "2" : "1") + " hits Player " + character.getPlayerNum());
		        }
		        if (intersectsWall()) { // for attacking
		        	animationTimer.stop();
		        	gc.drawImage(GameStage.EXPLODE, x, y);
		        }

            }
        };
        
        // Start the animation
        animationTimer.start();
    }

    public boolean intersects(Character character) {
    	return x < character.getX() + character.getWidth() &&
               x + image.getWidth() > character.getX() &&
               y < character.getY() + character.getHeight() &&
               y + image.getHeight() > character.getY();
    }
    
    public boolean intersectsWall() {
        if (this.x < 0 || this.y >= 948 || this.x < 0 || this.y >= 693) {
            return true; // Out of bounds is considered a collision
        }
        return false;
    }
    

    // Getters for the position of the bullet
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

