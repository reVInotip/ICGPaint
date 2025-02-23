package view.components;

import event.Observer;
import event.events.DrawLineEvent;
import event.events.DrawLinesEvent;
import event.events.DrawTempLineEvent;
import event.events.Event;
import model.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class DrawPanel extends JPanel implements Observer {
    protected Event event;
    private BufferedImage panelContent;

    public DrawPanel(int width, int height) {
        panelContent = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                panelContent = resize(panelContent, getWidth(), getHeight());
            }
        });

        /*setVerticalScrollBar(new JScrollBar());
        setHorizontalScrollBar(new JScrollBar());
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);*/
    }

    private BufferedImage resize(BufferedImage img, int newW, int newH) {
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    private void drawLine(Point start, Point end) {
        Point newEnd = new Point(Math.abs(end.x - start.x), Math.abs(end.y - start.y));

        int x = 0;
        int y = 0;
        int calcConstX = end.x - start.x < 0 ? -1 : 1;
        int calcConstY = end.y - start.y < 0 ? -1 : 1;

        try {
            if (newEnd.y > newEnd.x) {
                int err = -newEnd.y;
                for (int i = 0; i < newEnd.y; ++i) {
                    y++;
                    err += 2 * newEnd.x;
                    if (err > 0) {
                        err -= 2 * newEnd.y;
                        x++;
                    }

                    int movedX = calcConstX * x + start.x;
                    int movedY = calcConstY * y + start.y;
                    if ((movedX >= 0 && movedX < panelContent.getWidth()) && (movedY >= 0 && movedY < panelContent.getHeight())) {
                        panelContent.setRGB(movedX, movedY, Settings.drawingColor.getRGB());
                    }
                }
            } else {
                int err = -newEnd.x;
                for (int i = 0; i < newEnd.x; ++i) {
                    x++;
                    err += 2 * newEnd.y;
                    if (err > 0) {
                        err -= 2 * newEnd.x;
                        y++;
                    }

                    int movedX = calcConstX * x + start.x;
                    int movedY = calcConstY * y + start.y;
                    if ((movedX >= 0 && movedX < panelContent.getWidth()) && (movedY >= 0 && movedY < panelContent.getHeight())) {
                        panelContent.setRGB(movedX, movedY, Settings.drawingColor.getRGB());
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Coordinates: (" +  x + ", " + y + ") out of panel bounds (width="
                    + getWidth() + ", height=" + getHeight() + ")!");
        }
    }

    @Override
    public void update(Event event) {
        this.event = event;
        paintComponent(getGraphics());
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Settings.drawingColor);
        if (event instanceof DrawLineEvent drawLineEvent) {
            if (Settings.thickness > 1) {
                Graphics2D g2 = (Graphics2D) panelContent.getGraphics();
                g2.setStroke(new BasicStroke(Settings.thickness));
                g2.setColor(Settings.drawingColor);
                g2.drawLine(drawLineEvent.start.x, drawLineEvent.start.y, drawLineEvent.end.x, drawLineEvent.end.y);
            } else {
                drawLine(drawLineEvent.start, drawLineEvent.end);
            }
        } else if (event instanceof DrawTempLineEvent drawTempLineEvent) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(Settings.thickness));
            g2.drawLine(drawTempLineEvent.start.x, drawTempLineEvent.start.y, drawTempLineEvent.end.x, drawTempLineEvent.end.y);
        } else if (event instanceof DrawLinesEvent drawLinesEvent) {
            if (Settings.thickness > 1) {
                Graphics2D g2 = (Graphics2D) panelContent.getGraphics();
                g2.setStroke(new BasicStroke(Settings.thickness));
                g2.setColor(Settings.drawingColor);
                for (int i = 0, j = 1; j < drawLinesEvent.points.size(); ++i, ++j) {
                    g2.drawLine(drawLinesEvent.points.get(i).x, drawLinesEvent.points.get(i).y, drawLinesEvent.points.get(j).x, drawLinesEvent.points.get(j).y);
                }
                g2.drawLine(drawLinesEvent.points.getFirst().x, drawLinesEvent.points.getFirst().y, drawLinesEvent.points.getLast().x, drawLinesEvent.points.getLast().y);
            } else {
                for (int i = 0, j = 1; j < drawLinesEvent.points.size(); ++i, ++j) {
                    drawLine(drawLinesEvent.points.get(i), drawLinesEvent.points.get(j));
                }
                drawLine(drawLinesEvent.points.getFirst(), drawLinesEvent.points.getLast());
            }
        } else {
            super.paintComponent(g);
        }

        g.drawImage(panelContent, 0, 0, this);
    }
}
