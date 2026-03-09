package controller;

import java.io.IOException;
import java.io.PrintWriter;
package controller;

import model.ReadingDAO;
import model.ReadingDTO;
import java.io.IOException;
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

@WebServlet(name = "ReadingController", urlPatterns = {"/reading-controller"})
public class ReadingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Lấy tham số tìm kiếm từ giao diện
        String rIdStr = request.getParameter("roomId");
        String pIdStr = request.getParameter("pollutantId");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");

        // 2. Xử lý logic phân trang (Pagination)
        int pageIndex = 1; 
        int pageSize = 20; // Số dòng hiển thị mỗi trang
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            pageIndex = Integer.parseInt(pageStr);
        }

        // Chuyển đổi kiểu dữ liệu cho DAO
        Integer roomId = (rIdStr != null && !rIdStr.isEmpty()) ? Integer.parseInt(rIdStr) : null;
        Integer pollutantId = (pIdStr != null && !pIdStr.isEmpty()) ? Integer.parseInt(pIdStr) : null;

        ReadingDAO dao = new ReadingDAO();

        // 3. Thực hiện truy vấn dữ liệu theo trang
        List<ReadingDTO> list = dao.searchAdvancedWithPaging(roomId, pollutantId, fromDate, toDate, pageIndex, pageSize);
        
        // 4. Tính toán tổng số trang để hiển thị thanh điều hướng
        int totalRecords = dao.getTotalRecords(roomId, pollutantId, fromDate, toDate);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // 5. Gửi dữ liệu sang JSP
        request.setAttribute("READINGS_LIST", list);
        request.setAttribute("TOTAL_PAGES", totalPages);
        request.setAttribute("CURRENT_PAGE", pageIndex);
        
        // Giả sử trang hiển thị của bạn là reading-list.jsp hoặc search.jsp
        request.getRequestDispatcher("reading-list.jsp").forward(request, response);
    }
}







