package view.raw;

import view.components.DrawPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

public class MainFrame extends JFrame {
    private static final String WINDOW_NAME = "ICG Paint";
    private static final Dimension MIN_DIMENSION = new Dimension(640, 480);

    private final Dimension COMPONENT_MAX_DIMENSION;

    private final JMenuBar menuBar;
    private final HashMap<String, Integer> menuHashMap;
    private int menuIndex;

    private final JToolBar toolBar;
    private final HashMap<String, AbstractButton> buttonsHashMap;

    protected final DrawPanel drawPanel;

    private void configure(int width, int height) {
        Dimension prefferdDimension = new Dimension(width, height);
        setPreferredSize(prefferdDimension);
        setMinimumSize(MIN_DIMENSION);

        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - width) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - height) / 2);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     *  Creates raw MainFrame template
     * @param width width of frame
     * @param height height of frame
     */
    protected MainFrame(int width, int height) {
        super(WINDOW_NAME);
        configure(width, height);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        toolBar = new JToolBar();
        toolBar.addSeparator();
        add(toolBar, BorderLayout.NORTH);

        COMPONENT_MAX_DIMENSION = new Dimension(50, 50);

        drawPanel = new DrawPanel(
                getWidth() - toolBar.getWidth() - menuBar.getWidth(),
                getHeight() - toolBar.getHeight() - menuBar.getHeight());
        add(drawPanel, BorderLayout.CENTER);
        

        menuHashMap = new HashMap<>();
        buttonsHashMap = new HashMap<>();

        menuIndex = 0;
    }

    protected void addMenu(String title) {
        JMenu menu = new JMenu(title);
        menuHashMap.put(title, menuIndex);
        menuBar.add(menu);

        ++menuIndex;
    }

    protected void addMenuItem(String title, String name) {
        menuBar.getMenu(menuHashMap.get(title)).add(name);
    }

    protected void addToolbarButton(String title) {
        JButton button = new JButton(title);
        toolBar.add(button);
        buttonsHashMap.put(title, button);
    }

    protected void addButtonGroup(String[] items) {
        ButtonGroup buttonGroup = new ButtonGroup();
        for (String button_name: items) {
            JRadioButton button = new JRadioButton(button_name);
            buttonGroup.add(button);
            toolBar.add(button);
            buttonsHashMap.put(button_name, button);
        }
    }

    protected void addToolbarCustomComponent(String name, JComponent component) {
        component.setMaximumSize(COMPONENT_MAX_DIMENSION);
        toolBar.add(name, component);
    }

    public void addButtonActionListener(String title, ActionListener listener) {
        buttonsHashMap.get(title).addActionListener(listener);
    }

    public void addDrawPanelMouseListener(MouseListener l) {
        drawPanel.addMouseListener(l);
    }

    public void addDrawPanelMouseMotionListener(MouseMotionListener l) {
        drawPanel.addMouseMotionListener(l);
    }
}
