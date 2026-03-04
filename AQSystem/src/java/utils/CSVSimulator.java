package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.ReadingDAO;
import model.ReadingDTO;

public class CSVSimulator {

    public void importCSV(String filePath) {
        
        ReadingDAO dao = new ReadingDAO();

        // Tạo "khuôn" để máy hiểu định dạng ngày tháng trong file CSV: 2024-03-01 08:00:00
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try ( BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                try {
                    // Cập nhật: thay ngoặc kép thành rỗng, sau đó xóa khoảng trắng
                    int sensorID = Integer.parseInt(data[0].replace("\"", "").trim());
                    int pollutantID = Integer.parseInt(data[1].replace("\"", "").trim());

                    String tsRaw = data[2].replace("\"", "").trim();
                    LocalDateTime ts = LocalDateTime.parse(tsRaw, formatter);

                    double value = Double.parseDouble(data[3].replace("\"", "").trim());
                    String quality = data[4].replace("\"", "").trim();

                    ReadingDTO dto = new ReadingDTO(0, sensorID, pollutantID, ts, value, quality, LocalDateTime.now());
                    dao.insertReading(dto);

                    System.out.println("Success: " + sensorID + " - " + value);
                } catch (Exception e) {
                    System.err.println("Error line: " + line + " -> " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi đọc file: " + e.getMessage());
        }
    }
}

