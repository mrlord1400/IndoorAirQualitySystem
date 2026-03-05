package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.ReadingDAO;
import model.ReadingDTO;
import model.SensorDAO;
import model.SensorDTO;

public class CSVSimulator {

    private String csvPath;
    private SensorDAO sensorDAO = new SensorDAO();
    private ReadingDAO readingDAO = new ReadingDAO();

    public CSVSimulator(String csvPath) {
        this.csvPath = csvPath;
    }

    // Hàm bắt đầu mô phỏng
    public void startSimulation() {
        List<SensorDTO> sensors = sensorDAO.getAllSensors();
        
        // Tạo một bộ quản lý luồng (ThreadPool)
        ExecutorService executor = Executors.newFixedThreadPool(sensors.size());

        for (SensorDTO sensor : sensors) {
            // Mỗi sensor chạy một luồng riêng
            executor.execute(new SensorWorker(sensor));
        }
    }

    // Lớp xử lý riêng cho từng Sensor
    private class SensorWorker implements Runnable {
        private SensorDTO sensor;

        public SensorWorker(SensorDTO sensor) {
            this.sensor = sensor;
        }

        @Override
        public void run() {
            try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
                String line;
                br.readLine(); 

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length < 15) continue; // Bỏ qua dòng lỗi/trống

                    // 1. Xử lý gộp Ngày và Giờ
                    // Định dạng trong CSV là d/M/yyyy và H:mm:ss
                    String dateStr = data[0];
                    String timeStr = data[1];
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy H:mm:ss");
                    LocalDateTime ts = LocalDateTime.parse(dateStr + " " + timeStr, formatter);
                
                    double value = Double.parseDouble(data[2]); 

                    // 3. Tạo DTO và lưu vào DB 
                    ReadingDTO reading = new ReadingDTO(0, sensor.getSensorID(), 1, ts, value, "NORMAL", LocalDateTime.now());
                    
                    boolean success = readingDAO.insertReading(reading);
                    
                    if(success) {
                        System.out.println("Sensor " + sensor.getSensorID() + " gửi dữ liệu: " + value);
                    }

                    // 4. Nghỉ 5-10 giây để tạo cảm giác thời gian thực
                    Thread.sleep(5000 + (int)(Math.random() * 5000));
                }
            } catch (Exception e) {
                System.err.println("Lỗi luồng Sensor " + sensor.getSensorID() + ": " + e.getMessage());
            }
        }
    }
