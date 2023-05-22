package simplicity.Model.Listeners;

public interface StartStopGameListener {

    /**
     * Handles starting the game
     *
     * @param newGame if the map should be generated or loaded in
     */
    void onGameStart(boolean newGame);

    /**
     * Handles quitting the game
     */
    void onGameStop();

}
