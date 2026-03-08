package service;

import model.ReadingDTO;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExportService {

    // Đây là hàm chính để xuất file, nhận vào danh sách và tên file muốn lưu
    public void exportToCSV(List<ReadingDTO> list, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // 1. Ghi dòng tiêu đề (Header)
            writer.append("ReadingID,SensorID,PollutantID,Timestamp,Value,QualityFlag,CreatedAt\n");

            // 2. Duyệt qua từng Object trong List và ghi vào file
            for (ReadingDTO r : list) {
                writer.append(String.valueOf(r.getReadingID())).append(",")
                      .append(String.valueOf(r.getSensorID())).append(",")
                      .append(String.valueOf(r.getPollutantID())).append(",")
                      .append(r.getTs().toString()).append(",")
                      .append(String.valueOf(r.getValue())).append(",")
                      .append(r.getQualityFlag()).append(",")
                      .append(r.getCreatedAt().toString()).append("\n");
            }
            
            System.out.println("Chúc mừng Quân! Đã xuất file thành công tại: " + filePath);
            
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }

    /*
    // HÀM MAIN ĐỂ QUÂN TEST ĐỘC LẬP
    public static void main(String[] args) {
        // 1. Giả lập dữ liệu (Dữ liệu giả bạn muốn)
        List<ReadingDTO> mockList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        mockList.add(new ReadingDTO(1L, 10, 1, now, 2.6, "Good", now));
        mockList.add(new ReadingDTO(2L, 11, 1, now.plusMinutes(5), 15.5, "Good", now));
        mockList.add(new ReadingDTO(3L, 10, 1, now.plusMinutes(10), -200.0, "Bad", now));

        // 2. Gọi hàm xuất file
        ExportService service = new ExportService();
        // Bạn có thể đổi đường dẫn này tùy ý (ví dụ: "D:/test_export.csv")
        service.exportToCSV(mockList, "test_export.csv");
    }
*/
}