package simplicity.Model.Listeners;

public interface MenuEventListener {

    /**
     * Handles resolution change (windowed)
     *
     * @param width  window width
     * @param height window height
     */
    void changedWindowed(int width, int height);

    /**
     * Handles resolution change (fullscreen)
     */
    void changedFullscreen();

}
