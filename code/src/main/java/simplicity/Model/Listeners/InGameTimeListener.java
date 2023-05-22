package simplicity.Model.Listeners;

public interface InGameTimeListener {

    /**
     * Handles when time changes
     *
     * @param inGameYear in game year
     * @param inGameDay  in game day
     * @param inGameHour in game hour
     */
    void timeChanged(int inGameYear, int inGameDay, int inGameHour);

}
