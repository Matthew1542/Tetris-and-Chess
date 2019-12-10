package cs1302.arcade;

import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

/**
 * The class that creates a block.
 * The blocks are composed of 4 rectangles.
 *
 */ 
public class Block {

    Rectangle r1; //1st square
    Rectangle r2; //2nd
    Rectangle r3; //3rd
    Rectangle r4; //4th

    String type; //type of block
    int rotation = 1; //keeping track of how the block looks
    ImagePattern png;

    /**
     * Constructor for no param arguments.
     *
     */
    public Block() {
        type = "cyan";
    } //constructor

    /**
     * Constructor to make a new block.
     *
     * @param r1 the left most square
     * @param r2 the second square
     * @param r3 the third square 
     * @param r4 the final square
     * @param type the color of the boxes
     */
    public Block(Rectangle r1, Rectangle r2, Rectangle r3, Rectangle r4, String type) {
        this.type = type;

        this.r1 = r1; //set blocks to instances v's
        this.r2 = r2;
        this.r3 = r3;
        this.r4 = r4;
        setImage(type); //call setter

    } //constructor

    /**
     * Method to create the color of the block based on the type.
     *
     * @param type the color of the block (red, blue, yellow, etc.)
     */
    public void setImage(String type) {
        if (type.equals("red")) {
            this.png = new ImagePattern(new Image("file:resources/redBlock.png"));
        } else if (type.equals("purple")) {
            this.png = new ImagePattern(new Image("file:resources/purpleBlock.png"));
        } else if (type.equals("green")) {
            this.png = new ImagePattern(new Image("file:resources/greenBlock.png"));
        } else if (type.equals("yellow")) {
            this.png = new ImagePattern(new Image("file:resources/yellowBlock.png"));
        } else if (type.equals("orange")) {
            this.png = new ImagePattern(new Image("file:resources/orangeBlock.png"));
        } else if (type.equals("blue")) {
            this.png = new ImagePattern(new Image("file:resources/blueBlock.png"));
        } else if (type.equals("cyan")) {
            this.png = new ImagePattern(new Image("file:resources/cyanBlock.png"));
        } else {
            System.out.println("Error: no matching color!");
        } //else

        r1.setFill(this.png); //set the color to the block
        r2.setFill(this.png);
        r3.setFill(this.png);
        r4.setFill(this.png);


    } //setter


} //block
