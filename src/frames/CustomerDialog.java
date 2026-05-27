package frames;

import model.Customer;
import javax.swing.*;
import java.awt.*;

public class CustomerDialog extends JDialog {
    private Customer customer;
    private JTextField txtName, txtEmail, txtPhone, txtAddress;

    public CustomerDialog(Frame parent, Customer existing) {
        super(parent, existing == null ? "Add Customer" : "Edit Customer", true);
        setSize(380, 280);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtName = new JTextField(15);
        txtEmail = new JTextField(15);
        txtPhone = new JTextField(15);
        txtAddress = new JTextField(15);

        String[] labels = {"Full Name", "Email", "Phone", "Address"};
        JTextField[] fields = {txtName, txtEmail, txtPhone, txtAddress};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel lbl = new JLabel(labels[i] + ":");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            panel.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            panel.add(fields[i], gbc);
        }

        if (existing != null) {
            txtName.setText(existing.getFullName());
            txtEmail.setText(existing.getEmail());
            txtPhone.setText(existing.getPhone());
            txtAddress.setText(existing.getAddress());
        }

        JButton btnSave = new JButton("Save");
        btnSave.setBackground(Color.WHITE);
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
            int id = existing == null ? 0 : existing.getId();
            customer = new Customer(id, txtName.getText(), txtEmail.getText(), txtPhone.getText(), txtAddress.getText());
            dispose();
        });
    }

    public Customer getCustomer() { return customer; }
}