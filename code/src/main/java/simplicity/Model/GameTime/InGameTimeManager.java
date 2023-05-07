package simplicity.Model.GameTime;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class InGameTimeManager implements Serializable {

    private static InGameTimeManager instance = null;
    @Getter
    @Setter
    private InGameTime inGameTime;

    private InGameTimeManager() {
        inGameTime = new InGameTime();
    }

    /**
     * creates or returns an instance
     *
     * @return instance of itself
     */
    public static InGameTimeManager getInstance() {
        if (instance == null) {
            instance = new InGameTimeManager();
        }
        return instance;
    }

}
