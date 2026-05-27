package repository;

import model.Transaction;
import java.sql.*;
import java.util.*;

public class TransactionRepository {
    private Connection conn;

    public TransactionRepository(Connection conn) {
        this.conn = conn;
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        try {
            String query = "SELECT t.*, c.full_name, CONCAT(ca.year,' ',ca.make,' ',ca.model) AS car_info " +
                    "FROM transactions t JOIN customers c ON t.customer_id=c.id JOIN cars ca ON t.car_id=ca.id";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Transaction tx = new Transaction(rs.getInt("id"), rs.getInt("customer_id"),
                        rs.getInt("car_id"), rs.getDouble("sale_price"),
                        rs.getDate("sale_date"), rs.getDate("due_date"), rs.getString("status"));
                tx.setCustomerName(rs.getString("full_name"));
                tx.setCarInfo(rs.getString("car_info"));
                list.add(tx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addTransaction(Transaction t) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO transactions (customer_id,car_id,sale_price,sale_date,due_date,status) VALUES (?,?,?,?,?,?)");
            ps.setInt(1, t.getCustomerId());
            ps.setInt(2, t.getCarId());
            ps.setDouble(3, t.getSalePrice());
            ps.setDate(4, t.getSaleDate());
            ps.setDate(5, t.getDueDate());
            ps.setString(6, t.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(int id, String status) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE transactions SET status=? WHERE id=?");
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}