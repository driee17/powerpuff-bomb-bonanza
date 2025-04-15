package elements;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class GameMap {

    private static final int MAP_SIZE_X = 16; //x-axis of the matrix
    private static final int MAP_SIZE_Y = 11; //y-axis

    private int[][] map = { //matrix for the map that we want to implement for our game
    		{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
    		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    		{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    		{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    		{1, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},

    };

    //creation of map
    public GridPane createMap() {
        GridPane gridPane = new GridPane();

        //creates a layout with tile that have dimensions of 59x63
        for (int y = 0; y < MAP_SIZE_Y; y++) {
            for (int x = 0; x < MAP_SIZE_X; x++) {
                Rectangle tile = new Rectangle(59, 63);
                tile.setStroke(Color.TRANSPARENT); //set the borders to transparent

                //uses the value of each tile to determine what image to use for the tile
                if (map[y][x] == 1) {
                    Image brickImage = new Image("/images/brick.png"); //fill the tiles for visual purposes
                    tile.setFill(new ImagePattern(brickImage));
                } else if (map[y][x] == 2) {
                    Image windowImage = new Image("/images/window.png");
                    tile.setFill(new ImagePattern(windowImage));
                } else if (map[y][x] == 0) {
                    Image windowImage = new Image("/images/grass.jpg");
                    tile.setFill(new ImagePattern(windowImage));
                } else {
                    tile.setFill(Color.BEIGE);
                }

                gridPane.add(tile, x, y);
            }
        }

        return gridPane;
    }

    //returns the map array
    //allow
    public int[][] getMap() {
        return map;
    }
}