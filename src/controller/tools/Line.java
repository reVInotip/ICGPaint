package controller.tools;

import controller.ToolView;
import event.events.DrawLineEvent;
import event.events.DrawTempLineEvent;

import java.awt.*;
import java.awt.event.MouseEvent;

@ToolView(name = "Line", descr = "Линия (между двумя последними кликами)", icon = "/tools/line.png")
public class Line extends Tool {
    Point start = new Point(0, 0);
    int countClicks = 0;

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
        ++countClicks;
        if (countClicks == 2) {
            update(new DrawLineEvent(start.x, start.y, e.getX(), e.getY()));
            countClicks = 0;
        } else {
            start.x = e.getX();
            start.y = e.getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (countClicks == 1) {
            update(new DrawTempLineEvent(start.x, start.y, e.getX(), e.getY()));
        }
    }
}
