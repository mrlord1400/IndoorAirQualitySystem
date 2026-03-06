package model;

import java.time.LocalDateTime;
import java.util.Date;

public class RoomDTO {
    private int roomId; //
    private String code; //
    private String name; //
    private String roomType; //
    private String location; //
    private boolean status; //
    private Date createdAt; //

    public RoomDTO() {}

    public RoomDTO(int roomId, String code, String name, String roomType, String location, boolean status) {
        this.roomId = roomId;
        this.code = code;
        this.name = name;
        this.roomType = roomType;
        this.location = location;
        this.status = status;
    }

    RoomDTO(int aInt, String string, String string0, String string1, String string2, boolean aBoolean, LocalDateTime toLocalDateTime) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    String getType() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    int getRoomID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
