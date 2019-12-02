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
    Image blackPawn = new Image("file:resources/blackPawn.png"); //pawn
    Image whitePawn = new Image("file:resources/whitePawn.png"); //pawn
    Image blackRook = new Image("file:resources/blackRook.png"); //rook
    Image whiteRook = new Image("file:resources/whiteRook.png"); //rook
    Image blackBishop = new Image("file:resources/blackBishop.png"); //bishop
    Image whiteBishop = new Image("file:resources/whiteBishop.png"); //bishop
    Image blackQueen = new Image("file:resources/blackQueen.png"); //queen
    Image whiteQueen = new Image("file:resources/whiteQueen.png"); //queen
    Image blackKing = new Image("file:resources/blackKing.png"); //king
    Image whiteKing = new Image("file:resources/whiteKing.png"); //king
    Image blackKnight = new Image("file:resources/blackKnight.png"); //knight
    Image whiteKnight = new Image("file:resources/whiteKnight.png"); //knight
    

    int score = 40; //score goes down with each loss of a piece
    Group group = new Group();
    ArcadeApp app;
    Scene scene;
    Text score1Text;
    Text score2Text;
    
    /**
     * The constructor of the chess game.
     *
     * @param app The parent {@code ArcadeApp} used for scene-change
     */
    public GameTwo(ArcadeApp app) {
	this.app = app;
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
		    //stop timeline
		}
	    });
	group.getChildren().addAll(board, quit, score1Text, score2Text);
	
    } //gameTwo

    /**
     * Getter for the scene
     *
     * @return the scene
     */
    public Scene getScene() {
	return scene;
    } //getter
}
