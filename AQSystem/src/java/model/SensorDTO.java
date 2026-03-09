package model;

import java.time.LocalDateTime;

public class SensorDTO {

    private int sensorID;
    private int roomID;
    private String serialNo;
    private String model;
    private boolean status;
    private LocalDateTime installedAt;
    private LocalDateTime lastSeenTs;

    /* cái này code vô sql nha, này là bảng map
    CREATE TABLE SensorPollutantMap (
    map_id INT PRIMARY KEY IDENTITY(1,1),
    sensor_id INT,                         -- Tên cột này khớp với bảng Sensor
    pollutant_name NVARCHAR(50),
    csv_column_index INT,
    -- Nối với bảng dbo.Sensor, cột sensor_id
    CONSTRAINT FK_SensorMap FOREIGN KEY (sensor_id) REFERENCES dbo.Sensor(sensor_id)
);

INSERT INTO SensorPollutantMap (sensor_id, pollutant_name, csv_column_index)
VALUES
(1, 'CO', 2),       -- Sensor 1 (MQ-135) lấy cột 2
(2, 'Temp', 12),    -- Sensor 2 (DHT-22) lấy cột 12
(3, 'Dust', 3);     -- Sensor 3 (SDS-011) lấy cột 3
     */
    private int csvColumnIndex;// là cái trên

    public SensorDTO() {
    }

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

    public int getCsvColumnIndex() {
        return csvColumnIndex;
    }

    public void setCsvColumnIndex(int csvColumnIndex) {
        this.csvColumnIndex = csvColumnIndex;
    }

}
