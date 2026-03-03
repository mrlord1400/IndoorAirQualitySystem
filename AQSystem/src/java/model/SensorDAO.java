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
} 

