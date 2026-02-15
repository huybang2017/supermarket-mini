package SieuThiMiniDAO;

import java.sql.*;
import javax.swing.*;

public class MyConnection {
    private String user = "root";
    private String password = "";
    private String url = "jdbc:mysql://localhost:3306/sieuthimini";
    private Connection cnn = null;

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cnn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy Driver JDBC!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi kết nối: " + ex.getMessage());
        }
        return cnn;
    }

    public void closeConnection() {
        try {
            if (cnn != null && !cnn.isClosed()) {
                cnn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}