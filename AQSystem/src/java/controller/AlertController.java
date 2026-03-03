/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AlertActionDAO;
import model.AlertActionDTO;
import model.AlertDAO;
import model.UserDTO;

/**
 *
 * @author Admin
 */
public class AlertController {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long alertId = Long.parseLong(request.getParameter("alertId"));
        String action = request.getParameter("action"); // Giá trị "ack" hoặc "close"
        String note = request.getParameter("note"); // Lấy ghi chú từ giao diện (nếu có)

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

        response.sendRedirect(request.getHeader("Referer"));
    }

}
