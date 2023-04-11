package simplicity.Model.Game;

import lombok.Getter;

import java.awt.*;

public class FieldData {

    @Getter private FieldType type;
    private Point position;

    public FieldData(Point position, Image image){
        this.position = position;
        this.type = FieldType.EMPTY;
    }

    @Override
    public String toString(){
        return "(" + position.x + "," + position.y + ")[" + this.type + "]";
    }

}
