package view.components;

import model.Settings;

import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {
    private final JComboBox<Integer> countVertices = new JComboBox<>(new Integer[] {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
    private final JSlider radiusSlider = new JSlider(JSlider.HORIZONTAL, 10, 510, 25);
    private final JSlider angleSlider = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);

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
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(component, gbc);
    }

    public SettingsDialog(JFrame parent, int width, int height) {
        super(parent, "Дополнительные настройки", true);
        countVertices.addActionListener(actionEvent -> Settings.vertices = Integer.parseInt(countVertices.getSelectedItem().toString()));

        JLabel radiusLabel = new JLabel(String.valueOf(radiusSlider.getValue()));
        radiusSlider.addChangeListener(changeEvent -> {
            Settings.radius = radiusSlider.getValue();
            radiusLabel.setText(String.valueOf(radiusSlider.getValue()));
        });

        radiusSlider.setMajorTickSpacing(100);
        radiusSlider.setMinorTickSpacing(10);
        radiusSlider.setPaintTicks(true);
        radiusSlider.setPaintLabels(true);
        radiusSlider.setPreferredSize(new Dimension(300, 50));

        JLabel angleLabel = new JLabel(String.valueOf(angleSlider.getValue()));
        angleSlider.addChangeListener(changeEvent -> {
            Settings.angle = angleSlider.getValue();
            angleLabel.setText(String.valueOf(angleSlider.getValue()));
        });

        angleSlider.setMajorTickSpacing(40);
        angleSlider.setMinorTickSpacing(10);
        angleSlider.setPaintTicks(true);
        angleSlider.setPaintLabels(true);
        angleSlider.setPreferredSize(new Dimension(300, 50));

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Отступы между компонентами

        addLabelAndComponent(contentPanel, gbc, "Count vertices: ", countVertices, 0);

        addLabelAndComponent(contentPanel, gbc, "Radius: ", radiusLabel, 1);
        addComponent(contentPanel, gbc, radiusSlider, 2);

        addLabelAndComponent(contentPanel, gbc, "Angle: ", angleLabel, 3);
        addComponent(contentPanel, gbc, angleSlider, 4);

        add(contentPanel, BorderLayout.CENTER);
        setSize(width, height);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - width) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - height) / 2);
        setResizable(false);
    }
}
