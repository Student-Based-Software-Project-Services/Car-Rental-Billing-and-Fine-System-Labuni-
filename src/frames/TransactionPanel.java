package frames;

import model.Car;
import model.Customer;
import model.Transaction;
import model.User;
import repository.RepoManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class TransactionPanel extends JPanel {
    private User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;

    public TransactionPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 244, 250));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(240, 244, 250));
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JLabel title = new JLabel("Car Purchase Transactions");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(30, 45, 70));

        JButton btnNew = new JButton("+ New Transaction");
        JButton btnPay = new JButton("Record Payment");

        styleButton(btnNew, new Color(60, 160, 100));
        styleButton(btnPay, new Color(70, 130, 200));

        header.add(title);
        header.add(Box.createHorizontalStrut(20));
        header.add(btnNew);
        header.add(btnPay);

        String[] cols = {"ID", "Customer", "Car", "Sale Price", "Sale Date", "Due Date", "Status"};
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

        btnNew.addActionListener(e -> {
            List<Customer> customers = RepoManager.getInstance().getCustomerRepo().getAllCustomers();
            List<Car> cars = RepoManager.getInstance().getCarRepo().getAvailableCars();
            if (customers.isEmpty()) { JOptionPane.showMessageDialog(this, "No customers found. Add a customer first."); return; }
            if (cars.isEmpty()) { JOptionPane.showMessageDialog(this, "No available cars in inventory."); return; }

            JComboBox<String> cmbCustomer = new JComboBox<>();
            for (Customer c : customers) cmbCustomer.addItem(c.getId() + " - " + c.getFullName());

            JComboBox<String> cmbCar = new JComboBox<>();
            for (Car c : cars) cmbCar.addItem(c.getId() + " - " + c.getYear() + " " + c.getMake() + " " + c.getModel() + " ($" + c.getPrice() + ")");

            JTextField txtDueDate = new JTextField(LocalDate.now().plusMonths(1).toString());

            JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
            form.add(new JLabel("Customer:")); form.add(cmbCustomer);
            form.add(new JLabel("Car:")); form.add(cmbCar);
            form.add(new JLabel("Due Date (YYYY-MM-DD):")); form.add(txtDueDate);

            int res = JOptionPane.showConfirmDialog(this, form, "New Transaction", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    int custId = customers.get(cmbCustomer.getSelectedIndex()).getId();
                    Car selectedCar = cars.get(cmbCar.getSelectedIndex());
                    Date saleDate = Date.valueOf(LocalDate.now());
                    Date dueDate = Date.valueOf(txtDueDate.getText().trim());
                    Transaction tx = new Transaction(0, custId, selectedCar.getId(), selectedCar.getPrice(), saleDate, dueDate, "unpaid");
                    RepoManager.getInstance().getTransactionRepo().addTransaction(tx);
                    RepoManager.getInstance().getCarRepo().updateStatus(selectedCar.getId(), "sold");
                    RepoManager.getInstance().getLogRepo().log(currentUser.getUsername(), "New transaction: Customer ID " + custId + " bought Car ID " + selectedCar.getId());
                    loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.");
                }
            }
        });

        btnPay.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a transaction first."); return; }
            int txId = (int) tableModel.getValueAt(row, 0);
            String status = tableModel.getValueAt(row, 5).toString();
            new PaymentDialog(null, txId, Date.valueOf(status), currentUser).setVisible(true);
            loadData();
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Transaction> list = RepoManager.getInstance().getTransactionRepo().getAllTransactions();
        for (Transaction t : list) {
            tableModel.addRow(new Object[]{t.getId(), t.getCustomerName(), t.getCarInfo(),
                    String.format("%.2f", t.getSalePrice()), t.getSaleDate(), t.getDueDate(), t.getStatus()});
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