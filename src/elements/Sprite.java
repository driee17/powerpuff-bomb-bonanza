package elements;

import java.util.ArrayList;

import javafx.scene.image.Image;

public abstract class Sprite {
    protected Image image;
    private double width;
    private double height;
    protected double x;
    protected double y;

    public Sprite(Image imagePath, double width, double height, double x, double y) {
        this.image = imagePath;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public RectType getBoundingRect() {
        return new RectType(this.x, this.y, this.width, this.height, image);
    }

    public void controls(ArrayList<String> input, String left, String right, String down, String up, int pace) {
        if (input.contains(left)) { this.x -= pace; }
        if (input.contains(right)) { this.x += pace; }
        if (input.contains(down)) { this.y += pace; }
        if (input.contains(up)) { this.y -= pace; }
    }
}
