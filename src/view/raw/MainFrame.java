package view.raw;

import view.components.DrawManager;
import view.components.DrawPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    private static final String WINDOW_NAME = "ICG Paint";
    private static final Dimension MIN_DIMENSION = new Dimension(640, 480);

    private final Dimension COMPONENT_MAX_DIMENSION = new Dimension(50, 50);;

    private final JMenuBar menuBar;
    private final HashMap<String, Integer> menuHashMap;
    private int menuIndex;

    private static class ToolItem {
        public AbstractButton button;
        public JMenuItem item;
    }

    private final JToolBar toolBar;
    private final HashMap<String, ToolItem> toolsHashMap;

    protected final DrawManager drawManager;

    private void configure(int width, int height) {
        Dimension prefferdDimension = new Dimension(width, height);
        setPreferredSize(prefferdDimension);
        setMinimumSize(MIN_DIMENSION);

        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - width) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - height) / 2);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
        toolTipManager.setInitialDelay(500);  // Задержка перед появлением (в миллисекундах)
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

        drawManager = new DrawManager();

        JScrollPane scrollPane = new JScrollPane(drawManager);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scrollPane.getVerticalScrollBar().addAdjustmentListener(adjustmentEvent -> drawManager.getMain().redraw());
        scrollPane.getHorizontalScrollBar().addAdjustmentListener(adjustmentEvent -> drawManager.getMain().redraw());
        add(scrollPane, BorderLayout.CENTER);

        drawManager.init();

        menuHashMap = new HashMap<>();
        toolsHashMap = new HashMap<>();

        menuIndex = 0;
    }

    protected void addMenu(String title) {
        JMenu menu = new JMenu(title);
        menuHashMap.put(title, menuIndex);
        menuBar.add(menu);

        ++menuIndex;
    }

    protected void addMenuItem(String title, String name, ActionListener listener) {
        JMenuItem item = menuBar.getMenu(menuHashMap.get(title)).add(name);
        item.addActionListener(listener);
    }

    protected void addToolbarButton(String title, String descr, String iconPath, ActionListener listener) {
        if (title == null && iconPath == null) {
            System.err.println("Component should have name or icon");
        }

        JButton button = new JButton();
        button.addActionListener(listener);
        if (descr != null) {
            button.setToolTipText(descr);
        }

        if (iconPath != null) {
            try {
                ImageIcon icon = new ImageIcon(
                        MainFrame.class.getResource(iconPath)
                );
                Image image = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(image));
            } catch (NullPointerException e) {
                System.err.println("Can not load icon for button: " + title + " because " + e.getMessage());
            }
        } else {
            button.setName(title);
        }

        toolBar.add(button);
    }

    protected void addToolbarButtonGroup(JRadioButton[] items) {
        ButtonGroup buttonGroup = new ButtonGroup();
        toolBar.addSeparator();
        for (JRadioButton button: items) {
            button.setPreferredSize(new Dimension(40, 40));
            buttonGroup.add(button);
            toolBar.add(button);
        }
        toolBar.addSeparator();
    }

    protected void addToolbarSeparator() {
        toolBar.addSeparator();
    }

    protected void addToolGroup(String[] items, Map<String, String> toolsDescr, Map<String, String> icons) {
        ButtonGroup toolbarButtonGroup = new ButtonGroup();
        ButtonGroup menuButtonGroup = new ButtonGroup();

        for (String tool: items) {
            JRadioButton button = new JRadioButton(tool);
            if (toolsDescr.containsKey(tool)) {
                button.setToolTipText(toolsDescr.get(tool));
            }

            if (icons.containsKey(tool)) {
                try {
                    ImageIcon icon = new ImageIcon(
                            MainFrame.class.getResource(icons.get(tool))
                    );
                    Image image = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                    button.setIcon(new ImageIcon(image));
                } catch (NullPointerException e) {
                    System.err.println("Can not load icon for tool: " + tool + " because " + e.getMessage());
                }
            }

            toolBar.add(button);
            final boolean[] isClicked = {false, false};

            JRadioButtonMenuItem menuButton = new JRadioButtonMenuItem(tool);
            menuButton.addActionListener(actionEvent -> {
                if (!isClicked[0]) {
                    isClicked[0] = true;
                    button.doClick();
                } else {
                    isClicked[0] = false;
                    isClicked[1] = false;
                }
            });
            button.addActionListener(actionEvent -> {
                if (!isClicked[1]) {
                    toolbarButtonGroup.getElements().asIterator().forEachRemaining(b -> b.setBorderPainted(false));
                    button.setBorderPainted(true);
                    isClicked[1] = true;
                    menuButton.doClick();
                } else {
                    isClicked[0] = false;
                    isClicked[1] = false;
                }
            });

            menuButtonGroup.add(menuButton);
            toolbarButtonGroup.add(button);

            ToolItem toolItem = new ToolItem();
            toolItem.button = menuButton;
            toolItem.item = menuBar.getMenu(menuHashMap.get("View")).add(menuButton);
            toolsHashMap.put(tool, toolItem);
        }
    }

    protected void addToolbarCustomComponent(String name, String descr, String iconPath, JComponent component) {
        component.setMaximumSize(COMPONENT_MAX_DIMENSION);
        component.setToolTipText(descr);

        if (iconPath != null && component instanceof JButton button) {
            try {
                ImageIcon icon = new ImageIcon(
                        MainFrame.class.getResource(iconPath)
                );
                Image image = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(image));
            } catch (NullPointerException e) {
                System.err.println("Can not load icon for button: " + name + " because " + e.getMessage());
            }

            toolBar.add(component);
        } else if (name != null) {
            component.setName(name);
            toolBar.add(name, component);
        } else {
            System.err.println("Component should have name or icon");
        }
    }

    public void addToolActionListener(String title, ActionListener listener) {
        toolsHashMap.get(title).button.addActionListener(listener);
        toolsHashMap.get(title).item.addActionListener(listener);
    }

    public void addDrawPanelMouseListener(MouseListener l) {
        drawManager.getMain().addMouseListener(l);
        drawManager.getTmp().addMouseListener(l);
    }

    public void addDrawPanelMouseMotionListener(MouseMotionListener l) {
        drawManager.getMain().addMouseMotionListener(l);
        drawManager.getTmp().addMouseMotionListener(l);
    }
}
