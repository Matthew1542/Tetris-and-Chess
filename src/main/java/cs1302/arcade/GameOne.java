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

    int score = 0; //the score of the game
    Block mainBlock;
    Block nextBlock = makeBlock();
    Timeline timeline = new Timeline(); //create new timeline
    Group group = new Group();
    Scene scene;
    Text scoreText;
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
        scoreText = new Text("Score: " + score);
        scoreText.setX(935);
        scoreText.setY(100);
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

        group.getChildren().addAll(temp.r1, temp.r2, temp.r3, temp.r4, scoreText);
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
                        score++;
			break;
		    case UP:
			rotateBlock(block);
			break;
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
            scoreText.setText("Score: " + score);
        }
    }


    public void rotateBlock(Block block) {
        if (block.type.equals("red")) {
            rotateRed(block);
        } else if (block.type.equals("blue")) {
            rotateBlue(block);
        } else if (block.type.equals("green")) {
            rotateGreen(block);
        } else if (block.type.equals("cyan")) {
            rotateCyan(block);
        } else if (block.type.equals("purple")) {
            rotatePurple(block);
        } else if (block.type.equals("orange")) {
            rotateOrange(block);
        } 
    } //rotate
    
    public void rotateRed(Block block) {
        if (block.rotation == 1) {
            if (checkRotate(block.r1, 1, 0) && checkRotate(block.r2, 0, 1) &&
                checkRotate(block.r3, -1, 0) && checkRotate(block.r4, -2, 1)) {
                block.rotation++;
                block.r1.setX(block.r1.getX() + 36);
                block.r2.setY(block.r2.getY() + 36);
                block.r3.setX(block.r3.getX() - 36);
                block.r4.setX(block.r4.getX() - 72);
                block.r4.setY(block.r4.getY() + 36);
            }
        } else if (block.rotation == 2) {
            if (checkRotate(block.r1, -1, 0) && checkRotate(block.r2, 0, -1) &&
                checkRotate(block.r3, 1, 0) && checkRotate(block.r4, 2, -1)) {
                block.rotation = 1;
                block.r1.setX(block.r1.getX() - 36);
                block.r2.setY(block.r2.getY() - 36);
                block.r3.setX(block.r3.getX() + 36);
                block.r4.setX(block.r4.getX() + 72);
                block.r4.setY(block.r4.getY() - 36);
            }
        }
    }
    
    public void rotateBlue(Block block) {
        if (block.rotation == 1) {
            if (checkRotate(block.r1, 2, 0) && checkRotate(block.r2, 1, -1) &&
                checkRotate(block.r3, 0, 0) && checkRotate(block.r4, -1, 1)) {
                block.rotation++;
                block.r1.setX(block.r1.getX() + 72);
                block.r2.setX(block.r2.getX() + 36);
                block.r2.setY(block.r2.getY() - 36);
                block.r4.setX(block.r4.getX() - 36);
                block.r4.setY(block.r4.getY() + 36);
            }          
        } else if (block.rotation == 2) {
            if (checkRotate(block.r1, 0, 2) && checkRotate(block.r2, -1, 1) &&
                checkRotate(block.r3, 0, 0) && checkRotate(block.r4, 1, -1)) {
                block.rotation++;
                block.r1.setY(block.r1.getY() + 72);
                block.r2.setX(block.r2.getX() - 36);
                block.r2.setY(block.r2.getY() + 36);
                block.r4.setX(block.r4.getX() + 36);
                block.r4.setY(block.r4.getY() - 36);
            }  
        } else if (block.rotation == 3) {
            if (checkRotate(block.r1, -2, 0) && checkRotate(block.r2, 1, 1) &&
                checkRotate(block.r3, 0, 0) && checkRotate(block.r4, -1, -1)) {
                block.rotation++;
                block.r1.setX(block.r1.getX() - 72);
                block.r2.setX(block.r2.getX() + 36);
                block.r2.setY(block.r2.getY() + 36);
                block.r4.setX(block.r4.getX() - 36);
                block.r4.setY(block.r4.getY() - 36);
            }   
        } else if (block.rotation == 4) {
            if (checkRotate(block.r1, 0, -2) && checkRotate(block.r2, -1, -1) &&
                checkRotate(block.r3, 0, 0) && checkRotate(block.r4, 1, 1)) {
                block.rotation = 1;
                block.r1.setY(block.r1.getY() - 72);
                block.r2.setX(block.r2.getX() - 36);
                block.r2.setY(block.r2.getY() - 36);
                block.r4.setX(block.r4.getX() + 36);
                block.r4.setY(block.r4.getY() + 36);
            } 
        }
    }
    
    public void rotateGreen(Block block) {
        if (block.rotation == 1) {
            if (checkRotate(block.r1, 1, -1) && checkRotate(block.r2, 0, 0) &&
                checkRotate(block.r3, 1, 1) && checkRotate(block.r4, 0, 2)) {
                block.rotation++;
                block.r1.setX(block.r1.getX() + 36);
                block.r1.setY(block.r1.getY() - 36);
                block.r3.setX(block.r3.getX() + 36);
                block.r3.setY(block.r3.getY() + 36);
                block.r4.setY(block.r4.getY() + 72);
            }
        } else if (block.rotation == 2) {
            if (checkRotate(block.r1, -1, 1) && checkRotate(block.r2, 0, 0) &&
                checkRotate(block.r3, -1, -1) && checkRotate(block.r4, 0, -2)) {
                block.rotation = 1;
                block.r1.setX(block.r1.getX() - 36);
                block.r1.setY(block.r1.getY() + 36);
                block.r3.setX(block.r3.getX() - 36);
                block.r3.setY(block.r3.getY() - 36);
                block.r4.setY(block.r4.getY() - 72);
            }
        }
    }
    
    public void rotateCyan(Block block) {
        if (block.rotation == 1) {
            if (checkRotate(block.r1, 2, -2) && checkRotate(block.r2, 1, -1) &&
                checkRotate(block.r3, 0, 0) && checkRotate(block.r4, -1, 1)) {
                block.rotation++;
                block.r1.setX(block.r1.getX() + 72);
                block.r1.setY(block.r1.getY() - 72);
                block.r2.setX(block.r2.getX() + 36);
                block.r2.setY(block.r2.getY() - 36);
                block.r4.setX(block.r4.getX() - 36);
                block.r4.setY(block.r4.getY() + 36);
            }
        } else if (block.rotation == 2) {
            if (checkRotate(block.r1, -2, 2) && checkRotate(block.r2, -1, 1) &&
                checkRotate(block.r3, 0, 0) && checkRotate(block.r4, 1, -1)) {
                block.rotation = 1;
                block.r1.setX(block.r1.getX() - 72);
                block.r1.setY(block.r1.getY() + 72);
                block.r2.setX(block.r2.getX() - 36);
                block.r2.setY(block.r2.getY() + 36);
                block.r4.setX(block.r4.getX() + 36);
                block.r4.setY(block.r4.getY() - 36);
            }
        } 
    }
    
    public void rotatePurple(Block block) {
        if (block.rotation == 1) {
            if (checkRotate(block.r1, 1, 1) && checkRotate(block.r2, 0, 0) &&
                checkRotate(block.r3, 0, 0) && checkRotate(block.r4, 0, 0)) {
                block.rotation++;
                block.r1.setX(block.r1.getX() + 36);
                block.r1.setY(block.r1.getY() + 36);
            }
            
        } else if (block.rotation == 2) {
            if (checkRotate(block.r1, 0, 0) && checkRotate(block.r2, -1, 1) &&
                checkRotate(block.r3, 0, 0) && checkRotate(block.r4, 0, 0)) {
                block.rotation++;
                block.r2.setX(block.r2.getX() - 36);
                block.r2.setY(block.r2.getY() + 36);
            }
            
        } else if (block.rotation == 3) {
            if (checkRotate(block.r1, 0, 0) && checkRotate(block.r2, 0, 0) &&
                checkRotate(block.r3, 0, 0) && checkRotate(block.r4, -1, -1)) {
                block.rotation++;
                block.r4.setX(block.r4.getX() - 36);
                block.r4.setY(block.r4.getY() - 36);
            }
            
        } else if (block.rotation == 4) {
            if (checkRotate(block.r1, -1, -1) && checkRotate(block.r2, 1, -1) &&
                checkRotate(block.r3, 0, 0) && checkRotate(block.r4, 1, 1)) {
                block.rotation = 1;
                block.r1.setX(block.r1.getX() - 36);
                block.r1.setY(block.r1.getY() - 36);
                block.r2.setX(block.r2.getX() + 36);
                block.r2.setY(block.r2.getY() - 36);
                block.r4.setX(block.r4.getX() + 36);
                block.r4.setY(block.r4.getY() + 36);
            }
        }
    }
    
    public void rotateOrange(Block block) {
        if (block.rotation == 1) {
            if (checkRotate(block.r1, 1, -1) && checkRotate(block.r2, 0, 0) &&
                checkRotate(block.r3, 0, 1) && checkRotate(block.r4, -1, 2)) {
                block.rotation++;
                block.r1.setX(block.r1.getX() + 36);
                block.r1.setY(block.r1.getY() - 36);
                block.r3.setY(block.r3.getY() + 36);
                block.r4.setX(block.r4.getX() - 36);
                block.r4.setY(block.r4.getY() + 72);
            }          
        } else if (block.rotation == 2) {
            if (checkRotate(block.r1, -1, 1) && checkRotate(block.r2, 0, 0) &&
                checkRotate(block.r3, 0, -1) && checkRotate(block.r4, -1, 0)) {
                block.rotation++;
                block.r1.setX(block.r1.getX() - 36);
                block.r1.setY(block.r1.getY() + 36);
                block.r3.setY(block.r3.getY() - 36);
                block.r4.setX(block.r4.getX() - 36);
            }  
        } else if (block.rotation == 3) {
            if (checkRotate(block.r1, 1, -1) && checkRotate(block.r2, 0, 0) &&
                checkRotate(block.r3, -1, 1) && checkRotate(block.r4, 0, -2)) {
                block.rotation++;
                block.r1.setX(block.r1.getX() + 36);
                block.r1.setY(block.r1.getY() - 36);
                block.r3.setX(block.r3.getX() - 36);
                block.r3.setY(block.r3.getY() + 36);
                block.r4.setY(block.r4.getY() - 72);
            }   
        } else if (block.rotation == 4) {
            if (checkRotate(block.r1, -1, 1) && checkRotate(block.r2, 0, 0) &&
                checkRotate(block.r3, 1, -1) && checkRotate(block.r4, 2, 0)) {
                block.rotation = 1;
                block.r1.setX(block.r1.getX() - 36);
                block.r1.setY(block.r1.getY() + 36);
                block.r3.setX(block.r3.getX() + 36);
                block.r3.setY(block.r3.getY() - 36);
                block.r4.setX(block.r4.getX() + 72);
            }   
        }
    }

    public boolean checkRotate(Rectangle r, int x, int y) {
        if (r.getX() + (x * 36) < 820 && r.getX() + (x * 36) >= 460 &&
            r.getY() + (y * 36) >= 0 && r.getY() + (y * 36) < 720) {
            if (grid[(int)((r.getX() - 460) / 36) + x][(int)(r.getY() / 36) + y] == 0) {
                return true;
            }
        }
        return false;
    } //check
    
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
                score += 40;
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
