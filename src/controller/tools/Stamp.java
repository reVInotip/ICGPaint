package controller.tools;

import controller.ToolView;
import event.events.DrawLineEvent;
import event.events.DrawLinesEvent;
import event.events.DrawTempLinesEvent;
import model.Settings;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Set;

@ToolView(name = "Stamp", descr = "Правильные многоугольники и звёзды", icon = "/tools/polygon.png")
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
        if (Settings.form == Settings.StampForm.POLYGON) {
            for (int i = 0; i < Settings.vertices; ++i) {
                double angle = ((2 * Math.PI * i) / Settings.vertices) + Math.toRadians(Settings.angle);
                double x = Settings.radius * Math.cos(angle) + e.getX();
                double y = Settings.radius * Math.sin(angle) + e.getY();

                pointList.add(new Point((int) Math.round(x), (int) Math.round(y)));
            }

            update(new DrawLinesEvent(pointList));

            pointList.clear();
        } else {
            double innerRotate = Math.toRadians(180.0d / Settings.vertices);

            for (int i = 0; i < Settings.vertices; ++i) {
                double angle = ((2 * Math.PI * i) / Settings.vertices) + Math.toRadians(Settings.angle);
                double x = Settings.radius * Math.cos(angle) + e.getX();
                double y = Settings.radius * Math.sin(angle) + e.getY();

                double innerAngle = angle + innerRotate;
                double innerX = Settings.innerRadius * Math.cos(innerAngle) + e.getX();
                double innerY = Settings.innerRadius * Math.sin(innerAngle) + e.getY();

                pointList.add(new Point((int) Math.round(x), (int) Math.round(y)));
                pointList.add(new Point((int) Math.round(innerX), (int) Math.round(innerY)));
            }

            update(new DrawLinesEvent(pointList));

            pointList.clear();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (Settings.form == Settings.StampForm.POLYGON) {
            for (int i = 0; i < Settings.vertices; ++i) {
                double angle = ((2 * Math.PI * i) / Settings.vertices) + Math.toRadians(Settings.angle);
                double x = Settings.radius * Math.cos(angle) + e.getX();
                double y = Settings.radius * Math.sin(angle) + e.getY();

                pointList.add(new Point((int) Math.round(x), (int) Math.round(y)));
            }

            update(new DrawTempLinesEvent(pointList));

            pointList.clear();
        } else {
            double innerRotate = Math.toRadians(180.0d / Settings.vertices);

            for (int i = 0; i < Settings.vertices; ++i) {
                double angle = ((2 * Math.PI * i) / Settings.vertices) + Math.toRadians(Settings.angle);
                double x = Settings.radius * Math.cos(angle) + e.getX();
                double y = Settings.radius * Math.sin(angle) + e.getY();

                double innerAngle = angle + innerRotate;
                double innerX = Settings.innerRadius * Math.cos(innerAngle) + e.getX();
                double innerY = Settings.innerRadius * Math.sin(innerAngle) + e.getY();

                pointList.add(new Point((int) Math.round(x), (int) Math.round(y)));
                pointList.add(new Point((int) Math.round(innerX), (int) Math.round(innerY)));
            }

            update(new DrawTempLinesEvent(pointList));

            pointList.clear();
        }
    }
}
