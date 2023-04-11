package simplicity.Model.Placeables;

import lombok.Getter;
import simplicity.Model.Game.FieldType;

import java.awt.*;
import java.util.Objects;

@Getter
public abstract class Placeable {

    private final FieldType type;
    private final Point position;
    private final int buildPrice;

    public Placeable(FieldType type, Point position, int buildPrice) {
        this.type = type;
        this.position = position;
        this.buildPrice = buildPrice;
    }

    public abstract int calculateTax();

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

    @Override
    public String toString() {
        return this.type.name() + "(" + this.position.x + "," + this.position.y + ")";
    }
}
