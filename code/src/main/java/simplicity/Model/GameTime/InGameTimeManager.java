package simplicity.Model.GameTime;

import lombok.NoArgsConstructor;

import java.io.Serializable;

public class InGameTimeManager implements Serializable {

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
