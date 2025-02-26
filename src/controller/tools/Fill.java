package controller.tools;

import controller.ToolView;
import event.events.FillEvent;

import java.awt.event.MouseEvent;

@ToolView(name = "Fill", descr = "Заливка области", icon = "/tools/fill.png")
public class Fill extends Tool {
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
        update(new FillEvent(e.getX(), e.getY()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
