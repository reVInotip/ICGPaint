package view;

import event.Observer;
import model.Settings;
import view.components.SettingsDialog;
import view.raw.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class InitMainFrame extends MainFrame {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private final List<Observer> toolsObservers = new ArrayList<>();
    private final SettingsDialog settingsDialog = new SettingsDialog(this, 400, 300);

    public InitMainFrame() {
        super(WIDTH, HEIGHT);

        toolsObservers.add(drawPanel);

        addMenu("File");
        addMenuItem("File", "Save");

        addMenu("View");
        addMenuItem("View", "Line");

        addMenu("Help");
        addMenuItem("Help", "About");

        addToolbarButton("Save");

        JSpinner spinner = new JSpinner(new SpinnerListModel(IntStream.range(1, 500).boxed().toArray()));
        spinner.setMaximumSize(new Dimension(50, 50));
        spinner.addChangeListener(changeEvent -> {
            Settings.thickness = Integer.parseInt(String.valueOf(spinner.getValue()));
        });
        addToolbarCustomComponent("Size", spinner);

        JButton colorButton = new JButton();
        colorButton.setBackground(Color.BLACK);
        colorButton.addActionListener(actionEvent -> {
            Color defaultColor = Settings.drawingColor;
            Color newColor = JColorChooser.showDialog(InitMainFrame.this, "Choose color", defaultColor);

            Settings.drawingColor = newColor;
            colorButton.setBackground(newColor);
        });
        addToolbarCustomComponent("Color chooser", colorButton);

        JButton settingsButton = new JButton("Optional settings");
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                settingsDialog.setVisible(true);
            }
        });

        addToolbarCustomComponent("Optional settings", settingsButton);
    }

    public void showFrame() {
        pack();
        setVisible(true);
    }

    public List<Observer> getToolsObservers() {
        return toolsObservers;
    }

    public void addToolsButtons(String[] tools) {
        addButtonGroup(tools);
    }
}
