package repository;

import model.Car;
import java.sql.*;
import java.util.*;

public class CarRepository {
    private Connection conn;

    public CarRepository(Connection conn) {
        this.conn = conn;
    }

    public List<Car> getAllCars() {
        List<Car> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM cars");
            while (rs.next()) {
                list.add(new Car(rs.getInt("id"), rs.getString("make"), rs.getString("model"),
                        rs.getInt("year"), rs.getString("color"), rs.getString("vin"),
                        rs.getDouble("price"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Car> getAvailableCars() {
        List<Car> list = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM cars WHERE status='available'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Car(rs.getInt("id"), rs.getString("make"), rs.getString("model"),
                        rs.getInt("year"), rs.getString("color"), rs.getString("vin"),
                        rs.getDouble("price"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addCar(Car car) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO cars (make,model,year,color,vin,price,status) VALUES (?,?,?,?,?,?,?)");
            ps.setString(1, car.getMake());
            ps.setString(2, car.getModel());
            ps.setInt(3, car.getYear());
            ps.setString(4, car.getColor());
            ps.setString(5, car.getVin());
            ps.setDouble(6, car.getPrice());
            ps.setString(7, car.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCar(Car car) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE cars SET make=?,model=?,year=?,color=?,vin=?,price=?,status=? WHERE id=?");
            ps.setString(1, car.getMake());
            ps.setString(2, car.getModel());
            ps.setInt(3, car.getYear());
            ps.setString(4, car.getColor());
            ps.setString(5, car.getVin());
            ps.setDouble(6, car.getPrice());
            ps.setString(7, car.getStatus());
            ps.setInt(8, car.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCar(int id) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM cars WHERE id=?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(int id, String status) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE cars SET status=? WHERE id=?");
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}