package simplicity.Model.Placeables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import simplicity.Model.Game.FieldType;
import simplicity.Model.Game.ForestType;
import simplicity.Model.GameModel;
import simplicity.Model.GameTime.Date;

import java.awt.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor(force = true)
public class Forest extends Placeable implements Serializable {
    private final Date plantTime;

    @Setter
    private int maintenanceCost;

    @Setter
    @Getter
    private int age;

    public Forest(Point position, Date plantTime) {
        super(FieldType.FOREST, position, 5000);
        this.plantTime = plantTime;
        this.maintenanceCost = 500;
        this.age = 0;
    }

    public Forest(Point position) {
        this(position, new Date(0, 0, 0)); // need default plant time
    }

    @Override
    public int calculateTax() {
        return 0;
    }

    @Override
    public Image getImage(Placeable left, Placeable right, Placeable up, Placeable down) {
        Image forestImg;
        if (this.getPosition() == GameModel.NO_SELECTION) {
            forestImg = GameModel.FOREST_NONE;
        } else {
            ForestType type = ForestType.calc(left, right, up, down);
            switch (type) {
                case DOWN_TO_LEFT -> forestImg = GameModel.FOREST_DOWN_TO_LEFT;
                case DOWN_TO_RIGHT -> forestImg = GameModel.FOREST_DOWN_TO_RIGHT;
                case UP_TO_RIGHT -> forestImg = GameModel.FOREST_UP_TO_RIGHT;
                case UP_TO_LEFT -> forestImg = GameModel.FOREST_UP_TO_LEFT;
                case DOWN_T -> forestImg = GameModel.FOREST_DOWN_T;
                case RIGHT_T -> forestImg = GameModel.FOREST_RIGHT_T;
                case UP_T -> forestImg = GameModel.FOREST_UP_T;
                case LEFT_T -> forestImg = GameModel.FOREST_LEFT_T;
                case HORIZONTAL -> forestImg = GameModel.FOREST_HORIZONTAL;
                case VERTICAL -> forestImg = GameModel.FOREST_VERTICAL;
                case DOWN -> forestImg = GameModel.FOREST_DOWN;
                case RIGHT -> forestImg = GameModel.FOREST_RIGHT;
                case UP -> forestImg = GameModel.FOREST_UP;
                case LEFT -> forestImg = GameModel.FOREST_LEFT;
                case ALL -> forestImg = GameModel.FOREST_ALL;
                case NONE -> forestImg = GameModel.FOREST_NONE;
                default -> forestImg = GameModel.MISSING_IMG;
            }
        }
        return forestImg;
    }

    @Override
    public int calculateMaintenance() {
        return 0;
    }

    @Override
    public boolean roadConnects() {
        return false;
    }

}
