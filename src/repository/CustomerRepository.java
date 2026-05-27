package repository;

import model.Customer;
import java.sql.*;
import java.util.*;

public class CustomerRepository {
    private Connection conn;

    public CustomerRepository(Connection conn) {
        this.conn = conn;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM customers");
            while (rs.next()) {
                list.add(new Customer(rs.getInt("id"), rs.getString("full_name"),
                        rs.getString("email"), rs.getString("phone"), rs.getString("address")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addCustomer(Customer c) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO customers (full_name,email,phone,address) VALUES (?,?,?,?)");
            ps.setString(1, c.getFullName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getAddress());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCustomer(Customer c) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE customers SET full_name=?,email=?,phone=?,address=? WHERE id=?");
            ps.setString(1, c.getFullName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getAddress());
            ps.setInt(5, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCustomer(int id) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM customers WHERE id=?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}