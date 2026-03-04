package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import utils.DBUtils;

public class ReadingDAO {
    
    public boolean insertReading(ReadingDTO reading) {
        String sql = "INSERT INTO Reading (sensor_id, pollutant_id, ts, value, quality_flag)"
                   + " VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtils.getConnection()) { 
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setInt(1, reading.getSensorID());
                ps.setInt(2, reading.getPollutantID());
                // Chuyển LocalDateTime của Java sang Timestamp của SQL
                ps.setTimestamp(3, Timestamp.valueOf(reading.getTs())); 
                ps.setDouble(4, reading.getValue());
                ps.setString(5, reading.getQualityFlag());
                
                int row = ps.executeUpdate(); 
                
                return row > 0; 
            }
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        return false; 
    }
}
