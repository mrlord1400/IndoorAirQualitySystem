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
}
