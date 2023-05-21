package simplicity.Model.GameTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum InGameSpeeds {
    NORMAL(1000),
    FAST(500),
    FASTEST(333),

    ULTRASONIC_DEV_ONLY(5),

    ULTRASUPERSONIC_DEV_ONLY(1);

    @Getter
    private final int speed;
}
