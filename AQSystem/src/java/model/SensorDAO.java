/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class SensorDAO {

    public List<SensorDTO> getInactiveSensors(int thresholdMinutes) {
        List<SensorDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Sensors WHERE lastSeenTs < DATEADD(minute, ?, GETDATE()) ORDER BY lastSeenTs ASC";

        try ( Connection conn = DBUtils.getConnection(); 
                  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, -thresholdMinutes); 
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new SensorDTO(
                        rs.getInt("sensorID"), // 
                        rs.getInt("roomID"),
                        rs.getString("serialNo"),
                        rs.getString("model"),
                        rs.getBoolean("status"),
                        rs.getTimestamp("installedAt").toLocalDateTime(),
                        rs.getTimestamp("lastSeenTs").toLocalDateTime()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    // Lấy toàn bộ danh sách cảm biến đang hoạt động để chia luồng (Thread)
    public List<SensorDTO> getAllSensors() {
        List<SensorDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Sensor WHERE status = 1"; // Chỉ lấy sensor active

        try (Connection conn = DBUtils.getConnection()) {
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    // Chuyển đổi dữ liệu từ SQL sang đối tượng SensorDTO
                    SensorDTO dto = new SensorDTO(
                            rs.getInt("sensor_id"),
                            rs.getInt("room_id"),
                            rs.getString("serial_no"),
                            rs.getString("model"),
                            rs.getBoolean("status"),
                            rs.getTimestamp("installed_at").toLocalDateTime(),

                            rs.getTimestamp("last_seen_ts") != null 
                                ? rs.getTimestamp("last_seen_ts").toLocalDateTime() : null
                    );
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
} 


