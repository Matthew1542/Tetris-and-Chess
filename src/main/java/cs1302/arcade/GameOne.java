package cs1302.arcade;

import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * This class is a javaFX made game of tetris
 *
 */
public class GameOne extends Group {

    /** size of each block */
    public final int size = 36;

    /** size of the width of the game board */
    public final int xSize = size * 10 + 920;

    /** height of the game board */
    public final int ySize = xSize * 2;

    /** double array containing every possible spot for each block */
    public final int[][] grid = new int[xSize / size][ySize / size];

    /** Images to be used to the sides of the main game */
    ImageView board = new ImageView(new Image("file:resources/board.png"));


    public GameOne() {
        this.getChildren().add(board);
	Block temp = makeBlock();
	
	this.getChildren().addAll(temp.r1, temp.r2, temp.r3, temp.r4);

    }
    
    /**
     * Makes the block to be used
     *
     * @return
     */
    public Block makeBlock() {
        int color = (int) (Math.random() * 6);
        String type = "temp";
        Rectangle r1 = new Rectangle(size, size);
        Rectangle r2 = new Rectangle(size, size);
        Rectangle r3 = new Rectangle(size, size);
        Rectangle r4 = new Rectangle(size, size);

        if (color == 0) { //the color cyan block will be made
            r1.setX(xSize / 2 - size * 2);
            r2.setX(xSize / 2 - size);
            r3.setX(xSize / 2);
            r4.setX(xSize / 2 + size);
            type = "cyan";
        } else if (color == 1) {
            r1.setX(xSize / 2 - size);
            r1.setY(0 - size);
            r2.setX(xSize / 2 - size);
            r3.setX(xSize / 2);
            r4.setX(xSize / 2 + size);
            type = "blue";
        } else if (color == 2) {
            r1.setX(xSize / 2 - size);
            r2.setX(xSize / 2);
            r3.setX(xSize / 2 + size);
            r4.setX(xSize / 2 + size);
            r4.setY(0 - size);
            type = "orange";
        } else if (color == 3) {
            r1.setX(xSize / 2 - size);
            r1.setY(0 - size);
            r2.setX(xSize / 2);
            r2.setY(0 - size);
            r3.setX(xSize / 2 - size);
            r4.setX(xSize / 2);
            type = "yellow";
        }  else if(color == 4) {
            r1.setX(xSize / 2 - size);
            r2.setX(xSize / 2);
            r3.setX(xSize / 2);
            r3.setY(0 - size);
            r4.setX(xSize / 2 + size);
            r4.setY(0 - size);
            type = "green";
        } else if (color == 5) {
            r1.setX(xSize / 2 - size);
            r2.setX(xSize / 2);
            r2.setY(0 - size);
            r3.setX(xSize / 2);
            r4.setX(xSize / 2 + size);
            type = "purple";
        } else if (color == 6) {
            r1.setX(xSize / 2 - size);
            r1.setY(0 - size);
            r2.setX(xSize / 2);
            r2.setY(0 - size);
            r3.setX(xSize / 2);
            r4.setX(xSize / 2 + size);
            type = "red";
        } else {
            System.out.println("Error");
        } //else
	

        return new Block(r1, r2, r3, r4, type);

    } //makeBlock

}
