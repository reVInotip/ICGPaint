package view;

import event.Observable;
import event.Observer;
import event.events.CleanEvent;
import model.ImageWorker;
import model.Settings;
import model.System;
import view.components.SettingsDialog;
import view.raw.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static model.ImageWorker.panelContent;

public class InitMainFrame extends MainFrame {
    private final List<Observer> toolsObservers = new ArrayList<>();
    private final SettingsDialog settingsDialog = new SettingsDialog(this, 500, 400);

    private static class SimpleObservable extends Observable {}

    private static final SimpleObservable observable = new SimpleObservable();

    private void createDescription() {
        String descr = "nothing there";
        try (InputStream fileStream = InitMainFrame.class.getResourceAsStream("/description.txt")) {
            if (fileStream == null) {
                java.lang.System.err.println("Description file not found!");
                throw new IOException();
            }

            descr = new String(fileStream.readAllBytes());
        } catch (Exception e) {
            java.lang.System.err.println("Can not add description for About menu item: " + e.getMessage());
        }
        java.lang.System.out.println(descr);

        addMenu("Help");
        String finalDescr = descr;
        addMenuItem("Help", "About", actionEvent -> {
            JOptionPane.showMessageDialog(InitMainFrame.this,
                    finalDescr, "О программе", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void addFileMenu(ActionListener saveListener, ActionListener settingsListener) {
        addMenu("File");
        addMenuItem("File", "Save", saveListener);
        addMenuItem("File", "Load", actionEvent -> {
            FileDialog fd = new FileDialog(InitMainFrame.this, "Открыть изображение", FileDialog.LOAD);
            fd.setFile("*.png; *.jpg; *.jpeg; *.gif; *.bmp");
            fd.setName("Image.png");
            fd.setVisible(true);
            if (fd.getFile() != null && fd.getDirectory() != null) {
                ImageWorker.preload(fd.getDirectory(), fd.getFile());
            }
            ImageWorker.cleanPanelContent();
            drawManager.resizePanel(panelContent.getWidth(), panelContent.getHeight());
            ImageWorker.apply();

            drawManager.getMain().redraw();
            drawManager.revalidate();
            drawManager.repaint();
        });
        addMenuItem("File", "Settings", settingsListener);
    }

    private void addColorButtons() {
        JRadioButton selectedButton = new JRadioButton();

        JButton colorButton = new JButton();
        colorButton.addActionListener(actionEvent -> {
            Color defaultColor = Settings.drawingColor;
            Settings.drawingColor = JColorChooser.showDialog(InitMainFrame.this, "Choose color", defaultColor);
            selectedButton.setBackground(Settings.drawingColor);
        });
        addToolbarCustomComponent("Color chooser", "Выбор цвета", "/utils/paint-palette.png", colorButton);

        selectedButton.setBackground(Settings.drawingColor);
        selectedButton.addActionListener(actionEvent -> {
            Settings.drawingColor = selectedButton.getBackground();
        });
        selectedButton.setToolTipText("Выбрать заданный в палитре цвет");

        JRadioButton redButton = new JRadioButton();
        redButton.setBackground(Color.RED);
        redButton.addActionListener(actionEvent -> {
            Settings.drawingColor = Color.RED;
        });
        redButton.setToolTipText("Выбрать красный цвет");

        JRadioButton greenButton = new JRadioButton();
        greenButton.setBackground(Color.GREEN);
        greenButton.addActionListener(actionEvent -> {
            Settings.drawingColor = Color.GREEN;
        });
        greenButton.setToolTipText("Выбрать зелёный цвет");

        JRadioButton blueButton = new JRadioButton();
        blueButton.setBackground(Color.BLUE);
        blueButton.addActionListener(actionEvent -> {
            Settings.drawingColor = Color.BLUE;
        });
        blueButton.setToolTipText("Выбрать синий цвет");

        addToolbarButtonGroup(new JRadioButton[]{selectedButton, redButton, greenButton, blueButton});
    }

    private void createSaveButton(ActionListener saveListener) {
        addToolbarButton("Save", "Сохраняет изображение", "/utils/save.png", saveListener);
        addToolbarSeparator();
    }

    private void createSizeButton() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 500, 1));
        spinner.addChangeListener(changeEvent -> {
            Settings.thickness = Integer.parseInt(String.valueOf(spinner.getValue()));
        });
        addToolbarCustomComponent("Size", "Настройка размера линии", null, spinner);

        addToolbarSeparator();
    }

    private void createSettingsButton(ActionListener settingsListener) {
        JButton settingsButton = new JButton();
        settingsButton.addActionListener(settingsListener);

        addToolbarCustomComponent("Optional settings", "Дополнительные настройки", "/utils/settings.png", settingsButton);
        addToolbarSeparator();
    }

    private void createCleanButton() {
        JButton cleanButton = new JButton();
        cleanButton.addActionListener(actionEvent -> observable.update(new CleanEvent()));
        addToolbarCustomComponent("Clean", "Очистка области", "/utils/clean.png", cleanButton);
    }

    private void createMenus(ActionListener saveListener, ActionListener settingsListener) {
        addFileMenu(saveListener, settingsListener);
        addMenu("View");
        createDescription();
    }

    private void createToolbarButtons(ActionListener saveListener, ActionListener settingsListener) {
        createSaveButton(saveListener);
        addColorButtons();
        createSizeButton();
        createSettingsButton(settingsListener);
        createCleanButton();
    }

    public InitMainFrame() {
        super(System.WIDTH, System.HEIGHT);

        toolsObservers.add(drawManager.getMain());
        observable.add(drawManager.getMain());

        toolsObservers.add(drawManager.getTmp());
        observable.add(drawManager.getTmp());

        ActionListener saveListener = action -> {
            FileDialog fd = new FileDialog(InitMainFrame.this, "Сохранить изображение", FileDialog.SAVE);
            fd.setFile("*.png");
            fd.setName("Image.png");
            fd.setVisible(true);
            if (fd.getFile() != null && fd.getDirectory() != null) {
                ImageWorker.save(fd.getDirectory(), fd.getFile());
            }
        };

        ActionListener settingsListener = actionEvent -> settingsDialog.setVisible(true);

        createMenus(saveListener, settingsListener);
        createToolbarButtons(saveListener, settingsListener);
    }

    public void showFrame() {
        pack();
        setVisible(true);
    }

    public List<Observer> getToolsObservers() {
        return toolsObservers;
    }

    public void addToolsButtons(String[] tools, Map<String, String> toolsDescr, Map<String, String> icons) {
        addToolGroup(tools, toolsDescr, icons);
    }
}
