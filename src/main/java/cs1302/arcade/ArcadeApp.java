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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.scene.text.FontWeight;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.File;
import javafx.scene.text.Text;

/**
 * Application subclass for {@code ArcadeApp}.
 * @version 2019.fa
 */
public class ArcadeApp extends Application {

    VBox vbox;
    String gameState = "ANIMATION";
    Stage stage;
    Rectangle projectName = new Rectangle(1280, 720);
    Rectangle teamName = new Rectangle(1280, 720);
    Rectangle name = new Rectangle(1280, 720);
    Timeline timeline = new Timeline();
    MenuBar menuBar;
    GameOne gameOne;
    GameTwo gameTwo;

    /**
     * This method creates an HBox called menu and returns it into a new scene.
     * @return The next HBox being created.
     */
    public VBox menu() {
        HBox hbox = new HBox();
        VBox vbox = new VBox();
        Button gameOneButton = new Button();
        Button gameTwoButton = new Button();
        Image imageOne = new Image("file:resources/tetris.png");
        Image imageTwo = new Image("file:resources/chess.png");
        menuBar = menuMaker(); //helper method 
        
        gameOneButton.setGraphic(new ImageView(imageOne));
        gameTwoButton.setGraphic(new ImageView(imageTwo));
        hbox.getChildren().addAll(gameOneButton, gameTwoButton);
        vbox.getChildren().addAll(menuBar, hbox);
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
        return vbox;
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
        } else if (gameState.equals("GAMEONE")) {
            gameOne = new GameOne(this);
            stage.setScene(gameOne.getScene());
            stage.setTitle("Tetris");
        } else if (gameState.equals("GAMETWO")) {
            gameTwo = new GameTwo(this);
            stage.setScene(gameTwo.getScene()); 
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
        if (name.getX() == -1300) {
            timeline.stop();
            gameState = "MENU";
            updateScene(); 
        }
    }

    /**
     * Makes a menu bar and adds it to the scene.
     *
     * @return The created menu bar to be added to the scene.
     */
    public MenuBar menuMaker() {       
        MenuBar menBar = new MenuBar();
        try {
            Menu menu = new Menu("High scores");
            menBar.getMenus().addAll(menu); //add to bar
    
            MenuItem tetrisItem = new MenuItem("Tetris");
            MenuItem chessItem = new MenuItem("Chess");
    
            tetrisItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        getScoreScene(1);
                    }
                });
            chessItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        getScoreScene(2);
                    }
                });
            menu.getItems().addAll(tetrisItem, chessItem);
        } catch (NullPointerException e) {
            System.out.print("");
        }
        return menBar;
    } //menu
    
    File scoreTetris = new File("resources/scoreTetris.txt");
    File scoreChess = new File("resources/scoreChess.txt");

    /**
     * Pops Up a new window that shows the scores of the designated game.
     * 
     * @param g The "game" being looked at.
     */
    public void getScoreScene(int g) {
        Group groupT = new Group();
        Scene sceneT = new Scene(groupT, 640, 720);
        Stage stageT = new Stage();
        stageT.setScene(sceneT);
        stageT.initOwner(stage);
        stageT.initModality(Modality.APPLICATION_MODAL);
        Text title = new Text();
        Text scoreOne = new Text();
        Text scoreTwo = new Text();
        Text scoreThree = new Text();
        ArrayList<Integer> topScores = new ArrayList<>();
        ArrayList<String> topInitials = new ArrayList<>();
        File scoreT = new File("");
        if (g == 1) {
            scoreT = scoreTetris;
            title.setText("Tetris Top Scores:");
        } else if (g == 2) {
            scoreT = scoreChess;
            title.setText("Chess Top Scores:");
        }
        try {
            String line = "";
            BufferedReader br = new BufferedReader(new FileReader(scoreT));
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(",");
                topInitials.add(temp[0] + "");
                topScores.add(Integer.parseInt(temp[1]));        
            } //while
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR");
        } catch (IOException e) {
            System.out.println("ERROR");
        } //catch
        scoreOne.setText(topInitials.get(0) + ":  " + topScores.get(0));
        scoreTwo.setText(topInitials.get(1) + ":  " + topScores.get(1));
        scoreThree.setText(topInitials.get(2) + ":  " + topScores.get(2));
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        scoreOne.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        scoreTwo.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        scoreThree.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        title.setFill(Color.RED);
        scoreOne.setFill(Color.RED);
        scoreTwo.setFill(Color.RED);
        scoreThree.setFill(Color.RED);
        title.setX(100);
        title.setY(100);
        scoreOne.setX(100);
        scoreOne.setY(200);
        scoreTwo.setX(100);
        scoreTwo.setY(300);
        scoreThree.setX(100);
        scoreThree.setY(400);
        groupT.getChildren().addAll(title, scoreOne, scoreTwo, scoreThree);
        stageT.showAndWait();
    } //getScoreScene
      
    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;      
        stage.setTitle("cs1302-arcade!");
        updateScene();
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    } // start

} // ArcadeApp
