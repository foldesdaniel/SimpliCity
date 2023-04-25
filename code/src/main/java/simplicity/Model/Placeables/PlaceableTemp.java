package simplicity.Model.Placeables;

import lombok.Getter;
import simplicity.Model.Placeables.Placeable;

import java.awt.*;
import java.util.Objects;

public class PlaceableTemp extends Placeable {

    @Getter
    private final Placeable placeable;

    public PlaceableTemp(Placeable placeable, Point p){
        super(placeable.getType(), p, placeable.getBuildPrice());
        this.placeable = placeable;
    }

    @Override
    public int calculateTax() {
        return placeable.calculateTax();
    }

    @Override
    public Image getImage(Placeable left, Placeable right, Placeable up, Placeable down) {
        return null;
    }

    @Override
    public int hashCode() {
        return placeable.hashCode();
    }

    @Override
    public Dimension getDisplaySize(){
        return placeable.getSize();
    }

}
