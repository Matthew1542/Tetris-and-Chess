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
    ArcadeApp app;
    Scene scene;
    Text score1Text;
    Text score2Text;
    int scoreOne = 100; //score goes down with each loss of a piece
    int scoreTwo = 100; //score goes down with each loss of a piece

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
        try {
            if (grid[xX + 1][yY - 1] == 2) {
                int[] temp = {(xX + 1), (yY - 1)};
                possible.add(temp);
            }
            if (grid[xX - 1][yY - 1] == 2) {
                int[] temp = {xX - 1, yY - 1};
                possible.add(temp);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.print("");
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
        try {
            if (grid[xX + 1][yY + 1] == 1) {
                int[] temp = {xX + 1, yY + 1};
                possible.add(temp);
            }
            if (grid[xX - 1][yY + 1] == 1) {
                int[] temp = {xX - 1, yY + 1};
                possible.add(temp);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.print("");
        }
        showGreen();
    }

    /**
     * Determines the availiable spot for the rook.
     *
     * @param p The piece that is being moved
     */
    public void showRook(Piece p) {

    }

    /**
     * Determines the availiable spot for the bishop.
     *
     * @param p The piece that is being moved
     */
    public void showBishop(Piece p) {

    }

    /**
     * Determines the availiable spot for the bishop.
     *
     * @param p The piece that is being moved  
     */
    public void showHorse(Piece p) {

    }

    /**
     * Determines the availiable spot for the queen.
     *
     * @param p The piece that is being moved  
     */
    public void showQueen(Piece p) {

    }

    /**
     * Determines the availiable spot for the king.
     *
     * @param p The piece that is being moved  
     */
    public void showKing(Piece p) {

    }

    /**
     * This method keeps track of the scoring.
     *
     * @param p The piece to be scored
     */
    public void doScoring(Piece p) {
        if (p.type.contains("w")) {
            if (p.type.contains("P")) {
                scoreTwo += 1;
                scoreOne -= 1;
            } else if (p.type.contains("B")) {
                scoreTwo += 3;
                scoreOne -= 3;
            } else if (p.type.contains("H")) {
                scoreTwo += 3;
                scoreOne -= 3;
            } else if (p.type.contains("R")) {
                scoreTwo += 5;
                scoreOne -= 5;
            } else if (p.type.contains("Q")) {
                scoreTwo += 10;
                scoreOne -= 10;
            }
        } else {
            if (p.type.contains("P")) {
                scoreTwo -= 1;
                scoreOne += 1;
            } else if (p.type.contains("B")) {
                scoreTwo -= 3;
                scoreOne += 3;
            } else if (p.type.contains("H")) {
                scoreTwo -= 3;
                scoreOne += 3;
            } else if (p.type.contains("R")) {
                scoreTwo -= 5;
                scoreOne += 5;
            } else if (p.type.contains("Q")) {
                scoreTwo -= 10;
                scoreOne += 10;
            }
        }
        score1Text.setText("" + scoreOne);
        score2Text.setText("" + scoreTwo);
    }

}
