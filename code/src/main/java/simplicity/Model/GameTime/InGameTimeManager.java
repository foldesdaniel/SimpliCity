package simplicity.Model.GameTime;

public class InGameTimeManager {

    private static InGameTimeManager instance = null;
    private final InGameTime inGameTime;

    private InGameTimeManager() {
        inGameTime = new InGameTime();
    }

    public static InGameTimeManager getInstance() {
        if (instance == null) {
            instance = new InGameTimeManager();
        }
        return instance;
    }

    public InGameTime getInGameTime() {
        return inGameTime;
    }

}
