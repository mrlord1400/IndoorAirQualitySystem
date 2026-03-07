package controller;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.SensorDAO;
import model.SensorDTO;
import utils.DBUtils;

@WebServlet(name = "SensorController", urlPatterns = {"/SensorController"})
public class SensorController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        SensorDAO dao = new SensorDAO();

        try (Connection conn = DBUtils.getConnection()) {
            if (action == null || action.equals("list")) {
                // 1. Lấy toàn bộ danh sách cảm biến
                List<SensorDTO> list = dao.getAllSensors(conn);
                request.setAttribute("SENSOR_LIST", list);
                request.getRequestDispatcher("sensor.jsp").forward(request, response);

            } else if (action.equals("filterByRoom")) {
                // 2. Lọc cảm biến theo phòng
                int roomID = Integer.parseInt(request.getParameter("roomID"));
                List<SensorDTO> list = dao.getSensorsByRoom(roomID, conn);
                request.setAttribute("SENSOR_LIST", list);
                request.getRequestDispatcher("sensor.jsp").forward(request, response);

            } else if (action.equals("checkInactive")) {
                // 3. Kiểm tra cảm biến không hoạt động (Dùng phương thức tự quản lý kết nối)
                int threshold = Integer.parseInt(request.getParameter("threshold"));
                List<SensorDTO> list = dao.getInactiveSensors(threshold);
                request.setAttribute("SENSOR_LIST", list);
                request.setAttribute("MESSAGE", "Danh sách cảm biến ngoại tuyến trên " + threshold + " phút.");
                request.getRequestDispatcher("sensor.jsp").forward(request, response);
            }

        } catch (Exception e) {
            log("Error at SensorController: " + e.toString());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        SensorDAO dao = new SensorDAO();

        if ("add".equals(action)) {
            try (Connection conn = DBUtils.getConnection()) {
                // Lấy dữ liệu từ form
                int roomID = Integer.parseInt(request.getParameter("roomID"));
                String serialNo = request.getParameter("serialNo");
                String model = request.getParameter("model");
                boolean status = Boolean.parseBoolean(request.getParameter("status"));
                
                // Tạo đối tượng DTO (Giả sử InstalledAt là thời điểm hiện tại)
                SensorDTO newSensor = new SensorDTO(0, roomID, serialNo, model, status, LocalDateTime.now(), null);
                
                boolean check = dao.addSensor(newSensor, conn);
                if (check) {
                    response.sendRedirect("SensorController?action=list");
                } else {
                    request.setAttribute("ERROR", "Không thể thêm cảm biến mới!");
                    request.getRequestDispatcher("sensor.jsp").forward(request, response);
                }
            } catch (Exception e) {
                log("Error at SensorController POST: " + e.toString());
            }
        }
    }
}
