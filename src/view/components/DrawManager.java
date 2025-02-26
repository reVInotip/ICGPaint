package view.components;

import model.ImageWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static model.ImageWorker.setPanelContentColor;

public class DrawManager extends JPanel {
    private final DrawPanel drawPanel;
    private final TmpPanel tmpPanel;
    int width;
    int height;

    public DrawManager() {
        super();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int mWidth = Math.max(getWidth(), DrawManager.this.width);
                int mHeight = Math.max(getHeight(), DrawManager.this.height);
                resizePanel(mWidth, mHeight);
            }
        });
        setOpaque(false);

        drawPanel = new DrawPanel();
        tmpPanel = new TmpPanel();

        setLayout(new OverlayLayout(this));
        add(drawPanel);
        add(tmpPanel);
    }

    public void init() {
        this.width = getWidth();
        this.height = getHeight();
        setPreferredSize(new Dimension(width, height));
        ImageWorker.createPanelContent(width, height);

        drawPanel.setSize(width, height);
        drawPanel.setSize(width, height);
        setPanelContentColor(getBackground());
    }

    public void resizePanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        drawPanel.resizePanel(width, height);
        tmpPanel.resizePanel(width, height);
    }

    public DrawPanel getMain() {
        return drawPanel;
    }

    public TmpPanel getTmp() {
        return tmpPanel;
    }
}
