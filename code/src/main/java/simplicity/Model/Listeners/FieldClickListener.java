package simplicity.Model.Listeners;

import simplicity.Model.Placeables.Placeable;

public interface FieldClickListener {

    /**
     * Handles when a Placeable on the grid is clicked
     *
     * @param f clicked Placeable
     */
    void fieldClicked(Placeable f);

}
