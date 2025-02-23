package controller.tools;

import controller.ToolView;
import event.events.DrawLineEvent;
import event.events.DrawLinesEvent;
import model.Settings;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Set;

@ToolView(name = "Stamp")
public class Stamp extends Tool {
    private final ArrayList<Point> pointList = new ArrayList<>();

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < Settings.vertices; ++i) {
            double angle = ((2*Math.PI*i)/Settings.vertices) + Math.toRadians(Settings.angle);
            double x = Settings.radius * Math.cos(angle) + e.getX();
            double y = Settings.radius * Math.sin(angle) + e.getY();

            pointList.add(new Point((int) Math.round(x), (int) Math.round(y)));
        }

        update(new DrawLinesEvent(pointList));

        pointList.clear();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
