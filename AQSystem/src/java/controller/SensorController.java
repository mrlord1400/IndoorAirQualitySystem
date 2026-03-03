package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.SensorDAO;
import model.SensorDTO;

@WebServlet(name = "SensorController", urlPatterns = {"/SensorController"})
public class SensorController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        SensorDAO dao = new SensorDAO();
        
        // Giả sử ta muốn tìm các sensor không gửi dữ liệu trong 30 phút qua
        int threshold = 30; 
        
        List<SensorDTO> inactiveSensors = dao.getInactiveSensors(threshold);
        
        // Gửi danh sách này sang trang JSP
        request.setAttribute("sensorList", inactiveSensors);
        request.setAttribute("threshold", threshold);
        
        request.getRequestDispatcher("sensor.jsp").forward(request, response);
    }
}
