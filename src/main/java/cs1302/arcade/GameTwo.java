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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;


/**
 * This class is the main class for a javaFX chess game.
 *
 */
public class GameTwo {

    /** double array containing every possible spot for each piece. */
    public final int[][] grid = new int[8][8]; //8x8 game board

    /** Image to be used as the game board. */
    ImageView board = new ImageView(new Image("file:resources/chessBoard.png"));
    Image posRoute = new Image("file:resources/pos.png"); //highlights routes


    int score = 100; //score goes down with each loss of a piece
    Group group = new Group();
    ArcadeApp app;
    Scene scene;
    Text score1Text;
    Text score2Text;

    Boolean turn = true; //which turn it is

    ArrayList<Piece> pieces = new ArrayList<Piece>();
    
    String[] typeArray = {"bR", "bH", "bB", "bQ", "bK", "bB", "bH", "bR",
        "bP", "bP", "bP", "bP", "bP", "bP", "bP", "bP",
        "wP", "wP", "wP", "wP", "wP", "wP", "wP", "wP",
        "wR", "wH", "wB", "wK", "wQ", "wB", "wH", "wR"};
    
    /**
     * The constructor of the chess game.
     *
     * @param app The parent {@code ArcadeApp} used for scene-change
     */
    public GameTwo(ArcadeApp app) {
        this.app = app;
        group.getChildren().add(board);
        scene = new Scene(group, 1280, 720);
        for (int i = 0; i < 8; i++) {
            for (int x = 0; x < 8; x++) {
                if (i == 0 || i == 1) {
                    grid[i][x] = 1; //1 = white piece
                } else if (i == 6 || i == 7) {
                    grid[i][x] = 2; //2 = black piece
                } else {
                    //if you are here, the spot is empty
                    grid[i][x] = 0; //empty spot
                } //else
            } //for
        } //for
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 8; i++) {
                int x = 280 + (i * 90);
                int y = j * 90 + 360;
                if (j < 2) {
                    y = j * 90;
                } 
                String type = typeArray[j * 8 + i];
                Piece p = new Piece(x, y, type);
                
                pieces.add(p);
            }
        }
        
        for (int i = 0; i < pieces.size(); i++)  {
            group.getChildren().add(pieces.get(i));
        }

        score1Text = new Text("" + score);
        score1Text.setX(1105);
        score1Text.setY(175);
        score1Text.setFont(Font.font("Verdana", FontWeight.BOLD, 40));

        score2Text = new Text("" + score);
        score2Text.setX(115);
        score2Text.setY(175);
        score2Text.setFont(Font.font("Verdana", FontWeight.BOLD, 40));

        Button quit = new Button("Quit");
        quit.setTranslateX(10);
        quit.setTranslateY(690);
        quit.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    app.gameState = "MENU";
                    app.updateScene();
                }
            });
        group.getChildren().addAll(quit, score1Text, score2Text);
        group.setOnMouseClicked(createMouseHandler()); 

    } //gameTwo

    /**
     * Getter for the scene
     *
     * @return the scene
     */
    public Scene getScene() {
        return scene;
    } //getter
    
    private EventHandler<? super MouseEvent> createMouseHandler() {
        return event -> {
            int x = (int)event.getX();
            int y = (int)event.getY();
            x = (int)Math.floor((x - 280) / 90);
            y = (int)Math.floor(y / 90);
            System.out.println("X: " + x + " Y: " + y);
            for (int i = 0; i < pieces.size(); i++) {
                if ((int)Math.floor((pieces.get(i).getX() - 280) / 90) == x &&
                    (int)Math.floor(pieces.get(i).getY() / 90) == y) {
                    if (type.equals("wP")) {
                        moveWhitePawn(pieces.get(i));
                    } else if (type.equals("bP")) {
                        moveBlackPawn(pieces.get(i));
                    } else if (type.contains("R")) {
                        moveRook(pieces.get(i));
                    } else if (type.contains("H")) {
                        moveHorse(pieces.get(i));
                    } else if (type.contains("B")) {
                        moveBishop(pieces.get(i));
                    } else if (type.contains("K")) {
                        moveKing(pieces.get(i));
                    } else if (type.contains("Q")) {
                        moveQueen(pieces.get(i));
                    }
                    System.out.println(pieces.get(i).type);
                }
            }
        };
    } // createMouseHandler



}
