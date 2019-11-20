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
    public final int xSize = size * 10;

    /** height of the game board */
    public final int ySize = xSize * 2;

    /** double array containing every possible spot for each block */
    public final int[][] grid = new int[xSize / size][ySize / size];
    

    public GameOne() {
	

    }
    
}
