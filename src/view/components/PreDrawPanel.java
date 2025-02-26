package view.components;
import event.events.DrawLinesEvent;
import event.events.Event;
import model.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PreDrawPanel extends JPanel {
    private ArrayList<Point> calcPoint() {
        ArrayList<Point> points = new ArrayList<>();
        int xOffset = getWidth() / 2;
        int yOffset = getHeight() / 2;

        if (Settings.form == Settings.StampForm.POLYGON) {
            for (int i = 0; i < Settings.vertices; ++i) {
                double angle = ((2 * Math.PI * i) / Settings.vertices) + Math.toRadians(Settings.angle);
                double x = Settings.radius * Math.cos(angle) + xOffset;
                double y = Settings.radius * Math.sin(angle) + yOffset;

                points.add(new Point((int) Math.round(x), (int) Math.round(y)));
            }
        } else {
            double innerRotate = Math.toRadians(180.0d / Settings.vertices);

            for (int i = 0; i < Settings.vertices; ++i) {
                double angle = ((2 * Math.PI * i) / Settings.vertices) + Math.toRadians(Settings.angle);
                double x = Settings.radius * Math.cos(angle) + xOffset;
                double y = Settings.radius * Math.sin(angle) + yOffset;

                double innerAngle = angle + innerRotate;
                double innerX = Settings.innerRadius * Math.cos(innerAngle) + xOffset;
                double innerY = Settings.innerRadius * Math.sin(innerAngle) + yOffset;

                points.add(new Point((int) Math.round(x), (int) Math.round(y)));
                points.add(new Point((int) Math.round(innerX), (int) Math.round(innerY)));
            }
        }

        return points;
    }

    public void update() {
        paintComponent(getGraphics());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ArrayList<Point> points = calcPoint();

        g.setColor(Settings.drawingColor);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(Settings.thickness));
        g2.setColor(Settings.drawingColor);
        for (int i = 0, j = 1; j < points.size(); ++i, ++j) {
            g2.drawLine(points.get(i).x, points.get(i).y, points.get(j).x, points.get(j).y);
        }
        g2.drawLine(points.getFirst().x, points.getFirst().y, points.getLast().x, points.getLast().y);
    }
}
