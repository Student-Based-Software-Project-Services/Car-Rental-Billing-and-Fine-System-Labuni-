package repository;

import model.Payment;
import java.sql.*;
import java.util.*;

public class PaymentRepository {
    private Connection conn;

    public PaymentRepository(Connection conn) {
        this.conn = conn;
    }

    public List<Payment> getPaymentsByTransaction(int transactionId) {
        List<Payment> list = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM payments WHERE transaction_id=?");
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Payment(rs.getInt("id"), rs.getInt("transaction_id"),
                        rs.getDouble("amount_paid"), rs.getDate("payment_date"),
                        rs.getDouble("fine_applied")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM payments ORDER BY payment_date DESC");
            while (rs.next()) {
                list.add(new Payment(rs.getInt("id"), rs.getInt("transaction_id"),
                        rs.getDouble("amount_paid"), rs.getDate("payment_date"),
                        rs.getDouble("fine_applied")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addPayment(Payment p) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO payments (transaction_id,amount_paid,payment_date,fine_applied) VALUES (?,?,?,?)");
            ps.setInt(1, p.getTransactionId());
            ps.setDouble(2, p.getAmountPaid());
            ps.setDate(3, p.getPaymentDate());
            ps.setDouble(4, p.getFineApplied());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getTotalPaid(int transactionId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT SUM(amount_paid) FROM payments WHERE transaction_id=?");
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}