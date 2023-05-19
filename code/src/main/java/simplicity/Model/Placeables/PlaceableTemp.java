package simplicity.Model.Placeables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameModel;

import java.awt.*;
import java.io.Serializable;

@NoArgsConstructor(force = true)
public class PlaceableTemp extends Placeable implements Serializable {

    @Getter
    private final Placeable placeable;

    public PlaceableTemp(Placeable placeable, Point p) {
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
    public Point getDisplayPosition() {
        return placeable.getPosition();
    }

    @Override
    public Dimension getDisplaySize() {
        return placeable.getSize();
    }

    @Override
    public boolean roadConnects() {
        int x = this.getPosition().x;
        int y = this.getPosition().y;
        GameModel model = GameModel.getInstance();
        boolean left = x > 0 && model.grid(x - 1, y) != null && model.grid(x - 1, y).getType() == FieldType.ROAD;
        boolean right = x < GameModel.GRID_SIZE - 1 && model.grid(x + 1, y) != null && model.grid(x + 1, y).getType() == FieldType.ROAD;
        boolean up = y > 0 && model.grid(x, y - 1) != null && model.grid(x, y - 1).getType() == FieldType.ROAD;
        boolean down = y < GameModel.GRID_SIZE - 1 && model.grid(x, y + 1) != null && model.grid(x, y + 1).getType() == FieldType.ROAD;
        return !placeable.roadConnects() && (left || right || up || down);
    }

}
