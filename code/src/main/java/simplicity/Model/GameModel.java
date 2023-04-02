package simplicity.Model;

import lombok.Getter;
import lombok.Setter;
import simplicity.Model.Resource.ResourceLoader;

import java.awt.*;

public class GameModel {

    public static final String GAME_TITLE = "SimpliCity";

    public static final Image MISSING_IMG = ResourceLoader.loadImage("missing.png");
    public static final Image GRASS_IMG = ResourceLoader.loadImage("grass.png");
    public static final Image SELECTION_IMG = ResourceLoader.loadImage("selection.png");
    public static final Image SELECTION_2_IMG = ResourceLoader.loadImage("selection2.png");
    public static final Image ROAD_STRAIGHT_IMG = ResourceLoader.loadImage("road.png");
    public static final Image ROAD_TURN_IMG = ResourceLoader.loadImage("road_turn.png");
    public static final Image ROAD_T = ResourceLoader.loadImage("road_t.png");
    public static final Image ROAD_ALL = ResourceLoader.loadImage("road_all.png");
    public static final Font CUSTOM_FONT = ResourceLoader.loadFont("vt323.ttf");
    public static final Color BG_DARK = new Color(61, 63, 65); // default flatlaf dark

}
