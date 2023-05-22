package simplicity.Model.Placeables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import simplicity.Model.Game.FieldType;
import simplicity.Model.Game.RoadType;
import simplicity.Model.GameModel;
import simplicity.Model.Resource.ResourceLoader;

import java.awt.*;
import java.io.Serializable;

@NoArgsConstructor
public class Road extends Placeable implements Serializable {

    @Getter
    private int maintenanceCost;

    public Road(Point position) {
        super(FieldType.ROAD, position, 600);
        this.maintenanceCost = 100;
    }

    @Override
    public int calculateTax() {
        return maintenanceCost;
    }

    @Override
    public int calculateMaintenance() {
        return 0;
    }

    @Override
    public Image getImage(Placeable left, Placeable right, Placeable up, Placeable down) {
        Image roadImg;
        if (this.getPosition() == GameModel.NO_SELECTION) {
            roadImg = GameModel.ROAD_STRAIGHT_IMG;
        } else {
            RoadType type = RoadType.calc(left, right, up, down);
            switch (type) {
                case HORIZONTAL -> roadImg = GameModel.ROAD_STRAIGHT_IMG;
                case VERTICAL -> roadImg = ResourceLoader.rotateImage(GameModel.ROAD_STRAIGHT_IMG, 90);
                case DOWN_TO_LEFT -> roadImg = GameModel.ROAD_TURN_IMG;
                case DOWN_TO_RIGHT -> roadImg = ResourceLoader.rotateImage(GameModel.ROAD_TURN_IMG, -90);
                case UP_TO_RIGHT -> roadImg = ResourceLoader.rotateImage(GameModel.ROAD_TURN_IMG, 180);
                case UP_TO_LEFT -> roadImg = ResourceLoader.rotateImage(GameModel.ROAD_TURN_IMG, 90);
                case DOWN_T -> roadImg = GameModel.ROAD_T;
                case RIGHT_T -> roadImg = ResourceLoader.rotateImage(GameModel.ROAD_T, -90);
                case UP_T -> roadImg = ResourceLoader.rotateImage(GameModel.ROAD_T, 180);
                case LEFT_T -> roadImg = ResourceLoader.rotateImage(GameModel.ROAD_T, 90);
                case ALL -> roadImg = GameModel.ROAD_ALL;
                default -> roadImg = GameModel.MISSING_IMG;
            }
        }
        return roadImg;
    }
}
