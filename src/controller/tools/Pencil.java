package controller.tools;

import controller.ToolView;
import event.events.DrawLineEvent;

import java.awt.event.MouseEvent;

@ToolView(name = "Pencil")
public class Pencil extends Tool {
    private int x = 0, y = 0;
    private boolean drawing = false;

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        drawing = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawing = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (drawing) {
            update(new DrawLineEvent(x, y, e.getX(), e.getY()));

            x = e.getX();
            y = e.getY();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
