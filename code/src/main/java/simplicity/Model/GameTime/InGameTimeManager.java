package simplicity.Model.GameTime;

public class InGameTimeManager {

    private static InGameTimeManager instance = null;
    private InGameTime inGameTime;

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

    public void setInGameTime(int inGameYear, int inGameDay, int inGameHour) {
        inGameTime.setInGameTime(inGameYear, inGameDay, inGameHour);
    }

    public void startInGameTime(InGameSpeeds speed) {
        inGameTime.startInGameTime(speed);
    }

    public void stopInGameTime() {
        inGameTime.stopInGameTime();
    }

}
