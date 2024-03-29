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
import javafx.scene.Node;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.io.File;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import java.util.Arrays;


/**
 * This class is a javaFX made game of tetris.
 *
 */
public class GameOne {

    /** 2D array containing every possible spot for each block. */
    public final boolean[][] grid = new boolean[10][20];

    /** Images to be used to the sides of the main game. */
    ImageView board = new ImageView(new Image("file:resources/board.png"));

    //Lists involved when a row is full and must be removed
    /** List to hold all of the lines that are to be removed. */ 
    ArrayList<Integer> linesToRemove = new ArrayList<>();
    int[] linesToRem = new int[0];

    /** List to hold all of the rectangles to be removed. */
    ArrayList<Rectangle> rectangles = new ArrayList<>();

    /** List to hold all of the rectangles to fall after deleting lines. */
    ArrayList<Rectangle> rectanglesMove = new ArrayList<Rectangle>();
    
    int score = 0; //the score of the game
    int level = 0;
    Block mainBlock = makeBlock(); //block that is controlled
    Block nextBlock = makeBlock(); //next block to be controlled
    Group group = new Group();
    Scene scene;
    Text scoreText;
    Text levelText;
    //create new timeline
    KeyFrame keyFrame;
    Timeline timeline;
    ArcadeApp app;
    String initials = "";

    /**
     * The constructor of the tetris game.
     * @param app The parent {@code ArcadeApp} used for switching scenes.
     */
    public GameOne(ArcadeApp app) {
        this.app = app;
        scene = new Scene(group, 1280, 720);
        for (int i = 0; i < 10; i++) {
            for (int x = 0; x < 20; x++) {
                grid[i][x] = false; //fill grid with falses
            } //for
        } //for

        scoreText = new Text("Score: " + score);
        scoreText.setX(915);
        scoreText.setY(150);
        scoreText.setFont(Font.font("Verdana", FontWeight.BOLD, 40));

        levelText = new Text("Level: " + level);
        levelText.setX(915);
        levelText.setY(250);
        levelText.setFont(Font.font("Verdana", FontWeight.BOLD, 40));

        Button quit = new Button("Quit");
        quit.setTranslateX(50);
        quit.setTranslateY(50);
        quit.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    app.gameState = "MENU";
                    app.updateScene();
                    timeline.stop();
                }
            });
        group.getChildren().addAll(board, quit, scoreText, levelText);
        try { 
            initials = getInitials();
        } catch (NullPointerException npe) {
            System.out.println("error");
        } //
        timeline = new Timeline(); //create timeline
        
        setUpBlocks(); //call helper method

        makeTimeline(1000); //start timeline

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
     * Creates a timeline to run the game loop with a variable delay.
     * @param milis The miliseconds between each loop execution.
     */
    private void makeTimeline(int milis) {
        EventHandler<ActionEvent> handler = event -> runner();
        keyFrame = new KeyFrame(Duration.millis(milis), handler);
        timeline.stop();
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    /**
     * The method that results in a moving block.
     *
     * @param block the block to be moved
     * @return EventHandler the keyPress
     */
    private EventHandler<? super KeyEvent> keyPressed (Block block) {
        return event -> {           
            switch (event.getCode()) {
            case LEFT:  // Move piece left
                moveLeft(block);
                break;
            case RIGHT: // Move piece right
                moveRight(block);
                break;
            case DOWN: // speed up piece
                moveDown(block);
                score++;
                break;
            case UP: //rotate
                rotateBlock(block);
                break;      
            } //switch
        }; //event
    } //keyPressed

    /**
     * Helper method to make set up blocks and make them fall.
     * Also checks if the row is full of Rectangels
     *
     */
    private void setUpBlocks() {
        mainBlock = makeBlock(); //block to be controlled
        
        group.getChildren().addAll(mainBlock.r1, mainBlock.r2, mainBlock.r3, mainBlock.r4);
        //add the block to the group to make it visible
        
        group.setOnKeyPressed(keyPressed(mainBlock)); //call back the method returning a key-event
        nextBlock = makeBlock();//go ahead and create the next block to fall
        
        group.requestFocus(); //request focus for the group
    }

    int timer = 0;
    boolean playing = true;
    Text gameOver = new Text("GAME OVER");

    /**
     * Main runner class for the game.
     */
    public void runner() {
        if (mainBlock.r1.getY() == 0 || mainBlock.r2.getY() == 0 ||
            mainBlock.r3.getY() == 0 || mainBlock.r4.getY() == 0) {
            timer++;
        } else {
            timer = 0;
        }
        if (timer == 3) {
            gameOver.setX(180);
            gameOver.setY(360);
            gameOver.setFill(Color.RED);
            gameOver.setFont(Font.font("Verdana", FontWeight.BOLD, 150));
            group.getChildren().add(gameOver);
            playing = false;
            timeline.stop();
            scoreUpdate();
            timeline.play();
        }
        if (timer == 10) {
            app.gameState = "MENU";
            app.updateScene();
            timeline.stop();
        }
        if (playing) {
            moveDown(mainBlock);
            scoreText.setText("Score: " + score);
            levelText.setText("Level: " + level);
        }
        if (score > 1000 && level == 0) {
            level++;
            makeTimeline(1000 - 100 * level);
        } else if (score > 2000 && level == 1) {
            level++;
            makeTimeline(1000 - 100 * level);
        } else if (score > 3000 && level == 2) {
            level++;
            makeTimeline(1000 - 100 * level);
        } else if (score > 4000 && level == 3) {
            level++;
            makeTimeline(1000 - 100 * level);
        } else if (score > 5000 && level == 4) {
            level++;
            makeTimeline(1000 - 100 * level);
        }
    }

    File scoreTetris;

    /**
     * Gets and then updates the scores in the *hidden* file that keeps
     * track of the scores for the game.
     */
    public void scoreUpdate() {
        scoreTetris = new File("resources/scoreTetris.txt");
        ArrayList<Integer> topScores = new ArrayList<Integer>();
        ArrayList<String> topInitials = new ArrayList<String>();
        try {
            String line = "";
            BufferedReader br = new BufferedReader(new FileReader(scoreTetris));
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(",");
                topInitials.add(temp[0] + "");
                topScores.add(Integer.parseInt(temp[1]));
            }
            br.close();
            for (int i = 0; i < topScores.size(); i++) {
                if (score > topScores.get(i)) {
                    String newInitials = initials;

                    topScores.add(i, score);
                    topInitials.add(i, newInitials);
                    topScores.remove(topScores.size() - 1);
                    topInitials.remove(topInitials.size() - 1);
                    break;
                }
            }
            scoreTetris.delete();
            scoreTetris = new File("resources/scoreTetris.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(scoreTetris));
            writer.write(topInitials.get(0) + "," + topScores.get(0) + "\n");
            writer.write(topInitials.get(1) + "," + topScores.get(1) + "\n");
            writer.write(topInitials.get(2) + "," + topScores.get(2));
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.print("ERROR");
        } catch (IOException e) {
            System.out.print("ERROR");
        }
    }

    /**
     * Pops up a box to get the initials of the players involved in the game.
     *
     * @return The initials input by the user.
     */
    public String getInitials() {
       
        TextInputDialog dialog = new TextInputDialog("");
        String initials = "";
        dialog.setTitle("Initial Input");
        dialog.setHeaderText("Enter your initials:");
        dialog.setContentText("Initials:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            initials = result.get();
        }
        return initials;
    }
    
    /**
     * This method removes full rows in the tetris game.
     *
     */
    private void clearRows() {
        
        int size = group.getChildren().size(); //number of children in group

        for (int i = 0; i < size; i++) {
            //transverse through the group
            Node node = group.getChildren().get(i); 
            if (node instanceof Rectangle) { //check if node = rectangle
                //the node needs to be added to the list
                this.rectangles.add((Rectangle)node); //add to list 
            } //if
        } //for

        while (this.linesToRemove.size() >= 1) {
            //loop while there are still lines to be removed
            score += 100; //add to score for every cut line
            cutLine(); //helper method to cut the lines and drop other rectangles
        } //while
        
    } //removeRows

    /**
     * Checks to see which type of block is being rotated and calls the
     * corresponding method.
     * @param block The block being rotated.
     */
    public void rotateBlock(Block block) {
        if (block.type.equals("red")) { //red block
            rotateRed(block);
        } else if (block.type.equals("blue")) { //blue block
            rotateBlue(block);
        } else if (block.type.equals("green")) { //green block
            rotateGreen(block);
        } else if (block.type.equals("cyan")) { //cyan block
            rotateCyan(block);
        } else if (block.type.equals("purple")) { //purple block
            rotatePurple(block);
        } else if (block.type.equals("orange")) { //orange block
            rotateOrange(block);
        } //if-else
    } //rotate

    /**
     * Rotates the red block.
     * @param block The block being rotated.
     */
    public void rotateRed(Block block) {
        //only requires 2 rotations in total
        if (block.rotation == 1) {
            if (checkRotate(block.r1, 1, 0) && checkRotate(block.r2, 0, 1) &&
                checkRotate(block.r3, -1, 0) && checkRotate(block.r4, -2, 1)) {
                //if you are here, red has passed the rotation requirements.
                block.rotation++;
                block.r1.setX(block.r1.getX() + 36); //shift spot of r1 block
                block.r2.setY(block.r2.getY() + 36); //shift spot of r2 block
                block.r3.setX(block.r3.getX() - 36); //shift spot of r3 block
                block.r4.setX(block.r4.getX() - 72); //shift spot of r4 block
                block.r4.setY(block.r4.getY() + 36); //shift HEIGHT of r4 block
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

    /**
     * Rotates the blue block.
     * @param block The block being rotated.
     */
    public void rotateBlue(Block block) {
        //Hard one, requires 4 total rotations
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

    /**
     * Rotates the green block.
     * @param block The block being rotated.
     */
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

    /**
     * Rotates the cyan block.
     * @param block The block being rotated.
     */
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
            } //if
        } //else-if
    } //if

    /**
     * Rotates the purple block.
     * @param block The block being rotated.
     */
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

    /**
     * Rotates the orange block.
     * @param block The block being rotated.
     */
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

    /**
     * Checks to see if a rotation is "legal".
     * @param r The rectangle being checked.
     * @param x The x direction it is being moved.
     * @param y The y direction it is being moved.
     * @return If it is a "legal" rotation/move.
     */
    public boolean checkRotate(Rectangle r, int x, int y) {
        if (r.getX() + (x * 36) < 820 && r.getX() + (x * 36) >= 460 &&
            r.getY() + (y * 36) >= 0 && r.getY() + (y * 36) < 720) {
            //if you are here, the block is not going to rotate into a wall
            if (grid[(int)((r.getX() - 460) / 36) + x][(int)(r.getY() / 36) + y] == false) {
                //here, there is no other block that is obstructing, so you can rotate
                return true;
            }
        }
        return false; //cant rotate
    } //check

    /**
     * The method that causes the block to fall down by its 36,
     * every 10 seconds. Also checks if a row is full
     *
     * @param block The block to fall down
     */
    public void moveDown(Block block) {
        try {
            if (block.r1.getY() == 684 || block.r2.getY() == 684 ||
                block.r3.getY() == 684 || block.r4.getY() == 684 ||
                grid[(int)(block.r1.getX() - 460) / 36][((int)block.r1.getY() / 36) + 1] ||
                grid[(int)(block.r2.getX() - 460) / 36][((int)block.r2.getY() / 36) + 1] ||
                grid[(int)(block.r3.getX() - 460) / 36][((int)block.r3.getY() / 36) + 1] ||
                grid[(int)(block.r4.getX() - 460) / 36][((int)block.r4.getY() / 36) + 1]) {
                //if you are here, the block has passed the criteria for stopping
                score += 40; //move up score by 40 

                //Fill the spot in the double array, block hit a solid. -> true = taken spot
                grid[(int)(block.r1.getX() - 460) / 36][(int)block.r1.getY() / 36] = true;
                grid[(int)(block.r2.getX() - 460) / 36][(int)block.r2.getY() / 36] = true;
                grid[(int)(block.r3.getX() - 460) / 36][(int)block.r3.getY() / 36] = true;
                grid[(int)(block.r4.getX() - 460) / 36][(int)block.r4.getY() / 36] = true;

                this.linesToRemove = new ArrayList<Integer>(); //make sure lists are new to check
                this.rectangles = new ArrayList<Rectangle>();
                this.rectanglesMove = new ArrayList<Rectangle>();

                
                int lineCount = 0; //goes up one for each full spot
                for (int i = 0; i < 20; i++) {
                    int sLine = i; //the specific line that is to be removed   
                    for (int x = 0; x < 10; x++) {
                        //2 loops to go through entire 2D array
                        if (grid[x][i]) {
                            //if you are here, this spot is full 
                            lineCount++; //add 1 for every full spot
                        } //if
                        if (lineCount > 9) {
                            this.linesToRemove.add(sLine);
                        } //if
                    } //for
                    lineCount = 0; //reset counter      
                }
                if (linesToRemove.size() > 0) { //check if rows need to be deleted
                    clearRows();
                } //if
                
                setUpBlocks();
                for (int i = 0; i < 10; i++) {
                    if (grid[i][0]) {
                        playing = false;
                    } //if
                } //for

            } //if

            keepFalling(block); //method to check if falling should continue
          
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        } //catch
    }  //movDown

    /**
     * Helper method to use a block to determine if it
     * should continue falling.
     *
     * @param block the block to be checked
     */
    private void keepFalling(Block block) {
        if (block.r1.getY() + 36 < 720 && block.r2.getY() + 36 < 720 &&
            block.r3.getY() + 36 < 720 && block.r4.getY() + 36 < 720 &&
            !grid[(int)(block.r1.getX() - 460) / 36][((int)block.r1.getY() / 36) + 1] &&
            !grid[(int)(block.r2.getX() - 460) / 36][((int)block.r2.getY() / 36) + 1] &&
            !grid[(int)(block.r3.getX() - 460) / 36][((int)block.r3.getY() / 36) + 1] &&
            !grid[(int)(block.r4.getX() - 460) / 36][((int)block.r4.getY() / 36) + 1])  {
            //checking if the spot below is free for taking
            block.r1.setY(block.r1.getY() + 36); //move down 1
            block.r2.setY(block.r2.getY() + 36);
            block.r3.setY(block.r3.getY() + 36);
            block.r4.setY(block.r4.getY() + 36);
        } //if 
        
    } //keepFalling
    
    /**
     * Moves the block one spot to the left.
     *
     * @param block The block to be moved
     */
    public void moveLeft(Block block) {
        
        if (checkLeft(block) == true) {
            block.r1.setX(block.r1.getX() - 36);
            block.r2.setX(block.r2.getX() - 36);
            block.r3.setX(block.r3.getX() - 36);
            block.r4.setX(block.r4.getX() - 36);
        } //if
        
    } //move left

    /**
     * Helper method to see if a move left is open.
     *
     * @param block the block to be checked
     * @return true if the block can move
     */
    private boolean checkLeft(Block block) {
        boolean b = false;
        int r1x = ((int)block.r1.getX() - 460) / 36 - 1; //the future x spot
        int r2x = ((int)block.r2.getX() - 460) / 36 - 1;
        int r3x = ((int)block.r3.getX() - 460) / 36 - 1;
        int r4x = ((int)block.r4.getX() - 460) / 36 - 1;
        try {
            if (block.r1.getX() - 496 >= 0 && block.r2.getX() - 496 >= 0 &&
                block.r3.getX() - 496 >= 0 && block.r4.getX() - 496 >= 0 &&
                !grid[r1x][(int)block.r1.getY() / 36] &&
                !grid[r2x][(int)block.r2.getY() / 36] &&
                !grid[r3x][(int)block.r3.getY() / 36] &&
                !grid[r4x][(int)block.r4.getY() / 36])  {
                //if you are here, the block has all rights to move left
                b = true; //the block can move
            } //if

        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        } //exception

        return b;
        
    } //block

    /**
     * Moves block to the right.
     *
     * @param block The moving item
     */
    public void moveRight(Block block) {

        if (checkRight(block) == true) {
            block.r1.setX(block.r1.getX() + 36);
            block.r2.setX(block.r2.getX() + 36);
            block.r3.setX(block.r3.getX() + 36);
            block.r4.setX(block.r4.getX() + 36);
        } //if 

    } //moveRight
    
    /** 
     * Helper method to see if a move right is open.
     *
     * @param block the block to be checked
     * @return true if the block can move 
     */
    private boolean checkRight(Block block) {
        boolean b = false;
        int r1x = ((int)block.r1.getX() - 460) / 36 + 1; //the future x spot
        int r2x = ((int)block.r2.getX() - 460) / 36 + 1;
        int r3x = ((int)block.r3.getX() - 460) / 36 + 1;
        int r4x = ((int)block.r4.getX() - 460) / 36 + 1;
        try {
            if (block.r1.getX() - 460 + 36 < 360 && block.r2.getX() - 460 + 36 < 360 &&
                block.r3.getX() - 460 + 36 < 360 && block.r4.getX() - 460 + 36 < 360 &&
                !grid[r1x][(int)block.r1.getY() / 36] &&
                !grid[r2x][(int)block.r2.getY() / 36] &&
                !grid[r3x][(int)block.r3.getY() / 36] &&
                !grid[r4x][(int)block.r4.getY() / 36])  {
                //if you are here, the block has all rights to move right
                b = true; //the block can move
            } //if 

        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        } //exception

        return b;

    } //block  

    /**
     * Makes the block to be used.
     *
     * @return The block to be created
     */
    public Block makeBlock() {
        int color = (int)(Math.random() * 7); //7 total blocks
        Block theBlock = new Block();
        
        if (color == 0) { //the color cyan block will be made       
            theBlock = setCyan();
            
        } else if (color == 1) { //blue
            theBlock = setBlue();
            
        } else if (color == 2) { //orange
            theBlock = setOrange();
            
        } else if (color == 3) { //yellow
            theBlock = setYellow();
         
        }  else if (color == 4) { //green
            theBlock = setGreen();
            
        } else if (color == 5) { //purple
            theBlock = setPurple();
            
        } else if (color == 6) { //red
            theBlock = setRed();
            
        } //else if
        
        return theBlock; //return the block to fall
        
    } //makeBlock

    /**
     * Helper method to create a cyan block.
     *
     * @return The cyan colored block
     */
    public Block setCyan() {
        Rectangle r1 = new Rectangle(36, 36); //make the 4 blocks
        Rectangle r2 = new Rectangle(36, 36);
        Rectangle r3 = new Rectangle(36, 36);
        Rectangle r4 = new Rectangle(36, 36);
        Block b = new Block(r1, r2, r3, r4, "cyan"); //type cyan

        //move the blocks into the correct spot
        b.r1.setX(568);
        b.r2.setX(604);
        b.r3.setX(640);
        b.r4.setX(676);
        return b;
    } //setCyan

    /** 
     * Helper method to create a blue block.
     *
     * @return The blue colored block
     */
    public Block setBlue() {
        Rectangle r1 = new Rectangle(36, 36);
        Rectangle r2 = new Rectangle(36, 36);
        Rectangle r3 = new Rectangle(36, 36);
        Rectangle r4 = new Rectangle(36, 36);
        Block b = new Block(r1, r2, r3, r4, "blue");

        b.r1.setX(604);
        b.r1.setY(0 - 36);
        b.r2.setX(604);
        b.r3.setX(640);
        b.r4.setX(676);
        return b;
    } //set blue

    /** 
     * Helper method to create a orange block.
     *
     * @return The orange colored block
     */
    public Block setOrange() {
        Rectangle r1 = new Rectangle(36, 36);
        Rectangle r2 = new Rectangle(36, 36);
        Rectangle r3 = new Rectangle(36, 36);
        Rectangle r4 = new Rectangle(36, 36);
        Block b = new Block(r1, r2, r3, r4, "orange");

        b.r1.setX(604);
        b.r2.setX(640);
        b.r3.setX(676);
        b.r4.setX(676);
        b.r4.setY(0 - 36);
        return b;
    } //setOrange

    /** 
     * Helper method to create a yellow block.
     *
     * @return The yellow colored block
     */
    public Block setYellow() {
        Rectangle r1 = new Rectangle(36, 36);
        Rectangle r2 = new Rectangle(36, 36);
        Rectangle r3 = new Rectangle(36, 36);
        Rectangle r4 = new Rectangle(36, 36);
        Block b = new Block(r1, r2, r3, r4, "yellow");

        b.r1.setX(604);
        b.r1.setY(0 - 36);
        b.r2.setX(640);
        b.r2.setY(0 - 36);
        b.r3.setX(604);
        b.r4.setX(640);
        return b;
    } //set yellow

    /** 
     * Helper method to create a green block.
     *
     * @return The green colored block
     */
    public Block setGreen() {
        Rectangle r1 = new Rectangle(36, 36);
        Rectangle r2 = new Rectangle(36, 36);
        Rectangle r3 = new Rectangle(36, 36);
        Rectangle r4 = new Rectangle(36, 36);
        Block b = new Block(r1, r2, r3, r4, "green");

        b.r1.setX(604);
        b.r2.setX(640);
        b.r3.setX(640);
        b.r3.setY(0 - 36);
        b.r4.setX(676);
        b.r4.setY(0 - 36);
        return b;
    } //setGreen

    /** 
     * Helper method to create a purple block.
     *
     * @return The purple colored block
     */
    public Block setPurple() {
        Rectangle r1 = new Rectangle(36, 36);
        Rectangle r2 = new Rectangle(36, 36);
        Rectangle r3 = new Rectangle(36, 36);
        Rectangle r4 = new Rectangle(36, 36);
        Block b = new Block(r1, r2, r3, r4, "purple");

        b.r1.setX(604);
        b.r2.setX(640);
        b.r2.setY(0 - 36);
        b.r3.setX(640);
        b.r4.setX(676);
        return b;
    } //setPurple

    /** 
     * Helper method to create a red block.
     *
     * @return The red colored block
     */
    public Block setRed() {
        Rectangle r1 = new Rectangle(36, 36);
        Rectangle r2 = new Rectangle(36, 36);
        Rectangle r3 = new Rectangle(36, 36);
        Rectangle r4 = new Rectangle(36, 36);
        Block b = new Block(r1, r2, r3, r4, "red");

        b.r1.setX(604);
        b.r1.setY(0 - 36);
        b.r2.setX(640);
        b.r2.setY(0 - 36);
        b.r3.setX(640);
        b.r4.setX(676);
        return b;
    } //setRed

    /**
     * Helper method to cut the lines that are full.
     *
     *
     */
    private void cutLine() {
        
        for (int i = 0; i < this.rectangles.size(); i++) {
            if (this.rectangles.get(i).getY() == this.linesToRemove.get(0) * 36) {
                Rectangle rec = this.rectangles.get(i);
                grid[(int)((rec.getX() - 460) / 36)][(int)(rec.getY() / 36)] = false;
                group.getChildren().remove(rec); //remove the rectangle
            } else {
                Rectangle rec = this.rectangles.get(i);
                if (rec.getY() < this.linesToRemove.get(0) * 36) {
                    this.rectanglesMove.add(rec);
                } //if
            } //else
        }
        for (int i = 0; i < this.rectanglesMove.size(); i++) {
            Rectangle rec = this.rectanglesMove.get(i);
            grid[(int)((rec.getX() - 460) / 36)][(int)(rec.getY() / 36)] = false;
            grid[(int)((rec.getX() - 460) / 36)][(int)((rec.getY() + 36) / 36)] = true;
            this.rectanglesMove.get(i).setY(this.rectanglesMove.get(i).getY() + 36);
            
        }       
        
        this.rectangles = new ArrayList<Rectangle>(); //empty the list
        
        int size = group.getChildren().size();          
        for (int i = 0; i < size; i++) {
            //transverse through the group
            Node node = group.getChildren().get(i);
            if (node instanceof Rectangle) {
                //the node needs to be added to the list
                this.rectangles.add((Rectangle)node); //add to list
            } //if
        } //for 
        
        for (int i = 0; i < this.rectangles.size(); i++) {
            try {
                Rectangle rec = this.rectangles.get(i);
                grid[(int)((rec.getX() - 460) / 36)][(int)(rec.getY() / 36)] = true;
            } catch (IndexOutOfBoundsException e) {
                System.out.print("");
            } //try-catch
        } //for 
        this.rectanglesMove = new ArrayList<Rectangle>();
        this.linesToRemove.remove(0); //take off list
        
    } //cutLines
}
