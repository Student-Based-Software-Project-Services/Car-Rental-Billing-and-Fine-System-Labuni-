package frames;

import model.Customer;
import model.User;
import repository.RepoManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JPanel {
    private User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;

    public CustomerPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 244, 250));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(240, 244, 250));
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JLabel title = new JLabel("Customer Records");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(30, 45, 70));

        JButton btnAdd = new JButton("+ Add Customer");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");

        styleButton(btnAdd, new Color(60, 160, 100));
        styleButton(btnEdit, new Color(70, 130, 200));
        styleButton(btnDelete, new Color(210, 70, 70));

        header.add(title);
        header.add(Box.createHorizontalStrut(20));
        header.add(btnAdd);
        header.add(btnEdit);
        header.add(btnDelete);

        String[] cols = {"ID", "Full Name", "Email", "Phone", "Address"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.setSelectionBackground(new Color(190, 215, 245));
        table.setGridColor(new Color(220, 228, 240));
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));

        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        loadData();

        btnAdd.addActionListener(e -> {
            CustomerDialog dlg = new CustomerDialog(null, null);
            dlg.setVisible(true);
            if (dlg.getCustomer() != null) {
                RepoManager.getInstance().getCustomerRepo().addCustomer(dlg.getCustomer());
                RepoManager.getInstance().getLogRepo().log(currentUser.getUsername(), "Added customer: " + dlg.getCustomer().getFullName());
                loadData();
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a customer to edit."); return; }
            Customer c = getCustomer(row);
            CustomerDialog dlg = new CustomerDialog(null, c);
            dlg.setVisible(true);
            if (dlg.getCustomer() != null) {
                RepoManager.getInstance().getCustomerRepo().updateCustomer(dlg.getCustomer());
                RepoManager.getInstance().getLogRepo().log(currentUser.getUsername(), "Updated customer ID: " + c.getId());
                loadData();
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a customer to delete."); return; }
            Customer c = getCustomer(row);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this customer?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                RepoManager.getInstance().getCustomerRepo().deleteCustomer(c.getId());
                RepoManager.getInstance().getLogRepo().log(currentUser.getUsername(), "Deleted customer ID: " + c.getId());
                loadData();
            }
        });
    }

    private Customer getCustomer(int row) {
        return new Customer(
                (int) tableModel.getValueAt(row, 0),
                (String) tableModel.getValueAt(row, 1),
                (String) tableModel.getValueAt(row, 2),
                (String) tableModel.getValueAt(row, 3),
                (String) tableModel.getValueAt(row, 4)
        );
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Customer> list = RepoManager.getInstance().getCustomerRepo().getAllCustomers();
        for (Customer c : list) {
            tableModel.addRow(new Object[]{c.getId(), c.getFullName(), c.getEmail(), c.getPhone(), c.getAddress()});
        }
    }

    private void styleButton(JButton btn, Color fg) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}