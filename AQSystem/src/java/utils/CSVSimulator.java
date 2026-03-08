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

    public void startSimulation() {
        
        /*Lấy danh sách sensor từ SQL (đã JOIN với bảng Map):
        SELECT s.sensor_id, s.model, m.csv_column_index 
        FROM Sensor s 
        JOIN SensorPollutantMap m ON s.sensor_id = m.sensor_id
        */
        List<SensorDTO> sensors = sensorDAO.getAllSensorsWithMap();
        
        if (sensors == null || sensors.isEmpty()) {
            System.out.println(">>> Number of sensors found: 0. Check the Sensor table and SensorPollutantMap again!");
            return;
        }

        System.out.println(">>> Found" + sensors.size() + " sensor. Start creating a thread...");
        ExecutorService executor = Executors.newFixedThreadPool(sensors.size());

        for (SensorDTO sensor : sensors) {
            executor.execute(new SensorWorker(sensor));
        }
    }

    private class SensorWorker implements Runnable {
        private SensorDTO sensor;

        public SensorWorker(SensorDTO sensor) {
            this.sensor = sensor;
        }

        @Override
        public void run() {
            // Định dạng khớp với file: 3/10/2004 18:00:00 (M/d/yyyy H:mm:ss)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy H:mm:ss");

            try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
                String line;
                br.readLine(); // Bỏ qua Header

                while ((line = br.readLine()) != null) {

                    String[] data = line.split(",");
                    
                    // 2. Kiểm tra dòng trống hoặc thiếu dữ liệu (file có rất nhiều dòng rác)
                    if (data.length < 5 || data[0].trim().isEmpty()) continue;

                    try {
                        // 3. Gộp Ngày và Giờ
                        String fullTs = data[0].trim() + " " + data[1].trim();
                        LocalDateTime ts = LocalDateTime.parse(fullTs, formatter);

                        // 4. Lấy index từ bảng Map
                        int colIndex = sensor.getCsvColumnIndex();
                        if (colIndex >= data.length) continue;

                        String rawValue = data[colIndex].trim();
                        if (rawValue.isEmpty()) continue;
                        
                        double value = Double.parseDouble(rawValue);

                        // 5. In ra màn hình (kể cả -200)
                        if (value <= -200) {
                            System.out.println("Sensor " + sensor.getSensorID() + " [Error]: " + value);
                        } else {
                            // Lưu vào DB nếu dữ liệu ổn
                            ReadingDTO reading = new ReadingDTO(0, sensor.getSensorID(), 1, ts, value, "NORMAL", LocalDateTime.now());
                            boolean success = readingDAO.insertReading(reading);
                            if (success) {
                                System.out.println("Sensor " + sensor.getSensorID() + " [OK]: " + value + " lúc " + fullTs);
                            }
                        }

                        // Nghỉ mô phỏng
                        Thread.sleep(3000);

                    } catch (Exception e) {
                        continue;
                    }
                }
            } catch (Exception e) {
                System.err.println("Lỗi luồng Sensor " + sensor.getSensorID() + ": " + e.getMessage());
            }
        }
    }
/*
    //hàm main để test chạy ok chưa
    public static void main(String[] args) {
        String path = "D:\\SEMESTER 4\\PRJ\\ProjectTeam\\AQSystem\\AQSystem\\AirQualityUCI.csv"; 
        CSVSimulator sim = new CSVSimulator(path);
        sim.startSimulation();
    }
*/
}
