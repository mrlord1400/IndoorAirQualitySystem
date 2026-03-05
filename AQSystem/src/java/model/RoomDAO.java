package model;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private final DataSource dataSource;

    // Thay vì dùng DBUtils, ta truyền DataSource từ bên ngoài vào
    public RoomDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean insertRoom(RoomDTO room) {
        String sql = "INSERT INTO Room (code, name, room_type, location, status) VALUES (?, ?, ?, ?, 1)";
        // Lấy connection trực tiếp từ dataSource được truyền vào
        try (Connection conn = dataSource.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getCode());
            ps.setString(2, room.getName());
            ps.setString(3, room.getRoomType());
            ps.setString(4, room.getLocation());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
        }
        return false;
    }

    public List<RoomDTO> getAllRooms() {
        List<RoomDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Room WHERE status = 1";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new RoomDTO(
                    rs.getInt("room_id"),
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getString("room_type"),
                    rs.getString("location"),
                    rs.getBoolean("status")
                ));
            }
        } catch (SQLException e) {
        }
        return list;
    }

    // Các phương thức updateRoom, deleteRoom thực hiện tương tự...
}
