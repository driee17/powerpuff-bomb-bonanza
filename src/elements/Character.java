//package elements;
//
//import java.util.ArrayList;
//
//import javafx.scene.image.Image;
//
//public class Character {
//    private Image image;
//    //height and width of the character
//    private double charWidth;
//    private double charHeight;
//    //X and Y position of the character
//    private double charXPos;
//    private double charYPos;
//
//    //constructor
//    public Character(String imagePath, double charWidth, double charHeight, double charXPos, double charYPos) {
//        this.image = new Image(getClass().getResource(imagePath).toExternalForm());
//        this.charWidth = 60;
//        this.charHeight = 65;
//        this.charXPos = charXPos;
//        this.charYPos = charYPos;
//    }
//
//    //method that returns the bounded rectangle of the character
//    public RectType getBoundingRect() {
//        return new RectType(this.getCharXPos(), this.getCharYPos(), this.getCharWidth(), this.getCharHeight());
//    }
//
//    //getter method for character's image
//    public Image getImage() {
//        return image;
//    }
//
//    //getter method for character width
//    public double getCharWidth() {
//        return charWidth;
//    }
//
//    //getter method for character height
//    public double getCharHeight() {
//        return charHeight;
//    }
//
//    //getter method for X position of character
//    public double getCharXPos() {
//        return charXPos;
//    }
//
//    //getter method for Y position of character
//    public double getCharYPos() {
//        return charYPos;
//    }
//
//    //setter method for X position of character
//    public void setCharXPos(double charXPos) {
//        this.charXPos = charXPos;
//    }
//
//    //setter method for Y position of character
//    public void setCharYPos(double charYPos) {
//        this.charYPos = charYPos;
//    }
//
//    //method for controlling character movement based on the input
//    public void controls(ArrayList<String> input, String left, String right, String down, String up, int pace) {
//        if (input.contains(left)) { this.setCharXPos(this.getCharXPos() - pace); }
//        if (input.contains(right)) { this.setCharXPos(this.getCharXPos() + pace); }
//        if (input.contains(down)) { this.setCharYPos(this.getCharYPos() + pace); }
//        if (input.contains(up)) { this.setCharYPos(this.getCharYPos() - pace); }
//    }
//
//}
//
package elements;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Character extends Sprite {
	private String name;
	private String up;
	private String left;
	private String right;
	private String down;
	private int health;
	private int score;
	private Bullet bullet;
	private int playernum;

    public Character(Image imagePath, double x, double y, String left, String right, String down, String up, String attack, int playernum) {
    	super(imagePath, GameStage.PLAYER_HEIGHT, GameStage.PLAYER_WIDTH, x, y);
    	// for setting character names
        if (this.image == GameStage.PLAYER_BLOSSOM) {
        	this.name = GameStage.BLOSSOM;
        } else if (this.image == GameStage.PLAYER_BUBBLES) {
        	this.name = GameStage.BUBBLES;
        } else if (this.image == GameStage.PLAYER_BUTTERCUP) {
        	this.name = GameStage.BUTTERCUP;
        }
        this.left = left;
        this.right = right;
        this.down = down;
        this.up = up;
        this.health = 3;
        this.score = 0;
        this.bullet = new Bullet(GameStage.BULLET, this.x, this.y, 10);
        this.playernum = playernum;
    }

    public void controls(ArrayList<String> input, int pace) {
        if (input.contains(left)) this.x -= pace; 
        if (input.contains(right)) this.x += pace;
        if (input.contains(down)) this.y += pace;
        if (input.contains(up)) this.y -= pace; 
    }
    // Use the methods from Sprite class directly or override if necessary
   
    public String getName() {
    	return this.name;
    }
    public int getHealth() {
    	return this.health;
    }
    public int getScore() {
    	return this.score;
    }
    public int getPlayerNum() {
    	return this.playernum;
    }

    
    void setHealth(int hp) {
    	this.health += hp;
    }
    void setScore(int points) {
    	this.score += points;
    }
    void multScore(int points) {
    	this.score *= points;
    }
    void setName(String name) {
    	this.name = name;
    }  
    void setPlayerNum(int pnum) {
    	this.playernum = pnum;
    }    
    
    Bullet getBullet() {
    	return this.bullet;
    }
}
