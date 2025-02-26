package event.events;

import java.awt.*;
import java.util.ArrayList;

public class DrawTempLinesEvent implements Event {

    public ArrayList<Point> points;

    public DrawTempLinesEvent(ArrayList<Point> points) {
        this.points = points;
    }
}
