/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.DBUtils;

/**
 *
 * @author Admin
 */
public class AlertDAO {
    // Trong AlertDAO 

    public boolean updateAlertStatus(int alertId, String newStatus) {
        String sql = "UPDATE Alerts SET status = ? WHERE alertID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, alertId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Trong AlertController.java 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy thông tin từ request
        long alertId = Long.parseLong(request.getParameter("alertId"));
        String action = request.getParameter("action"); // Giá trị "ack" hoặc "close"
        String note = request.getParameter("note"); // Lấy ghi chú từ giao diện (nếu có)

        // 2. Lấy thông tin người dùng đang đăng nhập từ Session [cite: 35]
        UserDTO loginUser = (UserDTO) request.getSession().getAttribute("LOGIN_USER");
        int actorId = loginUser.getUserID();

        AlertDAO alertDao = new AlertDAO(); // 
        AlertActionDAO actionDao = new AlertActionDAO(); // 

        String actionType = "";
        String statusToUpdate = "";

        if ("ack".equals(action)) {
            actionType = "ACKNOWLEDGE";
            statusToUpdate = "Acknowledged";
        } else if ("close".equals(action)) {
            actionType = "CLOSE";
            statusToUpdate = "Closed";
        }

        // 3. Thực hiện cập nhật bảng Alert và lưu Log
        boolean isUpdated = alertDao.updateAlertStatus((int) alertId, statusToUpdate);

        if (isUpdated) {
            // Tạo đối tượng DTO để log lại hành động 
            AlertActionDTO logEntry = new AlertActionDTO(
                    0, // actionID tự tăng trong DB
                    alertId,
                    actorId,
                    actionType,
                    (note == null || note.isEmpty()) ? "No note provided" : note,
                    java.time.LocalDateTime.now()
            );
            actionDao.insertAlertAction(logEntry);
        }

        // Điều hướng về trang cũ [cite: 14, 18]
        response.sendRedirect(request.getHeader("Referer"));
    }
}
