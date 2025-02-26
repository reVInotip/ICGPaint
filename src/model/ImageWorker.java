package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWorker {
    private static Color defaultBackground;
    public static BufferedImage panelContent;
    private static BufferedImage preloadedImage;

    public static void createPanelContent(int width, int height) {
        panelContent = new BufferedImage(System.WIDTH, System.HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    public static void setPanelContentColor(Color color) {
        defaultBackground = color;

        for (int y = 0; y < panelContent.getHeight(); y++) {
            for (int x = 0; x < panelContent.getWidth(); x++) {
                panelContent.setRGB(x, y, defaultBackground.getRGB()); // Устанавливаем цвет пикселя
            }
        }
    }

    public static void resizePanelContent(int newW, int newH) {
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < dimg.getHeight(); y++) {
            for (int x = 0; x < dimg.getWidth(); x++) {
                dimg.setRGB(x, y, defaultBackground.getRGB()); // Устанавливаем цвет пикселя
            }
        }

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(panelContent, 0, 0, null);
        g2d.dispose();

        panelContent = dimg;
    }

    public static void cleanPanelContent() {
        for (int y = 0; y < panelContent.getHeight(); y++) {
            for (int x = 0; x < panelContent.getWidth(); x++) {
                panelContent.setRGB(x, y, defaultBackground.getRGB()); // Устанавливаем цвет пикселя
            }
        }
    }


    public static void apply() {
        if (preloadedImage == null) {
            return;
        }

        Graphics2D g2d = panelContent.createGraphics();
        g2d.drawImage(preloadedImage, 0, 0, null);
        g2d.dispose();
    }

    public static void preload(String imagePath, String imageName) {
        try {
            preloadedImage = ImageIO.read(new File(imagePath, imageName));

            int mWidth = Math.max(preloadedImage.getWidth(), panelContent.getWidth());
            int mHeight = Math.max(preloadedImage.getHeight(), panelContent.getHeight());

            resizePanelContent(mWidth, mHeight);
        } catch (IOException e) {
            java.lang.System.err.println("Can not load new file with name " +
                    imageName + " to directory " + imagePath + " because " + e.getMessage());
        }
    }

    public static void save(String imagePath, String imageName) {
        try {
            File newImage = new File(imagePath, imageName);
            ImageIO.write(panelContent, "png", newImage);
        } catch (IOException e) {
            java.lang.System.err.println("Can not store new file with name " +
                    imageName + " to directory " + imagePath + " because " + e.getMessage());
        }
    }
}
