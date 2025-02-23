package controller.tools;

import event.Observable;

import java.awt.event.MouseEvent;

public abstract class Tool extends Observable  {
    public abstract void mousePressed(MouseEvent e);
    public abstract void mouseReleased(MouseEvent e);
    public abstract void mouseDragged(MouseEvent e);
    public abstract void mouseClicked(MouseEvent e);
    public abstract void mouseMoved(MouseEvent e);
}
