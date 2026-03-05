package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class ReadingDAO {

    // Hàm này add đối tượng ReadingDTO và lưu nó vào bảng Reading trong SQL
    public boolean insertReading(ReadingDTO reading) {
        String sql = "INSERT INTO Reading (sensor_id, pollutant_id, ts, value, quality_flag)"
                + " VALUES (?, ?, ?, ?, ?)";

        try ( Connection conn = DBUtils.getConnection()) {
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
    

    public List<ReadingDTO> getAllReadings() {
        List<ReadingDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Reading";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ReadingDTO dto = new ReadingDTO(
                        rs.getLong("reading_id"),
                        rs.getInt("sensor_id"),
                        rs.getInt("pollutant_id"),
                        rs.getTimestamp("ts").toLocalDateTime(),
                        rs.getDouble("value"),
                        rs.getString("quality_flag"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy N bản ghi gần nhất để kiểm tra xem có vi phạm liên tục không
    public List<ReadingDTO> getRecentReadings(int roomID, int pollutantID, int limit) {
        List<ReadingDTO> list = new ArrayList<>();
        // Sử dụng JOIN với bảng Sensor để lọc theo RoomID
        String sql = "SELECT TOP (?) r.* FROM Reading r " +
                     "JOIN Sensor s ON r.sensor_id = s.sensor_id " +
                     "WHERE s.room_id = ? AND r.pollutant_id = ? " +
                     "ORDER BY r.ts DESC";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, roomID);
            ps.setInt(3, pollutantID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalDateTime ts = rs.getTimestamp("ts").toLocalDateTime();
                list.add(new ReadingDTO(rs.getLong("reading_id"), rs.getInt("sensor_id"), 
                                     rs.getInt("pollutant_id"), ts, rs.getDouble("value"), 
                                     rs.getString("quality_flag"), null));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}

