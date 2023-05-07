package simplicity.Model.Placeables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import simplicity.Model.Game.FieldType;

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
        return this.type.getDisplayName();
    }

    @Override
    public String toString() {
        return this.type.name() + "(" + this.position.x + "," + this.position.y + ")";
    }

    public Dimension getSize() {
        return new Dimension(1, 1);
    }

    public Dimension getDisplaySize() {
        return this.getSize();
    }

}
