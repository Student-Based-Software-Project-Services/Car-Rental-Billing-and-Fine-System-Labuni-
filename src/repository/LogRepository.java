package repository;

import java.sql.*;
import java.util.*;

public class LogRepository {
    private Connection conn;

    public LogRepository(Connection conn) {
        this.conn = conn;
    }

    public void log(String username, String action) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO activity_logs (username,action) VALUES (?,?)");
            ps.setString(1, username);
            ps.setString(2, action);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getAllLogs() {
        List<String[]> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM activity_logs ORDER BY log_time DESC");
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("username"),
                    rs.getString("action"),
                    rs.getTimestamp("log_time").toString()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}