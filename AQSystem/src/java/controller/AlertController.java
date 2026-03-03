package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet; // Quan trọng: Cần kế thừa cái này
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AlertActionDAO;
import model.AlertActionDTO;
import model.AlertDAO;
import model.UserDTO;

@WebServlet(name = "AlertController", urlPatterns = {"/AlertController"})
public class AlertController extends HttpServlet {

    /**
     * doGet: Thường dùng để lấy thông tin hoặc hiển thị trang chi tiết alert
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        response.sendRedirect("home.jsp"); 
    }

    /**
     * doPost: Dùng để xử lý các thay đổi dữ liệu (Update status, Insert log)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 1. Lấy tham số từ Request
            String alertIdRaw = request.getParameter("alertId");
            String action = request.getParameter("action"); 
            String note = request.getParameter("note");

            if (alertIdRaw == null || action == null) {
                response.sendRedirect("error.jsp");
                return;
            }

            long alertId = Long.parseLong(alertIdRaw);
            
            // 2. Kiểm tra Session người dùng
            UserDTO loginUser = (UserDTO) request.getSession().getAttribute("LOGIN_USER");
            if (loginUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            int actorId = loginUser.getUserID();

            // 3. Khởi tạo DAO
            AlertDAO alertDao = new AlertDAO();
            AlertActionDAO actionDao = new AlertActionDAO();

            String actionType = "";
            String statusToUpdate = "";

            // 4. Xác định loại hành động
            if ("ack".equals(action)) {
                actionType = "ACKNOWLEDGE";
                statusToUpdate = "Acknowledged";
            } else if ("close".equals(action)) {
                actionType = "CLOSE";
                statusToUpdate = "Closed";
            }

            // 5. Thực thi logic nghiệp vụ
            boolean isUpdated = alertDao.updateAlertStatus((int) alertId, statusToUpdate);

            if (isUpdated) {
                AlertActionDTO logEntry = new AlertActionDTO(
                        0,
                        alertId,
                        actorId,
                        actionType,
                        (note == null || note.isEmpty()) ? "No note provided" : note,
                        java.time.LocalDateTime.now()
                );
                actionDao.insertAlertAction(logEntry);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi để debug
        }

        // 6. Quay lại trang trước đó
        String referer = request.getHeader("Referer");
        if (referer != null) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect("adminDashboard.jsp"); // Fallback nếu không tìm thấy trang trước
        }
    }
}
