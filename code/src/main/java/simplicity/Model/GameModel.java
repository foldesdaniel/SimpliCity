package simplicity.Model;

import lombok.Getter;
import lombok.Setter;
import simplicity.Model.Resource.ResourceLoader;

import java.awt.*;

public class GameModel {

    public static final Image MISSING_IMG = ResourceLoader.loadImage("missing.png");
    public static final Image GRASS_IMG = ResourceLoader.loadImage("grass.png");
    public static final Image SELECTION_IMG = ResourceLoader.loadImage("selection.png");
    public static final Image ROAD_IMG = ResourceLoader.loadImage("road.png");

}
