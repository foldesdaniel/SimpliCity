package simplicity.Model.Placeables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import simplicity.Model.Placeables.Placeable;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor(force = true)
public class PlaceableTemp extends Placeable implements Serializable {

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
    public int calculateMaintenance() {
        return placeable.calculateMaintenance();
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
    public Point getDisplayPosition(){
        return placeable.getPosition();
    }

    @Override
    public Dimension getDisplaySize(){
        return placeable.getSize();
    }

}
