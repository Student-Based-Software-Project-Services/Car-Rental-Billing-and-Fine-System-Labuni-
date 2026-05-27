package frames;

import model.Payment;
import repository.RepoManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PaymentPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public PaymentPanel(model.User user) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 244, 250));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(240, 244, 250));
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JLabel title = new JLabel("Billing & Payment Records");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(30, 45, 70));
        header.add(title);

        String[] cols = {"ID", "Transaction ID", "Amount Paid", "Payment Date", "Fine Applied"};
        tableModel = new DefaultTableModel(cols, 0) {
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
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Payment> list = RepoManager.getInstance().getPaymentRepo().getAllPayments();
        for (Payment p : list) {
            tableModel.addRow(new Object[]{p.getId(), p.getTransactionId(),
                    String.format("%.2f", p.getAmountPaid()), p.getPaymentDate(),
                    String.format("%.2f", p.getFineApplied())});
        }
    }
}