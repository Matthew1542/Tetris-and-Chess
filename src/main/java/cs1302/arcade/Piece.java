package cs1302.arcade;

import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;


/**
 * The pieces that are being used in chess.
 *
 */
public class Piece extends Rectangle{

    String type; //the type of piece to be used
    ImagePattern png; //the image of the piece
    Rectangle r;
    int moves = 0;

    public Piece(int x, int y, String type) {
        super(90, 90);
        this.setX(x);
        this.setY(y);
        this.r = r;
        this.type = type;
        setImage(type);
    } //constructor

    public void setImage(String type) {
        if (type.equals("wP")) {
            this.png = new ImagePattern(new Image("file:resources/whitePawn.png"));
        } //if
        if (type.equals("bP")) {
            this.png = new ImagePattern(new Image("file:resources/blackPawn.png"));
        } //if
        if (type.equals("wR")) {
            this.png = new ImagePattern(new Image("file:resources/whiteRook.png"));
        } //if
        if (type.equals("bR")) {
            this.png = new ImagePattern(new Image("file:resources/blackRook.png"));
        } //if
        if (type.equals("wK")) {
            this.png = new ImagePattern(new Image("file:resources/whiteKing.png"));
        } //if
        if (type.equals("bK")) {
            this.png = new ImagePattern(new Image("file:resources/blackKing.png"));
        } //if
        if (type.equals("wH")) {
            this.png = new ImagePattern(new Image("file:resources/whiteKnight.png"));
        } //if
        if (type.equals("bH")) {
            this.png = new ImagePattern(new Image("file:resources/blackKnight.png"));
        } //if
        if (type.equals("wQ")) {
            this.png = new ImagePattern(new Image("file:resources/whiteQueen.png"));
        } //if
        if (type.equals("bQ")) {
            this.png = new ImagePattern(new Image("file:resources/blackQueen.png"));
        } //if
        if (type.equals("wB")) {
            this.png = new ImagePattern(new Image("file:resources/whiteBishop.png"));
        } //if
        if (type.equals("bB")) {
            this.png = new ImagePattern(new Image("file:resources/blackBishop.png"));
        } //if
        this.setFill(this.png);
    } //setter





}
