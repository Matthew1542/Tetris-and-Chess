package cs1302.arcade;

import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

String type; //the type of piece to be used
ImagePattern png; //the image of the piece 
    
/**
 * The pieces that are being used in chess.
 *
 */
public class Piece extends Rectangle {

    public Rectangle(int x, int y, String type) {
	this.setX(x);
	this.setY(y);
	this.type = type;
	
    } //constructor

    public void setImage(String type) {
	if (type.equals("WP")) {
	    this.png = new ImagePattern(new Image("file:resources/
	}
    } //setter
    

    


}
