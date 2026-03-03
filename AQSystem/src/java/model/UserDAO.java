/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import utils.DBUtils;

/**
 *
 * @author Admin
 */
public class UserDAO {

    private static final String LOGIN = "SELECT u.*, ur.role_id\n"
            + "FROM Users u\n"
            + "JOIN UserRole ur ON u.user_id = ur.user_id\n"
            + "WHERE u.username = ? AND u.password_hash = ?";

    public UserDTO login(String username, String password) throws SQLException {
        UserDTO user = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(LOGIN);
                ptm.setString(1, username);
                ptm.setString(2, password);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    int userID = rs.getInt("user_id");
                    String fullName = rs.getString("full_name");
                    int roleID = rs.getInt("role_ID");
                    String email = rs.getString("email");
                    boolean status = rs.getBoolean("Status");
                    Timestamp ts = rs.getTimestamp("created_at");
                    LocalDateTime createdAt = (ts != null) ? ts.toLocalDateTime() : null;
                    user = new UserDTO(userID, username, password, fullName, email, roleID, status, createdAt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return user;
    }
}
