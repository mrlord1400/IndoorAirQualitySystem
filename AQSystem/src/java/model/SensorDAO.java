package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class SensorDAO {

    public List<SensorDTO> getAllSensorsWithMap() {
        List<SensorDTO> list = new ArrayList<>();
        // Sử dụng JOIN để lấy dữ liệu từ 2 bảng cùng lúc
        String sql = "SELECT s.sensor_id, s.room_id, s.serial_no, s.model, s.status, "
                + "s.installed_at, s.last_seen_ts, m.csv_column_index "
                + "FROM dbo.Sensor s "
                + "JOIN SensorPollutantMap m ON s.sensor_id = m.sensor_id "
                + "WHERE s.status = 1";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SensorDTO dto = new SensorDTO();
                // Bốc dữ liệu từ bảng Sensor
                dto.setSensorID(rs.getInt("sensor_id"));
                dto.setRoomID(rs.getInt("room_id"));
                dto.setSerialNo(rs.getString("serial_no"));
                dto.setModel(rs.getString("model"));
                dto.setStatus(rs.getBoolean("status"));
                dto.setInstalledAt(rs.getTimestamp("installed_at").toLocalDateTime());
                dto.setLastSeenTs(rs.getTimestamp("last_seen_ts").toLocalDateTime());

                if (rs.getTimestamp("installed_at") != null) {
                    dto.setInstalledAt(rs.getTimestamp("installed_at").toLocalDateTime());
                }
                if (rs.getTimestamp("last_seen_ts") != null) {
                    dto.setLastSeenTs(rs.getTimestamp("last_seen_ts").toLocalDateTime());
                }

                // Bốc dữ liệu từ bảng Map (Quan trọng nhất cho Simulator)
                dto.setCsvColumnIndex(rs.getInt("csv_column_index"));

                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
