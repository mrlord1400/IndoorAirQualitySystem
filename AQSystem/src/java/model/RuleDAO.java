/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RuleDAO {

    // Phương thức thêm quy tắc, nhận vào connection từ Controller/Service
    public boolean addRule(RuleDTO rule, Connection conn) {
        String sql = "INSERT INTO ThresholdRule (room_id, pollutant_id, lower_bound, " +
                     "upper_bound, duration_min, severity, active) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rule.getRoomID());
            ps.setInt(2, rule.getPollutantID());
            ps.setDouble(3, rule.getLowerBound());
            ps.setDouble(4, rule.getUpperBound());
            ps.setInt(5, rule.getDurationMin());
            ps.setString(6, rule.getSeverity());
            ps.setBoolean(7, rule.isActive());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
        }
        return false;
    }

    // Phương thức lấy danh sách, nhận vào connection
    public List<RuleDTO> getAllRules(Connection conn) {
        List<RuleDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ThresholdRule";
        
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(new RuleDTO(
                    rs.getInt("rule_id"),
                    rs.getInt("room_id"),
                    rs.getInt("pollutant_id"),
                    rs.getDouble("lower_bound"),
                    rs.getDouble("upper_bound"),
                    rs.getInt("duration_min"),
                    rs.getString("severity"),
                    rs.getBoolean("active"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
        }
        return list;
    }
}

