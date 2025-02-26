package event.events;

import java.awt.*;

public class FillEvent implements Event {
    public Point seed;

    public FillEvent(int x, int y) {
        seed = new Point(x, y);
    }
}
