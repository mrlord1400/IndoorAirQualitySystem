/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;
/**
 *
 * @author Admin
 */
public class SensorDTO {
    private int sensorID;
    private int roomID;
    private String serialNo;
    private String model;
    private boolean status;
    private LocalDateTime installedAt;
    private LocalDateTime lastSeenTs;

    public SensorDTO(int sensorID, int roomID, String serialNo, String model, boolean status, LocalDateTime installedAt, LocalDateTime lastSeenTs) {
        this.sensorID = sensorID;
        this.roomID = roomID;
        this.serialNo = serialNo;
        this.model = model;
        this.status = status;
        this.installedAt = installedAt;
        this.lastSeenTs = lastSeenTs;
    }

    

    public int getSensorID() {
        return sensorID;
    }

    public void setSensorID(int sensorID) {
        this.sensorID = sensorID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDateTime getInstalledAt() {
        return installedAt;
    }

    public void setInstalledAt(LocalDateTime installedAt) {
        this.installedAt = installedAt;
    }

    public LocalDateTime getLastSeenTs() {
        return lastSeenTs;
    }

    public void setLastSeenTs(LocalDateTime lastSeenTs) {
        this.lastSeenTs = lastSeenTs;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    
}
