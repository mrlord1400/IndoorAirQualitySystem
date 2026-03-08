package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ReadingDAO;
import model.ReadingDTO;

@WebServlet("/ReadingController") 
public class ReadingController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        if ("exportCSV".equals(action)) {
            // NHÁNH 1: Xử lý tải file CSV
            handleExportCSV(response);
        } else {
            // NHÁNH 2: Mặc định hiển thị bảng dữ liệu lên JSP
            handleViewData(request, response);
        }
    }

    private void handleViewData(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        ReadingDAO dao = new ReadingDAO();
        List<ReadingDTO> data = dao.getAllReadings();
        
        // Đẩy dữ liệu vào attribute đặt tên là "readings" để khớp với file JSP [cite: 15]
        request.setAttribute("readings", data); 
        
        // Chuyển hướng sang trang ViewReading.jsp để hiển thị bảng và nút bấm [cite: 9]
        request.getRequestDispatcher("ViewReading.jsp").forward(request, response);
    }

    private void handleExportCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"AirQualityData.csv\"");

        ReadingDAO dao = new ReadingDAO();
        List<ReadingDTO> data = dao.getAllReadings();

        try (PrintWriter writer = response.getWriter()) {
            writer.println("ID,SensorID,PollutantID,Timestamp,Value,QualityFlag");
            for (ReadingDTO r : data) {
                StringBuilder sb = new StringBuilder();
                sb.append(r.getReadingID()).append(",")
                  .append(r.getSensorID()).append(",")
                  .append(r.getPollutantID()).append(",")
                  .append(r.getTs()).append(",")
                  .append(r.getValue()).append(",")
                  .append(r.getQualityFlag());
                writer.println(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



/* còn cái này là exportController, t để đây luôn nha
 
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ReadingDTO;

@WebServlet("/ExportController")
public class ExportController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. LẤY DỮ LIỆU (Sau này bạn sẽ lấy từ Session hoặc DAO, giờ ta dùng Mock Data)
        List<ReadingDTO> listToExport = getMockData(); // Hàm giả lập dữ liệu bên dưới

        // 2. THIẾT LẬP THÔNG SỐ CHO TRÌNH DUYỆT (Để trình duyệt biết đây là file tải về)
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"reading_report.csv\"");

        // 3. XUẤT DỮ LIỆU QUA LUỒNG (OutputStream)
        try (PrintWriter writer = response.getWriter()) {
            // Ghi Header
            writer.println("ReadingID,SensorID,PollutantID,Timestamp,Value,QualityFlag,CreatedAt");

            // Duyệt List và ghi từng dòng (Dùng println để tự động xuống dòng)
            for (ReadingDTO r : listToExport) {
                writer.print(r.getReadingID() + ",");
                writer.print(r.getSensorID() + ",");
                writer.print(r.getPollutantID() + ",");
                writer.print(r.getTs() + ",");
                writer.print(r.getValue() + ",");
                writer.print(r.getQualityFlag() + ",");
                writer.println(r.getCreatedAt());
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xuất file trên Web: " + e.getMessage());
        }
    }

    // Hàm tạo dữ liệu giả để Quân test trên trình duyệt
    private List<ReadingDTO> getMockData() {
        List<ReadingDTO> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        list.add(new ReadingDTO(1L, 10, 1, now, 2.6, "Good", now));
        list.add(new ReadingDTO(2L, 11, 1, now.plusMinutes(5), 15.5, "Good", now));
        return list;
    }
}

*/




