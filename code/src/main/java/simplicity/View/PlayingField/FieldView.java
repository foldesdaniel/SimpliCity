package simplicity.View.PlayingField;

import lombok.Getter;
import simplicity.Model.GameModel;

import java.awt.*;

public class FieldView {

    private final Image image;
    @Getter private String type;
    private Point position;

    public FieldView(Point position, Image image){
        this.position = position;
        this.image = image;
        this.type = "empty";
    }

    /*public Image getImage(){
        return image;
    }*/

    public void toggleTeszt(){
        this.type = this.type.equals("empty") ? "road" : "empty";
    }

    @Override
    public String toString(){
        return "(" + position.x + "," + position.y + ")[" + this.type + "]";
    }

}
