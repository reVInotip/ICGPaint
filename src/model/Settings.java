package model;

import java.awt.*;

public class Settings {
    public enum StampForm {
        STAR,
        POLYGON
    }

    public static Color drawingColor = Color.BLACK;
    public static int thickness = 1;
    public static int vertices = 3;
    public static int angle = 0;
    public static StampForm from = StampForm.POLYGON;
    public static int radius = 25;
}
