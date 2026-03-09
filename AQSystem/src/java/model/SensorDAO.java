package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 * Lớp SensorDAO đóng vai trò là tầng truy xuất dữ liệu (Data Access Object).
 * Công dụng: Quản lý các thao tác CRUD (Create, Read, Update, Delete) với bảng Cảm biến trong cơ sở dữ liệu.
 */
public class SensorDAO {

    // --- PHẦN CŨ: GIỮ NGUYÊN ---
    /**
     * Công dụng: Tìm danh sách các cảm biến đã lâu không gửi tín hiệu (quá ngưỡng thời gian quy định).
     * Cơ chế: Tự khởi tạo và đóng kết nối tới DB thông qua DBUtils.
     */
    public List<SensorDTO> getInactiveSensors(int thresholdMinutes) {
        List<SensorDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Sensors WHERE lastSeenTs < DATEADD(minute, ?, GETDATE()) ORDER BY lastSeenTs ASC";

        try ( Connection conn = DBUtils.getConnection(); 
              PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, -thresholdMinutes); 
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new SensorDTO(
                        rs.getInt("sensorID"), 
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

    // --- PHẦN MỚI CHÈN: HỖ TRỢ CÁC TÁC VỤ QUẢN LÝ ---
    
    /**
     * Công dụng: Thêm một bản ghi cảm biến mới vào cơ sở dữ liệu.
     * Cơ chế: Nhận Connection từ bên ngoài để đảm bảo tính nhất quán (Transaction).
     */
    public boolean addSensor(SensorDTO sensor, Connection conn) {
        String sql = "INSERT INTO Sensor (room_id, serial_no, model, status, installed_at) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sensor.getRoomID());
            ps.setString(2, sensor.getSerialNo());
            ps.setString(3, sensor.getModel());
            ps.setBoolean(4, sensor.isStatus());
            ps.setTimestamp(5, Timestamp.valueOf(sensor.getInstalledAt()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Công dụng: Truy xuất toàn bộ danh sách cảm biến hiện có.
     * Cơ chế: Sử dụng helper method để map dữ liệu từ ResultSet sang Object.
     */
    public List<SensorDTO> getAllSensors(Connection conn) {
        List<SensorDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Sensor";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToSensor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Công dụng: Lọc danh sách cảm biến thuộc về một phòng cụ thể (roomID).
     * Cơ chế: Truy vấn có điều kiện (WHERE clause) giúp tối ưu hóa hiệu năng thay vì lấy toàn bộ dữ liệu.
     */
    public List<SensorDTO> getSensorsByRoom(int roomID, Connection conn) {
        List<SensorDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Sensor WHERE room_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSensor(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Công dụng: Phương thức hỗ trợ (Helper) để chuyển đổi từ dòng dữ liệu SQL (ResultSet) sang đối tượng (DTO).
     * Lợi ích: Tăng khả năng bảo trì, tránh lặp code (DRY - Don't Repeat Yourself).
     */
    private SensorDTO mapResultSetToSensor(ResultSet rs) throws SQLException {
        return new SensorDTO(
            rs.getInt("sensor_id"),
            rs.getInt("room_id"),
            rs.getString("serial_no"),
            rs.getString("model"),
            rs.getBoolean("status"),
            rs.getTimestamp("installed_at") != null ? rs.getTimestamp("installed_at").toLocalDateTime() : null,
            rs.getTimestamp("last_seen_ts") != null ? rs.getTimestamp("last_seen_ts").toLocalDateTime() : null
        );
    }

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

