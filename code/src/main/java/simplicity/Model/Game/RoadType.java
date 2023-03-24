package simplicity.Model.Game;

public enum RoadType {

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
    ALL,
    UNKNOWN;

    public static RoadType calc(FieldData _left, FieldData _right, FieldData _up, FieldData _down){
        boolean left = _left != null && _left.getType() == FieldType.ROAD;
        boolean right = _right != null && _right.getType() == FieldType.ROAD;
        boolean down = _down != null && _down.getType() == FieldType.ROAD;
        boolean up = _up != null && _up.getType() == FieldType.ROAD;

        if(left && right && down && up) return ALL;
        
        if(left && right && down) return DOWN_T;
        if(left && right && up) return UP_T;
        if(left && down && up) return LEFT_T;
        if(right && down && up) return RIGHT_T;

        if(left && down) return DOWN_TO_LEFT;
        if(left && up) return UP_TO_LEFT;
        if(right && down) return DOWN_TO_RIGHT;
        if(right && up) return UP_TO_RIGHT;

        if(up || down) return VERTICAL;

        return HORIZONTAL;
    }

}
