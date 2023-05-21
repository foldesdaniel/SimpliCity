package simplicity.Model.Listeners;

import java.awt.event.KeyEvent;

public interface GameKeyListener {

    /**
     * Handles when a key is pressed
     *
     * @param e the event to be processed
     */
    void onKeyPressed(KeyEvent e);

}
