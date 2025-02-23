package event.events;

import java.awt.*;
import java.util.ArrayList;

public class DrawLinesEvent implements Event {
    public ArrayList<Point> points;

    public DrawLinesEvent(ArrayList<Point> points) {
        this.points = points;
    }
}
