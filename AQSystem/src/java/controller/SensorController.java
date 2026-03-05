package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.SensorDAO;
import model.SensorDTO;

@WebServlet(name = "SensorController", urlPatterns = {"/SensorController"})
public class SensorController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer roleID = (Integer) session.getAttribute("USER_ROLE");

        // 1. Kiểm tra quyền: Chỉ Technician hoặc Admin mới được vào đây
        if (roleID == null || (roleID != 3 && roleID != 1)) {
            request.setAttribute("ERROR", "Bạn không có quyền thực hiện thao tác này!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // 2. Lấy threshold từ request (Technician chỉnh từ giao diện)
        String thresholdParam = request.getParameter("threshold");
        int threshold = 30; // Mặc định

        if (thresholdParam != null && !thresholdParam.isEmpty()) {
            try {
                threshold = Integer.parseInt(thresholdParam);
            } catch (NumberFormatException e) {
                threshold = 30;
            }
        }

        // 3. Xử lý dữ liệu
        SensorDAO dao = new SensorDAO();
        List<SensorDTO> inactiveSensors = dao.getInactiveSensors(threshold);

        // 4. Đẩy dữ liệu ra trang quản lý sensor chuyên biệt
        request.setAttribute("sensorList", inactiveSensors);
        request.setAttribute("threshold", threshold);

        request.getRequestDispatcher("sensor.jsp").forward(request, response);
    }
}
