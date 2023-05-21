package simplicity.Model.Game;

public enum FieldType {

    EMPTY("Empty"),
    ROAD("Road"),
    ZONE_RESIDENTIAL("Residential zone"),
    ZONE_SERVICE("Service zone"),
    ZONE_INDUSTRIAL("Industrial zone"),
    SCHOOL("High school"),
    UNIVERSITY("University"),
    STADIUM("Stadium"),
    POLICE("Police station"),
    FOREST("Forest");

    private final String displayName;

    FieldType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
