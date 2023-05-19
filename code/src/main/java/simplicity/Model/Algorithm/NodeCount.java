package simplicity.Model.Algorithm;

import lombok.NoArgsConstructor;

import java.awt.*;
import java.io.Serializable;

@NoArgsConstructor
public class NodeCount implements Serializable {
    public int count;
    public Point position;

    /**
     * @param position current position
     * @param count    counted steps since start
     */
    public NodeCount(Point position, int count) {
        this.position = position;
        this.count = count;
    }
}
