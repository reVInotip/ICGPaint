package view.components;

import model.Settings;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.IntStream;

public class SettingsDialog extends JDialog {
    final private PreDrawPanel preDrawPanel = new PreDrawPanel();

    private void addLabelAndComponent(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(component, gbc);
    }

    private void addComponent(JPanel panel, GridBagConstraints gbc, JComponent component, int row) {
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(component, gbc);
    }

    public SettingsDialog(JFrame parent, int width, int height) {
        super(parent, "Дополнительные настройки", true);

        final JComboBox<Integer> countVertices = new JComboBox<>(new Integer[] {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
        final JSlider radiusSlider = new JSlider(JSlider.HORIZONTAL, 10, 510, Settings.radius);
        final JSlider angleSlider = new JSlider(JSlider.HORIZONTAL, 0, 360, Settings.angle);
        final JSlider innerRadiusSlider = new JSlider(JSlider.HORIZONTAL, 5, Settings.radius, Settings.innerRadius);
        preDrawPanel.setVisible(true);

        countVertices.addActionListener(actionEvent -> {
            Settings.vertices = Integer.parseInt(countVertices.getSelectedItem().toString());
            preDrawPanel.update();
        });

        JSpinner innerRadiusSpinner = new JSpinner(new SpinnerNumberModel(Settings.innerRadius, 5, Settings.radius, 1));
        innerRadiusSpinner.addChangeListener(changeEvent -> {
            int value = Integer.parseInt(String.valueOf(innerRadiusSpinner.getValue()));
            Settings.innerRadius = value;
            innerRadiusSlider.setValue(value);
            preDrawPanel.update();
        });

        innerRadiusSlider.addChangeListener(changeEvent -> {
            Settings.innerRadius = innerRadiusSlider.getValue();
            innerRadiusSpinner.setValue(innerRadiusSlider.getValue());
            preDrawPanel.update();
        });

        innerRadiusSlider.setMajorTickSpacing(50);
        innerRadiusSlider.setMinorTickSpacing(5);
        innerRadiusSlider.setPaintTicks(true);
        innerRadiusSlider.setPaintLabels(true);
        innerRadiusSlider.setPreferredSize(new Dimension(200, 200));

        JSpinner radiusSpinner = new JSpinner(new SpinnerNumberModel(Settings.radius, 10, 510, 1));
        radiusSpinner.addChangeListener(changeEvent -> {
            Settings.radius = Integer.parseInt(String.valueOf(radiusSpinner.getValue()));
            radiusSlider.setValue(Settings.radius);
            preDrawPanel.update();
        });
        radiusSlider.addChangeListener(changeEvent -> {
            Settings.radius = radiusSlider.getValue();
            innerRadiusSlider.setMaximum(Settings.radius - 5);

            ((SpinnerNumberModel) innerRadiusSpinner.getModel()).setMaximum(Settings.radius - 5);
            radiusSpinner.getModel().setValue(Settings.radius);
            preDrawPanel.update();
        });

        radiusSlider.setMajorTickSpacing(100);
        radiusSlider.setMinorTickSpacing(10);
        radiusSlider.setPaintTicks(true);
        radiusSlider.setPaintLabels(true);
        radiusSlider.setPreferredSize(new Dimension(300, 50));

        JSpinner angleSpinner = new JSpinner(new SpinnerNumberModel(Settings.angle, 0, 360, 1));
        angleSpinner.addChangeListener(changeEvent -> {
            Settings.angle = Integer.parseInt(String.valueOf(angleSpinner.getValue()));
            angleSlider.setValue(Settings.angle);
            preDrawPanel.update();
        });

        angleSlider.addChangeListener(changeEvent -> {
            Settings.angle = angleSlider.getValue();
            angleSpinner.getModel().setValue(Settings.angle);
            preDrawPanel.update();
        });

        angleSlider.setMajorTickSpacing(40);
        angleSlider.setMinorTickSpacing(10);
        angleSlider.setPaintTicks(true);
        angleSlider.setPaintLabels(true);
        angleSlider.setPreferredSize(new Dimension(300, 50));

        JSplitPane contentPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        contentPanel.setDividerLocation(100);

        JPanel settingsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Отступы между компонентами

        addLabelAndComponent(settingsPanel, gbc, "Count vertices: ", countVertices, 0);

        addLabelAndComponent(settingsPanel, gbc, "Radius: ", radiusSpinner, 1);
        addComponent(settingsPanel, gbc, radiusSlider, 2);

        addLabelAndComponent(settingsPanel, gbc, "Inner radius: ", innerRadiusSpinner, 3);
        addComponent(settingsPanel, gbc, innerRadiusSlider, 4);

        addLabelAndComponent(settingsPanel, gbc, "Angle: ", angleSpinner, 5);
        addComponent(settingsPanel, gbc, angleSlider, 6);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        JToggleButton chooseButton = new JToggleButton("Star mode");
        chooseButton.addActionListener(actionEvent -> {
            if (chooseButton.isSelected()) {
                Settings.form = Settings.StampForm.STAR;
            } else {
                Settings.form = Settings.StampForm.POLYGON;
            }

            preDrawPanel.update();
        });

        settingsPanel.add(chooseButton, gbc);
        settingsPanel.setPreferredSize(new Dimension(width, height));

        contentPanel.setLeftComponent(new JScrollPane(preDrawPanel));
        contentPanel.setRightComponent(new JScrollPane(settingsPanel));

        add(contentPanel, BorderLayout.CENTER);
        setMinimumSize(new Dimension(width + 200, height + 200));
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - width - 200) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - height - 200) / 2);
    }
}
