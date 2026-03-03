/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    // Trong SensorController.java 

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        SensorDAO dao = new SensorDAO(); // 

        if ("healthReport".equals(action)) {
            // Tìm các sensor không hoạt động > 30 phút
            List<SensorDTO> unhealthySensors = dao.getInactiveSensors(30);

            request.setAttribute("SENSOR_LIST", unhealthySensors);
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response); // [cite: 8]
        }
    }
}

