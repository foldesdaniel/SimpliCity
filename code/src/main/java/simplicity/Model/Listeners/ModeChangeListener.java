package simplicity.Model.Listeners;

public interface ModeChangeListener {

    /**
     * Handles entering/exiting build mode
     *
     * @param on enter/exit
     */
    void onBuildModeChanged(boolean on);

    /**
     * Handles entering/exiting delete mode
     *
     * @param on enter/exit
     */
    void onDeleteModeChanged(boolean on);

}
