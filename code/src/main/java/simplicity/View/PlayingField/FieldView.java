package simplicity.View.PlayingField;

import java.awt.*;

public class FieldView {

    private boolean isHovering;
    private final Image image;
    private Point position;

    public FieldView(Point position, Image image){
        this.position = position;
        this.image = image;
    }

    public Image getImage(){
        return image;
    }

    @Override
    public String toString(){
        return "(" + position.x + "," + position.y + ")[" + "type" + "]";
    }

}
