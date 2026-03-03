package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.AlertActionDAO;
import model.AlertActionDTO;
import model.AlertDAO;
import model.UserDTO;

@WebServlet(name = "AlertController", urlPatterns = {"/AlertController"})
public class AlertController extends HttpServlet {

    private static final String LOGIN = "login.jsp";
    private static final String DASHBOARD = "adminDashboard.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String referer = request.getHeader("Referer");
        String url = (referer != null) ? referer : DASHBOARD;

        try {
            // 1. Lấy tham số từ Request
            String alertIdRaw = request.getParameter("alertId");
            String action = request.getParameter("action");
            String note = request.getParameter("note");

            if (alertIdRaw == null || action == null) {
                url = DASHBOARD;
            } else {
                long alertId = Long.parseLong(alertIdRaw);

                // 2. Kiểm tra Session người dùng
                HttpSession session = request.getSession();
                UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
                
                if (loginUser == null) {
                    url = LOGIN;
                } else {
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
                }
            }
        } catch (Exception e) {
            log("Error at AlertController: " + e.toString());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
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
        processRequest(request, response);
    }
}
