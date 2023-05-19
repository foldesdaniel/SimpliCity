package simplicity.Model.Game;

import simplicity.Model.Placeables.Placeable;

public enum ForestType {

    HORIZONTAL,
    VERTICAL,
    DOWN_TO_RIGHT,
    DOWN_TO_LEFT,
    UP_TO_RIGHT,
    UP_TO_LEFT,
    DOWN_T,
    UP_T,
    LEFT_T,
    RIGHT_T,
    LEFT,
    RIGHT,
    DOWN,
    UP,
    ALL,
    NONE,
    UNKNOWN;

    public static ForestType calc(Placeable _left, Placeable _right, Placeable _up, Placeable _down) {
        boolean left = _left != null && _left.getType() == FieldType.FOREST;
        boolean right = _right != null && _right.getType() == FieldType.FOREST;
        boolean down = _down != null && _down.getType() == FieldType.FOREST;
        boolean up = _up != null && _up.getType() == FieldType.FOREST;

        if (left && right && down && up) return ALL;
        if (!left && !right && !down && !up) return NONE;

        if (left && right && down) return DOWN_T;
        if (left && right && up) return UP_T;
        if (left && down && up) return LEFT_T;
        if (right && down && up) return RIGHT_T;

        if (left && down) return DOWN_TO_LEFT;
        if (left && up) return UP_TO_LEFT;
        if (right && down) return DOWN_TO_RIGHT;
        if (right && up) return UP_TO_RIGHT;

        if (left && right) return HORIZONTAL;
        if (up && down) return VERTICAL;

        if (left) return LEFT;
        if (right) return RIGHT;
        if (down) return DOWN;
        if (up) return UP;

        return UNKNOWN;
    }

}
