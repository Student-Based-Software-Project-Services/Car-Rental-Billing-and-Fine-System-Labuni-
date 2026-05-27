package frames;

import model.Car;
import javax.swing.*;
import java.awt.*;

public class CarDialog extends JDialog {
    private Car car;
    private JTextField txtMake, txtModel, txtYear, txtColor, txtVin, txtPrice;
    private JComboBox<String> cmbStatus;

    public CarDialog(Frame parent, Car existing) {
        super(parent, existing == null ? "Add Car" : "Edit Car", true);
        setSize(400, 380);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        txtMake = new JTextField(15);
        txtModel = new JTextField(15);
        txtYear = new JTextField(15);
        txtColor = new JTextField(15);
        txtVin = new JTextField(15);
        txtPrice = new JTextField(15);
        cmbStatus = new JComboBox<>(new String[]{"available", "sold", "reserved"});

        String[] labels = {"Make", "Model", "Year", "Color", "VIN", "Price", "Status"};
        JComponent[] fields = {txtMake, txtModel, txtYear, txtColor, txtVin, txtPrice, cmbStatus};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel lbl = new JLabel(labels[i] + ":");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            panel.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            panel.add(fields[i], gbc);
        }

        if (existing != null) {
            txtMake.setText(existing.getMake());
            txtModel.setText(existing.getModel());
            txtYear.setText(String.valueOf(existing.getYear()));
            txtColor.setText(existing.getColor());
            txtVin.setText(existing.getVin());
            txtPrice.setText(String.valueOf(existing.getPrice()));
            cmbStatus.setSelectedItem(existing.getStatus());
        }

        JButton btnSave = new JButton("Save");
        btnSave.setBackground(Color.white);
        btnSave.setForeground(new Color(60, 160, 100));
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnSave.setFocusPainted(false);
        btnSave.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnCancel.addActionListener(e -> dispose());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.setBackground(Color.WHITE);
        btnRow.add(btnCancel);
        btnRow.add(btnSave);

        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        panel.add(btnRow, gbc);

        setContentPane(panel);

        btnSave.addActionListener(e -> {
            try {
                int id = existing == null ? 0 : existing.getId();
                car = new Car(id, txtMake.getText(), txtModel.getText(),
                        Integer.parseInt(txtYear.getText()), txtColor.getText(),
                        txtVin.getText(), Double.parseDouble(txtPrice.getText()),
                        (String) cmbStatus.getSelectedItem());
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Year and Price must be numbers.");
            }
        });
    }

    public Car getCar() { return car; }
}