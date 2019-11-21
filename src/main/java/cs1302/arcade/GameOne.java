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
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

/**
 * This class is a javaFX made game of tetris.
 *
 */
public class GameOne {

    /** size of each block. */
    public final int size = 36;

    /** size of the width of the game board. */
    public final int xSize = size * 10 + 920;

    /** height of the game board. */
    public final int ySize = xSize * 2;

    /** double array containing every possible spot for each block. */
    public final int[][] grid = new int[10][20];

    /** Images to be used to the sides of the main game. */
    ImageView board = new ImageView(new Image("file:resources/board.png"));

    int score; //the score of the game
    Block mainBlock;
    Block nextBlock = makeBlock();
    Timeline timeline = new Timeline(); //create new timeline
    Group group = new Group();
    Scene scene;
    ArcadeApp app;

    /**
     * The constructor of the tetris game.
     *
     */
    public GameOne(ArcadeApp app) {
        this.app = app;
        scene = new Scene(group, 1280, 720);
        for (int i = 0; i < 10; i++) {
            for (int x = 0; x < 20; x++) {
                grid[i][x] = 0; //fill grid with 0's
            } //for
        } //for
        Text score = new Text("Score: ");
        score.setX(935);
        score.setY(100);
        Button quit = new Button("Quit");
        quit.setTranslateX(50);
        quit.setTranslateY(50);
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                app.gameState = "MENU";
                app.updateScene();
            }
        });
        group.getChildren().addAll(board, quit);
        Block temp = nextBlock;

        group.getChildren().addAll(temp.r1, temp.r2, temp.r3, temp.r4, score);
        moveOnKeyPressed(temp);
        mainBlock = temp;
        nextBlock = makeBlock();

        EventHandler<ActionEvent> handler = event -> runner();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(400), handler);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        group.requestFocus();
    } //main/constructor

    /**
     * Getter for the scene. 
     *
     * @return the scene 
     */
    public Scene getScene() {
        return scene;
    }


    /**
     * The method that results in a moving block. 
     *
     * @param block the block to be moved
     */
    private void moveOnKeyPressed (Block block) {
        group.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent event) {
                    switch (event.getCode()) {
                    case LEFT:  // KeyCode.LEFT
                        moveLeft(block);
                        break;
                    case RIGHT: // KeyCode.RIGHT
                        moveRight(block);
                        break;
                    case DOWN:
                        moveDown(block);
                    }
                }
            });
    }

    int timer = 0;
    boolean playing = true;
    public void runner() {
        if (mainBlock.r1.getY() == 0 || mainBlock.r2.getY() == 0 ||
            mainBlock.r3.getY() == 0 || mainBlock.r4.getY() == 0) {
            timer++;
        } else {
            timer = 0;
        }
        if (timer == 3) {      
            Text gameOver = new Text("GAME OVER");
            gameOver.setX(600);
            gameOver.setY(360);
            gameOver.setFill(Color.RED);
            group.getChildren().add(gameOver);
            playing = false;
        }
        
        if (timer == 20) {
            app.gameState = "MENU";
            app.updateScene();
        }
        
        if (playing) {
            moveDown(mainBlock);
            
        }
    }
    
    /**
     * The method that causes the block to fall down by its size,
     * every 10 seconds.
     *
     * @param block the block to fall down
     */
    public void moveDown(Block block) {
        try {
            if (block.r1.getY() == 684 || block.r2.getY() == 684 ||
                block.r3.getY() == 684 || block.r4.getY() == 684 ||
                grid[(int)(block.r1.getX() - 460) / 36][((int)block.r1.getY() / 36) + 1] == 1 ||
                grid[(int)(block.r2.getX() - 460) / 36][((int)block.r2.getY() / 36) + 1] == 1 ||
                grid[(int)(block.r3.getX() - 460) / 36][((int)block.r3.getY() / 36) + 1] == 1 ||
                grid[(int)(block.r4.getX() - 460) / 36][((int)block.r4.getY() / 36) + 1] == 1) {
                //if you are here, the block has reached the bottom. Now, the grid must change
    
                //Fill the spot in the double array, block hit a solid
                grid[(int)(block.r1.getX() - 460) / 36][(int)block.r1.getY() / 36] = 1;
                grid[(int)(block.r2.getX() - 460) / 36][(int)block.r2.getY() / 36] = 1;
                grid[(int)(block.r3.getX() - 460) / 36][(int)block.r3.getY() / 36] = 1;
                grid[(int)(block.r4.getX() - 460) / 36][(int)block.r4.getY() / 36] = 1;

                Block temp = nextBlock;
                nextBlock = makeBlock();
                mainBlock = temp;

                group.getChildren().addAll(temp.r1, temp.r2, temp.r3, temp.r4);
                moveOnKeyPressed(temp);
            } //if

            if (block.r1.getY() + 36 < 720 && block.r2.getY() + 36 < 720 &&
                block.r3.getY() + 36 < 720 && block.r4.getY() + 36 < 720 &&
                grid[(int)(block.r1.getX() - 460) / 36][((int)block.r1.getY() / 36) + 1] == 0 &&
                grid[(int)(block.r2.getX() - 460) / 36][((int)block.r2.getY() / 36) + 1] == 0 &&
                grid[(int)(block.r3.getX() - 460) / 36][((int)block.r3.getY() / 36) + 1] == 0 &&
                grid[(int)(block.r4.getX() - 460) / 36][((int)block.r4.getY() / 36) + 1] == 0)  {
                //checking if the spot below is free for taking
                block.r1.setY(block.r1.getY() + 36);
                block.r2.setY(block.r2.getY() + 36);
                block.r3.setY(block.r3.getY() + 36);
                block.r4.setY(block.r4.getY() + 36);
            } //if
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        } //catch
    }  //movDown

    /**
     * Moves the block one spot to the left.
     *
     * @param block the block to be moved
     */
    public void moveLeft(Block block) {
        try {
            if (block.r1.getX() - 460 - size >= 0 && block.r2.getX() - 460 - size >= 0 &&
                block.r3.getX() - 460 - size >= 0 && block.r4.getX() - 460 - size >= 0 &&
                grid[((int)(block.r1.getX() - 460) / 36) - 1][(int)block.r1.getY() / 36] == 0 &&
                grid[((int)(block.r2.getX() - 460) / 36) - 1][(int)block.r2.getY() / 36] == 0 &&
                grid[((int)(block.r3.getX() - 460) / 36) - 1][(int)block.r3.getY() / 36] == 0 &&
                grid[((int)(block.r4.getX() - 460) / 36) - 1][(int)block.r4.getY() / 36] == 0)  {
                //checking if the spot below is free for taking
                block.r1.setX(block.r1.getX() - 36);
                block.r2.setX(block.r2.getX() - 36);
                block.r3.setX(block.r3.getX() - 36);
                block.r4.setX(block.r4.getX() - 36);
            } //if
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
    } //move left

    /**
     * Moves block to the right.
     *
     * @param block the moving item
     */ 
    public void moveRight(Block block) {
        try {
            if (block.r1.getX() - 460 + size < 360 && block.r2.getX() - 460 + size < 360 &&
                block.r3.getX() - 460 + size < 360 && block.r4.getX() - 460 + size < 360 &&
                grid[((int)(block.r1.getX() - 460) / 36) + 1][(int)block.r1.getY() / 36] == 0 &&
                grid[((int)(block.r2.getX() - 460) / 36) + 1][(int)block.r2.getY() / 36] == 0 &&
                grid[((int)(block.r3.getX() - 460) / 36) + 1][(int)block.r3.getY() / 36] == 0 &&
                grid[((int)(block.r4.getX() - 460) / 36) + 1][(int)block.r4.getY() / 36] == 0)  {
                //checking if the spot below is free for taking
                block.r1.setX(block.r1.getX() + 36);
                block.r2.setX(block.r2.getX() + 36);
                block.r3.setX(block.r3.getX() + 36);
                block.r4.setX(block.r4.getX() + 36);
            } //if
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        } //catch
    }

    /**
     * Makes the block to be used.
     *
     * @return the block to be created
     */
    public Block makeBlock() {
        int color = (int) (Math.random() * 7);
        String type = "temp";
        Rectangle r1 = new Rectangle(size, size), r2 = new Rectangle(size, size);       
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
        }  else if (color == 4) {
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
        } //else if
        return new Block(r1, r2, r3, r4, type);
    } //makeBlock

}
