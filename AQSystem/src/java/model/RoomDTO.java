package model;

import java.time.LocalDateTime;

public class RoomDTO {
    private int roomId;
    private String code;
    private String name;
    private String roomType;
    private String location;
    private boolean status;
    private LocalDateTime createdAt; // Dùng LocalDateTime cho đồng bộ

    public RoomDTO() {}

    // Constructor đầy đủ dùng cho mapResultSetToRoom
    public RoomDTO(int roomId, String code, String name, String roomType, 
                   String location, boolean status, LocalDateTime createdAt) {
        this.roomId = roomId;
        this.code = code;
        this.name = name;
        this.roomType = roomType;
        this.location = location;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters và Setters
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    // Giữ phương thức này để DAO gọi đúng
    public String getType() { return roomType; } 
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    int getRoomID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
