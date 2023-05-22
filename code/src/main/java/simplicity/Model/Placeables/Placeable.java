package simplicity.Model.Placeables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameModel;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor(force = true)
public abstract class Placeable implements Serializable {

    private final FieldType type;
    private final int buildPrice;
    private Point position;

    public Placeable(FieldType type, Point position, int buildPrice) {
        this.type = type;
        this.position = position;
        this.buildPrice = buildPrice;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getDisplayPosition() {
        return this.getPosition();
    }

    public abstract int calculateTax();

    public abstract int calculateMaintenance();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Placeable placeable)) return false;
        return buildPrice == placeable.buildPrice && type == placeable.type && Objects.equals(position, placeable.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, position, buildPrice);
    }

    public abstract Image getImage(Placeable left, Placeable right, Placeable up, Placeable down);

    public Image getImage() {
        return this.getImage(null, null, null, null);
    }

    public String getDisplayName() {
        return this.type == null ? "Null" : this.type.getDisplayName();
    }

    @Override
    public String toString() {
        return (this.type == null ? "NULL" : this.type.name()) + "(" + this.position.x + "," + this.position.y + ")";
    }

    public Dimension getSize() {
        return new Dimension(1, 1);
    }

    public Dimension getDisplaySize() {
        return this.getSize();
    }


    /**
     * Checks if there is at least 1 road near the object (purely for visual purposes)
     *
     * @return true if there is a road near the object, false otherwise
     */
    public boolean roadConnects() {
        int x = this.position.x;
        int y = this.position.y;
        GameModel model = GameModel.getInstance();
        boolean left = x > 0 && model.grid(x - 1, y) != null && model.grid(x - 1, y).type == FieldType.ROAD;
        boolean right = x < GameModel.GRID_SIZE - 1 && model.grid(x + 1, y) != null && model.grid(x + 1, y).type == FieldType.ROAD;
        boolean up = y > 0 && model.grid(x, y - 1) != null && model.grid(x, y - 1).type == FieldType.ROAD;
        boolean down = y < GameModel.GRID_SIZE - 1 && model.grid(x, y + 1) != null && model.grid(x, y + 1).type == FieldType.ROAD;
        return left || right || up || down;
    }

}
