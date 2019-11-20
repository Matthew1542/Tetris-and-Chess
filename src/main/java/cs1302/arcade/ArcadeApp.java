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
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

/**
 * Application subclass for {@code ArcadeApp}.
 * @version 2019.fa
 */
public class ArcadeApp extends Application {
    
    String gameState = "ANIMATION";
    Stage stage;
    Rectangle projectName = new Rectangle(1280, 720);
    Rectangle teamName = new Rectangle(1280, 720);
    Rectangle name = new Rectangle(1280, 720);
    Timeline timeline = new Timeline();
    
    GameOne gameOne;
    GameTwo gameTwo;

    /**
     * This method creates an HBox called menu and returns it into a new scene.
     * @return The next HBox being created.
     */
    public HBox menu() {
        HBox hbox = new HBox();
        Button gameOneButton = new Button();
        Button gameTwoButton = new Button();
        Image imageOne = new Image("file:resources/tetris.png");
        Image imageTwo = new Image("file:resources/chess.png");
        gameOneButton.setGraphic(new ImageView(imageOne));
        gameTwoButton.setGraphic(new ImageView(imageTwo));
        hbox.getChildren().addAll(gameOneButton, gameTwoButton);
        gameOneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                gameState = "GAMEONE";
                updateScene();
            }
        });
        gameTwoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                gameState = "GAMETWO";
                updateScene();
            }
        });
        return hbox;
    }
    
    /**
     * This method creates a group called animation and returns it into a new
     * scene. Animation is the animated opening for the program.
     * @return The group being returned.
     */
    public Group animation() {
        Group group = new Group();
        group.getChildren().addAll(projectName, teamName, name);   
        Image projectNameImage = new Image("file:resources/projectName.png");
        Image teamNameImage = new Image("file:resources/teamName.png");
        Image nameImage = new Image("file:resources/name.png");
        projectName.setFill(new ImagePattern(projectNameImage));
        projectName.setX(1280);
        projectName.setY(0);
        teamName.setFill(new ImagePattern(teamNameImage));
        teamName.setX(1280 * 2);
        teamName.setY(0);
        name.setFill(new ImagePattern(nameImage));
        name.setX(1280 * 3);
        name.setY(0);
        startAnimation();
        return group;
    }
    
    /**
     * Updates the scene to the previously specified {@code gameState}.
     */
    public void updateScene() {
        if (gameState.equals("ANIMATION")) {
            stage.setScene(new Scene (animation(), 1280, 720));
        } else if (gameState.equals("MENU")) {
            stage.setScene(new Scene(menu(), 1280, 720));
	    stage.setTitle("Menu");
            System.out.println("MENU");
        } else if (gameState.equals("GAMEONE")) {
            stage.setScene(new Scene(gameOne, 1280, 720));
	    stage.setTitle("Tetris");
        } else if (gameState.equals("GAMETWO")) {
            stage.setScene(new Scene(gameTwo, 1280, 720));
	    stage.setTitle("Chess");
        } else { 
            System.out.println("INVALID GAMESTATE");
        }
    }
    
    /**
     * Creates the KeyFrame and timeline for the opening animation.
     */
    public void startAnimation() {
        EventHandler<ActionEvent> handler = event -> moveRect();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000 / 60), handler);
        timeline.setCycleCount(Timeline.INDEFINITE);        
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
    
    /**
     * Moves the rectangles (Pictures) across the screen for the animation.
     */
    public void moveRect() {
        double changeAmount = 4;
        projectName.setX(projectName.getX() - changeAmount);
        teamName.setX(teamName.getX() - changeAmount);
        name.setX(name.getX() - changeAmount);
        try {
            if (projectName.getX() == 0) {
                timeline.pause();
                Thread.sleep(2000);
                timeline.play();
            }
            if (teamName.getX() == 0) {
                timeline.pause();
                Thread.sleep(2000);
                timeline.play();
            }
            if (name.getX() == 0) {
                timeline.pause();
                Thread.sleep(2000);
                timeline.play();
            }
        } catch (InterruptedException e) {
            System.out.println("INTERRUPTION, MOM GET OUT OF MY ROOM!");
        }
        if (name.getX() == -1280) {
            timeline.stop();
            gameState = "MENU";
            updateScene(); 
        }
    }
      
    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;      
        stage.setTitle("cs1302-arcade!");
        updateScene();
        stage.sizeToScene();
        stage.show();
    } // start

} // ArcadeApp
