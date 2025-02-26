package view.components;

import event.Observer;
import event.events.*;
import event.events.Event;
import model.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static model.ImageWorker.panelContent;

public class TmpPanel extends JPanel implements Observer {
    private int width;
    private int height;
    private Event event;

    private int sizeCount = 1;
    private final int sizeTimeout = 5;

    public TmpPanel() {
        super();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (sizeCount == sizeTimeout) {
                    sizeCount = 1;
                    int mWidth = Math.max(getWidth(), TmpPanel.this.width);
                    int mHeight = Math.max(getHeight(), TmpPanel.this.height);
                    resizePanel(mWidth, mHeight);
                } else {
                    ++sizeCount;
                }
            }
        });

        setOpaque(false);
    }

    public void setSize(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        this.width = width;
        this.height = height;
    }

    public void resizePanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
    }

    public void clear(Graphics g) {
        g.drawImage(panelContent, 0, 0, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        clear(g);
        g.setColor(Settings.drawingColor);
        switch (event) {
            case DrawTempLineEvent drawTempLineEvent -> {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(Settings.thickness));
                g2.drawLine(drawTempLineEvent.start.x, drawTempLineEvent.start.y, drawTempLineEvent.end.x, drawTempLineEvent.end.y);
            }
            case DrawTempLinesEvent drawTempLinesEvent -> {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(Settings.thickness));
                g2.setColor(Settings.drawingColor);
                for (int i = 0, j = 1; j < drawTempLinesEvent.points.size(); ++i, ++j) {
                    g2.drawLine(drawTempLinesEvent.points.get(i).x, drawTempLinesEvent.points.get(i).y, drawTempLinesEvent.points.get(j).x, drawTempLinesEvent.points.get(j).y);
                }
                g2.drawLine(drawTempLinesEvent.points.getFirst().x, drawTempLinesEvent.points.getFirst().y, drawTempLinesEvent.points.getLast().x, drawTempLinesEvent.points.getLast().y);
            }
            case null, default -> {}
        }

        this.event = null;
    }

    @Override
    public void update(Event event) {
        this.event = event;
        paintComponent(getGraphics());
    }
}
