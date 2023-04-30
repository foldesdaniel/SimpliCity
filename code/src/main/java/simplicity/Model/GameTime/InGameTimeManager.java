package simplicity.Model.GameTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

public class InGameTimeManager implements Serializable {

    private static InGameTimeManager instance = null;
    @Getter
    @Setter
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

//    @Serial
//    private Object readResolve() {
//        return (InGameTimeManager) getInstance();
//    }
}
