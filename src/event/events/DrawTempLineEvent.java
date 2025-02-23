package event.events;

import java.awt.*;

public class DrawTempLineEvent implements Event {
    public Point start;
    public Point end;

    public DrawTempLineEvent(int x1, int y1, int x2, int y2) {
        start = new Point(x1, y1);
        end = new Point(x2, y2);
    }
}
