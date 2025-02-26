package view.components;

import event.Observer;
import event.events.*;
import event.events.Event;
import model.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;

import static model.ImageWorker.*;

public class DrawPanel extends JPanel implements Observer {
    private Event event;
    private int width, height;

    private int sizeCount = 1;
    private final int sizeTimeout = 5;

    public DrawPanel() {
        super();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (sizeCount == sizeTimeout) {
                    sizeCount = 1;
                    int mWidth = Math.max(getWidth(), DrawPanel.this.width);
                    int mHeight = Math.max(getHeight(), DrawPanel.this.height);
                    resizePanel(mWidth, mHeight);
                } else {
                    ++sizeCount;
                }
            }
        });

        //setBackground(new Color(255, 255, 255, 0));
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
        resizePanelContent(width, height);
        setPreferredSize(new Dimension(width, height));
    }

    private void drawLine(int x1, int y1, int x2, int y2) {
        if (((x1 < 0 || x1 > width - 1) || (y1 < 0 || y1 > height - 1)) &&
                ((x2 < 0 || x2 > width - 1) || (y2 < 0 || y2 > height - 1))) {
            return;
        }

        int dx = x2 - x1;
        int dy = y2 - y1;
        if (x1 < 0) {
            y1 = (-x1 * dy) / dx + y1;
            x1 = 0;
        } else if (x1 > width - 1) {
            y1 = ((width - 1 - x1) * dy) / dx + y1;
            x1 = width - 1;
        }

        if (x2 < 0) {
            y2 = (-x1 * dy) / dx + y1;
            x2 = 0;
        } else if (x2 > width - 1) {
            y2 = ((width - 1 - x1) * dy) / dx + y1;
            x2 = width - 1;
        }

        if (y1 < 0) {
            x1 = (-y1 * dx) / dy + x1;
            y1 = 0;
        } else if (y1 > height - 1) {
            x1 = ((height - 1 - y1) * dx) / dy + x1;
            y1 = height - 1;
        }

        if (y2 < 0) {
            x2 = (-y1 * dx) / dy + x1;
            y2 = 0;
        } else if (y2 > height - 1) {
            x2 = ((height - 1 - y1) * dx) / dy + x1;
            y2 = height - 1;
        }

        panelContent.setRGB(x1, y1, Settings.drawingColor.getRGB());
        panelContent.setRGB(x2, y2, Settings.drawingColor.getRGB());

        Point newEnd = new Point(Math.abs(x2 - x1), Math.abs(y2 - y1));

        int x = 0;
        int y = 0;
        int calcConstX = x2 - x1 < 0 ? -1 : 1;
        int calcConstY = y2 - y1 < 0 ? -1 : 1;

        int movedX = 0;
        int movedY = 0;
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

                    movedX = calcConstX * x + x1;
                    movedY = calcConstY * y + y1;
                    panelContent.setRGB(movedX, movedY, Settings.drawingColor.getRGB());
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

                    movedX = calcConstX * x + x1;
                    movedY = calcConstY * y + y1;
                    panelContent.setRGB(movedX, movedY, Settings.drawingColor.getRGB());
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Coordinates: (" + movedX + ", " + movedY + ") out of panel bounds (width="
                    + panelContent.getWidth() + ", height=" + panelContent.getHeight() + ")!");
        }
    }

    private void createSpans(Stack<Point> spanStack, int left, int right, int y, int targetColor) {
        for (int x = left; x <= right; ++x) {
            if (panelContent.getRGB(x, y) == targetColor) {
                int startX = x;
                while (x <= right && panelContent.getRGB(x, y) == targetColor) {
                    ++x;
                }

                spanStack.push(new Point(startX, y));
            }
        }
    }

    private void fillArea(Point seed) {
        int targetColor = panelContent.getRGB(seed.x, seed.y);
        if (targetColor == Settings.drawingColor.getRGB()) {
            return;
        }
        int width = panelContent.getWidth();
        int height = panelContent.getHeight();

        final Stack<Point> spanStack = new Stack<>();
        spanStack.push(seed);

        int right;
        int left;
        while (!spanStack.empty()) {
            Point pixel = spanStack.pop();

            left = pixel.x;
            while (left >= 0 && panelContent.getRGB(left, pixel.y) == targetColor) {
                panelContent.setRGB(left, pixel.y, Settings.drawingColor.getRGB());
                left--;
            }
            left++;

            right = pixel.x + 1;
            while (right <= width - 1 && panelContent.getRGB(right, pixel.y) == targetColor) {
                panelContent.setRGB(right, pixel.y, Settings.drawingColor.getRGB());
                right++;
            }
            right--;

            if (pixel.y > 0) {
                createSpans(spanStack, left, right, pixel.y - 1, targetColor);
            }

            if (pixel.y < height - 1) {
                createSpans(spanStack, left, right, pixel.y + 1, targetColor);
            }
        }
    }

    public void redraw() {
        Graphics g = getGraphics();
        super.paintComponent(g);
        g.drawImage(panelContent, 0, 0, this);
    }

    @Override
    public void update(Event event) {
        this.event = event;
        paintComponent(getGraphics());
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Settings.drawingColor);
        switch (event) {
            case DrawLineEvent drawLineEvent -> {
                if (Settings.thickness > 1) {
                    Graphics2D g2 = (Graphics2D) panelContent.getGraphics();
                    g2.setStroke(new BasicStroke(Settings.thickness));
                    g2.setColor(Settings.drawingColor);
                    g2.drawLine(drawLineEvent.start.x, drawLineEvent.start.y, drawLineEvent.end.x, drawLineEvent.end.y);
                } else {
                    drawLine(drawLineEvent.start.x, drawLineEvent.start.y, drawLineEvent.end.x, drawLineEvent.end.y);
                }
            }
            case DrawLinesEvent drawLinesEvent -> {
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
                        drawLine(drawLinesEvent.points.get(i).x, drawLinesEvent.points.get(i).y, drawLinesEvent.points.get(j).x, drawLinesEvent.points.get(j).y);
                    }
                    drawLine(drawLinesEvent.points.getFirst().x, drawLinesEvent.points.getFirst().y, drawLinesEvent.points.getLast().x, drawLinesEvent.points.getLast().y);
                }
            }
            case FillEvent fillEvent -> fillArea(fillEvent.seed);
            case CleanEvent ignored -> {
                super.paintComponent(g);
                cleanPanelContent();
            }
            case null, default -> {}
        }

        this.event = null;

        g.drawImage(panelContent, 0, 0, this);
    }
}
