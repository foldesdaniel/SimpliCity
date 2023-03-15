package Model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum InGameSpeeds {
    NORMAL(1000),
    FAST(500),
    FASTEST(333);

    @Getter
    private int speed;
}
