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
        
        // Đẩy dữ liệu vào attribute đặt tên là "readings" để khớp với file JSP 
        request.setAttribute("readings", data); 
        
        // Chuyển hướng sang trang ViewReading.jsp để hiển thị bảng và nút bấm 
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
