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
import javafx.scene.paint.ImagePattern;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * This class is the main class for a javaFX game of chess.
 *
 */
public class GameTwo {

    /** double array containing every possible spot for each piece. */
    public final int[][] grid = new int[8][8]; //8x8 game board

    /** Image to be used as the game board. */
    ImageView board = new ImageView(new Image("file:resources/chessBoard.png"));
    Image posRoute = new Image("file:resources/pos.png"); //highlights routes


    Group group = new Group();
    KeyFrame keyFrame;
    Timeline timeline;
    ArcadeApp app;
    Scene scene;
    Text score1Text;
    Text score2Text;
    int scoreOne = 0; //score goes down with each loss of a piece
    int scoreTwo = 0; //score goes down with each loss of a piece
    int timer = 0;

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
                    grid[x][i] = 2; //1 = white piece
                } else if (i == 6 || i == 7) {
                    grid[x][i] = 1; //2 = black piece
                } else {
                    //if you are here, the spot is empty
                    grid[x][i] = 0; //empty spot
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

        score1Text = new Text("" + scoreOne);
        score1Text.setX(1105);
        score1Text.setY(175);
        score1Text.setFont(Font.font("Verdana", FontWeight.BOLD, 40));

        score2Text = new Text("" + scoreTwo);
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
        makeTimeline(1000);
    } //gameTwo

    /**
     * Getter for the scene.
     *
     * @return the scene
     */
    public Scene getScene() {
        return scene;
    } //getter

    ArrayList<int[]> possible = new ArrayList<int[]>();
    int placeHolder = 0;
    ArrayList<Rectangle> pos = new ArrayList<Rectangle>();

    /**
     * Determines the location of the mouse click, and results in
     * the moving of a chess piece.
     *
     * @return EventHandler the event handler to be returned.
     */
    private EventHandler<? super MouseEvent> createMouseHandler() {
        return event -> {
            int x = (int)Math.floor(((int)event.getX() - 280) / 90);
            int y = (int)Math.floor((int)event.getY() / 90);
            for (int v = 0; v < pos.size(); v++) {
                group.getChildren().remove(pos.get(v));
            }
            pos.clear();
            if (possible.size() > 0) {
                for (int i = 0; i < possible.size(); i++) {
                    if (possible.get(i)[0] == x && possible.get(i)[1] == y) {
                        for (int n = 0; n < pieces.size(); n++) {
                            Piece p = pieces.get(n);
                            int xX = (int)Math.floor((p.getX() - 280) / 90);
                            int yY = (int)Math.floor(p.getY() / 90);
                            if (xX == x && yY == y) {
                                doScoring(p);
                                group.getChildren().remove(pieces.get(n));
                                grid[xX][yY] = 0;
                                pieces.get(n).setX(-100);
                            }
                        }
                        Piece p = pieces.get(placeHolder);
                        int xX = (int)Math.floor((p.getX() - 280) / 90);
                        int yY = (int)Math.floor(p.getY() / 90);
                        grid[xX][yY] = 0;
                        pieces.get(placeHolder).setX(x * 90 + 280);
                        pieces.get(placeHolder).setY(y * 90);
                        xX = (int)Math.floor((p.getX() - 280) / 90);
                        yY = (int)Math.floor(p.getY() / 90);
                        if (p.type.contains("w")) {
                            grid[xX][yY] = 1;
                        } else {
                            grid[xX][yY] = 2;
                        }
                        pieces.get(placeHolder).moves++;
                        pieceMoved();
                        checkPawn(pieces.get(placeHolder));
                    }
                } //for
            }
            checkPieces(x, y);
        };
    } // createMouseHandler

    
    /**
     * This method checks the piece that the user clicked.
     *
     * @param x the spot of the x coordianate
     * @param y the spot on the y-axis
     */
    public void checkPieces(int x, int y) {
        for (int i = 0; i < pieces.size(); i++) {
            if ((int)Math.floor((pieces.get(i).getX() - 280) / 90) == x &&
                (int)Math.floor(pieces.get(i).getY() / 90) == y) {
                placeHolder = i;
                String type = pieces.get(i).type;
                possible = new ArrayList<int[]>();
                if (type.contains("w") && turn) {
                    if (type.equals("wP")) {
                        showWhitePawn(pieces.get(i));
                    } else if (type.contains("R")) {
                        showRook(pieces.get(i));
                    } else if (type.contains("H")) {
                        showHorse(pieces.get(i));
                    } else if (type.contains("B")) {
                        showBishop(pieces.get(i));
                    } else if (type.contains("K")) {
                        showKing(pieces.get(i));
                    } else if (type.contains("Q")) {
                        showQueen(pieces.get(i));
                    }
                } else if (type.contains("b") && !turn) {
                    if (type.equals("bP")) {
                        showBlackPawn(pieces.get(i));
                    } else if (type.contains("R")) {
                        showRook(pieces.get(i));
                    } else if (type.contains("H")) {
                        showHorse(pieces.get(i));
                    } else if (type.contains("B")) {
                        showBishop(pieces.get(i));
                    } else if (type.contains("K")) {
                        showKing(pieces.get(i));
                    } else if (type.contains("Q")) {
                        showQueen(pieces.get(i));
                    }
                }
            }
        }
    }

    /**
     * The method that determines who's turn it is.
     *
     */
    public void pieceMoved() {
        if (turn) {
            turn = false;
        } else {
            turn = true;
        }
    }
    
    /**
     * Creates a timeline to run the game loop with a variable delay.
     * @param milis The miliseconds between each loop execution.
     */
    private void makeTimeline(int milis) {
        EventHandler<ActionEvent> handler = event -> timer();
        keyFrame = new KeyFrame(Duration.millis(milis), handler);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        timeline.stop();
    }
    
    /**
     * Heleper method to keep track of time and exit to menu
     * at the desired time.
     *
     */    
    public void timer() {
        timer++;
        if (timer == 10) {
            app.gameState = "MENU";
            app.updateScene();
            timeline.stop();
        }
    }

    /**
     * Fills the availiable spots for a piece with a green outline.
     *
     */
    public void showGreen() {
        for (int i = 0; i < possible.size(); i++) {
            Rectangle r = new Rectangle(90, 90);
            r.setFill(new ImagePattern(posRoute));
            r.setX(possible.get(i)[0] * 90 + 280);
            r.setY(possible.get(i)[1] * 90);
            pos.add(r);
        }
        for (int i = 0; i < pos.size(); i++) {
            group.getChildren().add(pos.get(i));
        }
    }
    
    /**
     * Helper method to check if the spot at x or
     * y are taken by a piece.
     *
     * @param x the spot on the x axis
     * @param y the hight of the move
     * @return true if you can move to the spot 
     */    
    public boolean checkMove(int x, int y) {
        if (x > 7 || x < 0) {
            return false;
        }
        if (y > 7 || y < 0) {
            return false;
        }
        return true;
    }
    
    /**
     * Checks if the pawn is in the back of the board.
     * If so, the piece turns into a Queen.
     * 
     * @param p the piece to be checked 
     */    
    public void checkPawn(Piece p) {
        int xX = (int)Math.floor((p.getX() - 280) / 90);
        int yY = (int)Math.floor(p.getY() / 90);
        int color = 1;
        if (p.type.contains("b")) {
            color = 2;
        }
        if (p.type.contains("P")) {
            if (color == 1) {
                if (yY == 0) {
                    p.type = "wQ";
                    p.setImage("wQ");
                }
            } else {
                if (yY == 7) {
                    p.type = "bQ";
                    p.setImage("bQ");
                } 
            }
        }
    } 

    /**
     * Determines the availiable spot for the white pawn.
     *
     * @param p The piece to be moved
     */
    public void showWhitePawn(Piece p) {
        int xX = (int)Math.floor((p.getX() - 280) / 90);
        int yY = (int)Math.floor(p.getY() / 90);
        if (p.moves == 0) {
            if (grid[xX][yY - 2] == 0) {
                int[] temp = {xX, yY - 2};
                possible.add(temp);
            }
        }
        if (grid[xX][yY - 1] == 0) {
            int[] temp = {xX, yY - 1};
            possible.add(temp);
        }
        if (checkMove(xX + 1, yY - 1)) {
            if (grid[xX + 1][yY - 1] == 2) {
                int[] temp = {xX + 1, yY - 1};
                possible.add(temp);
            }
        }
        if (checkMove(xX - 1, yY - 1)) {
            if (grid[xX - 1][yY - 1] == 2) {
                int[] temp = {xX - 1, yY - 1};
                possible.add(temp);
            }
        }
        showGreen();
    }

    /**
     * Determines the availiable spot for the black pawn.
     *
     * @param p The piece to be moved
     */ 
    public void showBlackPawn(Piece p) {
        int xX = (int)Math.floor((p.getX() - 280) / 90);
        int yY = (int)Math.floor(p.getY() / 90);
        if (p.moves == 0) {
            if (grid[xX][yY + 2] == 0) {
                int[] temp = {xX, yY + 2};
                possible.add(temp);
            }
        }
        if (grid[xX][yY + 1] == 0) {
            int[] temp = {xX, yY + 1};
            possible.add(temp);
        }
        if (checkMove(xX + 1, yY + 1)) {
            if (grid[xX + 1][yY + 1] == 1) {
                int[] temp = {xX + 1, yY + 1};
                possible.add(temp);
            }
        }
        if (checkMove(xX - 1, yY + 1)) {
            if (grid[xX - 1][yY + 1] == 1) {
                int[] temp = {xX - 1, yY + 1};
                possible.add(temp);
            }
        }
        showGreen();
    }

    /**
     * Determines the availiable spot for the rook.
     *
     * @param p The piece that is being moved
     */
    public void showRook(Piece p) {
        int xX = (int)Math.floor((p.getX() - 280) / 90);
        int yY = (int)Math.floor(p.getY() / 90);
        int color = 1;
        if (p.type.contains("b")) {
            color = 2;
        }
        shortRook(xX, yY, color);
        showGreen(); //show the green     
    } //showRook
    
    /**
     * The helper method to assist with the movement of the rook.
     *
     * @param xX the location of getX() of the piece
     * @param yY the location of getY() of the piece
     * @param color the turn of the player 
     */    
    public void shortRook(int xX, int yY, int color) {
        for (int i = xX + 1; i < 8; i++) { 
            if (checkMove(i, yY)) { 
                if (grid[i][yY] == 0) { 
                    int[] temp = {i, yY};
                    possible.add(temp);  
                } else if (grid[i][yY] != color) {
                    int[] temp = {i, yY};
                    possible.add(temp);  
                    break;
                } else { 
                    break;
                } //else
            } //if  
        } //for        
        for (int i = xX - 1; i >= 0; i--) { 
            if (checkMove(i, yY)) { 
                if (grid[i][yY] == 0) { 
                    int[] temp = {i, yY};
                    possible.add(temp);  
                } else if (grid[i][yY] != color) {
                    int[] temp = {i, yY};
                    possible.add(temp);  
                    break;
                } else { 
                    break;
                } //else
            } //if  
        } //for        
        for (int i = yY + 1; i < 8; i++) { 
            if (checkMove(xX, i)) { 
                if (grid[xX][i] == 0) { 
                    int[] temp = {xX, i};
                    possible.add(temp);  
                } else if (grid[xX][i] != color) {
                    int[] temp = {xX, i};
                    possible.add(temp);  
                    break;
                } else { 
                    break;
                } //else
            } //if  
        } //for        
        for (int i = yY - 1; i >= 0; i--) { 
            if (checkMove(xX, i)) { 
                if (grid[xX][i] == 0) { 
                    int[] temp = {xX, i};
                    possible.add(temp);  
                } else if (grid[xX][i] != color) {
                    int[] temp = {xX, i};
                    possible.add(temp);  
                    break;
                } else { 
                    break;
                } //else
            } //if  
        } //for  
    }

    /**
     * Determines the availiable spot for the bishop.
     *
     * @param p The piece that is being moved
     */
    public void showBishop(Piece p) {
        int xX = (int)Math.floor((p.getX() - 280) / 90);
        int yY = (int)Math.floor(p.getY() / 90);
        int color = 1;
        if (p.type.contains("b")) {
            color = 2;
        }
        shortBishop(xX, yY, color);
        showGreen(); //show the green 
    }
    
    /**
     * Helper method to assist with the creation of the bishop.
     *
     * @param xX the location of getX() of the piece
     * @param yY the location of getY() of the piece
     * @param color the turn of the player
     */    
    public void shortBishop(int xX, int yY, int color) {
        for (int i = xX + 1; i < 8; i++) {
            if (checkMove(i, yY + i - xX)) { 
                if (grid[i][yY + i - xX] == 0) { 
                    int[] temp = {i, yY + i - xX};
                    possible.add(temp);  
                } else if (grid[i][yY + i - xX] != color) {
                    int[] temp = {i, yY + i - xX};
                    possible.add(temp);  
                    break;
                } else { 
                    break;
                } //else
            } //if  
        }
        for (int i = xX - 1; i >= 0; i--) {
            if (checkMove(i, yY + i - xX)) { 
                if (grid[i][yY + i - xX] == 0) { 
                    int[] temp = {i, yY + i - xX};
                    possible.add(temp);  
                } else if (grid[i][yY + i - xX] != color) {
                    int[] temp = {i, yY + i - xX};
                    possible.add(temp);  
                    break;
                } else { 
                    break;
                } //else
            } //if  
        }
        for (int i = xX + 1; i < 8; i++) {
            if (checkMove(i, yY - (i - xX))) { 
                if (grid[i][yY - (i - xX)] == 0) { 
                    int[] temp = {i, yY - (i - xX)};
                    possible.add(temp);  
                } else if (grid[i][yY - (i - xX)] != color) {
                    int[] temp = {i, yY - (i - xX)};
                    possible.add(temp);  
                    break;
                } else { 
                    break;
                } //else
            } //if  
        }
        for (int i = xX - 1; i >= 0; i--) {
            if (checkMove(i, yY - (i - xX))) { 
                if (grid[i][yY - (i - xX)] == 0) { 
                    int[] temp = {i, yY - (i - xX)};
                    possible.add(temp);  
                } else if (grid[i][yY - (i - xX)] != color) {
                    int[] temp = {i, yY - (i - xX)};
                    possible.add(temp);  
                    break;
                } else { 
                    break;
                } //else
            } //if  
        }
    }

    /**
     * Determines the availiable spot for the bishop.
     *
     * @param p The piece that is being moved  
     */
    public void showHorse(Piece p) {
        int xX = (int)Math.floor((p.getX() - 280) / 90);
        int yY = (int)Math.floor(p.getY() / 90);
        int color = 1;
        if (p.type.contains("b")) {
            color = 2;
        }
        if (checkMove(xX - 2, yY + 1)) {
            if (grid[xX - 2][yY + 1] !=  color) {
                int[] temp = {xX - 2, yY + 1};
                possible.add(temp);
            }
        }
        if (checkMove(xX - 2, yY - 1)) {
            if (grid[xX - 2][yY - 1] !=  color) {
                int[] temp = {xX - 2, yY - 1};
                possible.add(temp);
            }
        }
        if (checkMove(xX - 1, yY + 2)) {
            if (grid[xX - 1][yY + 2] !=  color) {
                int[] temp = {xX - 1, yY + 2};
                possible.add(temp);
            }
        }
        if (checkMove(xX - 1, yY - 2)) {
            if (grid[xX - 1][yY - 2] !=  color) {
                int[] temp = {xX - 1, yY - 2};
                possible.add(temp);
            }
        }
        if (checkMove(xX + 2, yY + 1)) {
            if (grid[xX + 2][yY + 1] !=  color) {
                int[] temp = {xX + 2, yY + 1};
                possible.add(temp);
            }
        }
        if (checkMove(xX + 2, yY - 1)) {
            if (grid[xX + 2][yY - 1] !=  color) {
                int[] temp = {xX + 2, yY - 1};
                possible.add(temp);
            }
        }
        if (checkMove(xX + 1, yY + 2)) {
            if (grid[xX + 1][yY + 2] !=  color) {
                int[] temp = {xX + 1, yY + 2};
                possible.add(temp);
            }
        }
        if (checkMove(xX + 1, yY - 2)) {
            if (grid[xX + 1][yY - 2] !=  color) {
                int[] temp = {xX + 1, yY - 2};
                possible.add(temp);
            }
        }
        showGreen();
    }

    /**
     * Determines the availiable spot for the queen.
     *
     * @param p The piece that is being moved  
     */
    public void showQueen(Piece p) {
        int xX = (int)Math.floor((p.getX() - 280) / 90);
        int yY = (int)Math.floor(p.getY() / 90);
        int color = 1;
        if (p.type.contains("b")) {
            color = 2;
        }
        shortRook(xX, yY, color);
        shortBishop(xX, yY, color);
        showGreen(); //show the green 
    }

    /**
     * Determines the availiable spot for the king.
     *
     * @param p The piece that is being moved  
     */
    public void showKing(Piece p) {
        int xX = (int)Math.floor((p.getX() - 280) / 90);
        int yY = (int)Math.floor(p.getY() / 90);
        int color = 1;
        if (p.type.contains("b")) {
            color = 2;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (checkMove(xX + i, yY + j)) {
                    if (grid[xX + i][yY + j] !=  color) {
                        int[] temp = {xX + i, yY + j};
                        possible.add(temp);
                    }
                } 
            }
        }
        showGreen();
    }

    /**
     * This method keeps track of the scoring.
     *
     * @param p The piece to be scored
     */
    public void doScoring(Piece p) {
        if (p.type.contains("b")) {
            if (p.type.contains("P")) {
                scoreTwo += 1;
            } else if (p.type.contains("B")) {
                scoreTwo += 3;
            } else if (p.type.contains("H")) {
                scoreTwo += 3;
            } else if (p.type.contains("R")) {
                scoreTwo += 5;
            } else if (p.type.contains("Q")) {
                scoreTwo += 10;
            } else if (p.type.contains("K")) {
                timeline.play();
                Text gameOver = new Text("GAME OVER");
                gameOver.setX(180);
                gameOver.setY(360);
                gameOver.setFill(Color.RED);
                gameOver.setFont(Font.font("Verdana", FontWeight.BOLD, 150));
                group.getChildren().add(gameOver);  
            }
        } else {
            if (p.type.contains("P")) {
                scoreOne += 1;
            } else if (p.type.contains("B")) {
                scoreOne += 3;
            } else if (p.type.contains("H")) {
                scoreOne += 3;
            } else if (p.type.contains("R")) {
                scoreOne += 5;
            } else if (p.type.contains("Q")) {
                scoreOne += 10;
            } else if (p.type.contains("K")) {
                timeline.play();
                Text gameOver = new Text("GAME OVER");
                gameOver.setX(180);
                gameOver.setY(360);
                gameOver.setFill(Color.RED);
                gameOver.setFont(Font.font("Verdana", FontWeight.BOLD, 150));
                group.getChildren().add(gameOver);
            }
        }
        score1Text.setText("" + scoreOne);
        score2Text.setText("" + scoreTwo);
    }

}
