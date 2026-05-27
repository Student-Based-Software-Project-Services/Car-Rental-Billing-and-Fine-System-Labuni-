package frames;

import model.Payment;
import model.Transaction;
import model.User;
import repository.RepoManager;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PaymentDialog extends JDialog {
    private static final double FINE_RATE_PER_DAY = 0.005;

    public PaymentDialog(Frame parent, int transactionId, Date dueDate, User user) {
        super(parent, "Record Payment", true);
        setSize(420, 340);
        setLocationRelativeTo(parent);
        setResizable(false);

        List<Transaction> all = RepoManager.getInstance().getTransactionRepo().getAllTransactions();
        Transaction tx = null;
        for (Transaction t : all) {
            if (t.getId() == transactionId) { tx = t; break; }
        }

        double salePrice = tx != null ? tx.getSalePrice() : 0;
        Date txDueDate = tx != null ? tx.getDueDate() : dueDate;

        double totalPaid = RepoManager.getInstance().getPaymentRepo().getTotalPaid(transactionId);
        double remaining = salePrice - totalPaid;

        LocalDate today = LocalDate.now();
        LocalDate due = txDueDate.toLocalDate();
        long overdueDays = ChronoUnit.DAYS.between(due, today);
        double fine = overdueDays > 0 ? remaining * FINE_RATE_PER_DAY * overdueDays : 0;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Transaction #" + transactionId);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitle.setForeground(new Color(30, 45, 70));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSale = new JLabel("Sale Price: ₱" + String.format("%.2f", salePrice));
        JLabel lblPaid = new JLabel("Total Paid: ₱" + String.format("%.2f", totalPaid));
        JLabel lblRemain = new JLabel("Remaining: ₱" + String.format("%.2f", remaining));
        JLabel lblFine = new JLabel("Fine (" + (overdueDays > 0 ? overdueDays + " days overdue" : "no overdue") + "): ₱" + String.format("%.2f", fine));
        lblFine.setForeground(overdueDays > 0 ? new Color(210, 70, 70) : new Color(60, 160, 100));

        for (JLabel lbl : new JLabel[]{lblSale, lblPaid, lblRemain, lblFine}) {
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        JLabel lblAmount = new JLabel("Amount to Pay:");
        lblAmount.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblAmount.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtAmount = new JTextField();
        txtAmount.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        txtAmount.setAlignmentX(Component.LEFT_ALIGNMENT);

        double fineVal = fine;
        double remainVal = remaining;
        int txId = transactionId;

        JButton btnPay = new JButton("Confirm Payment");
        btnPay.setBackground(Color.WHITE);
        btnPay.setForeground(new Color(60, 160, 100));
        btnPay.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnPay.setFocusPainted(false);
        btnPay.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnPay.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblSale);
        panel.add(lblPaid);
        panel.add(lblRemain);
        panel.add(lblFine);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblAmount);
        panel.add(Box.createVerticalStrut(4));
        panel.add(txtAmount);
        panel.add(Box.createVerticalStrut(12));
        panel.add(btnPay);

        setContentPane(panel);

        btnPay.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(txtAmount.getText().trim());
                if (amount <= 0) { JOptionPane.showMessageDialog(this, "Enter a valid amount."); return; }
                Payment payment = new Payment(0, txId, amount, Date.valueOf(today), fineVal);
                RepoManager.getInstance().getPaymentRepo().addPayment(payment);
                double newTotalPaid = RepoManager.getInstance().getPaymentRepo().getTotalPaid(txId);
                if (newTotalPaid + fineVal >= salePrice + fineVal - 0.01) {
                    RepoManager.getInstance().getTransactionRepo().updateStatus(txId, "paid");
                }
                RepoManager.getInstance().getLogRepo().log(user.getUsername(), "Payment of ₱" + amount + " for transaction #" + txId);
                JOptionPane.showMessageDialog(this, "Payment recorded successfully.");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
            }
        });
    }
}