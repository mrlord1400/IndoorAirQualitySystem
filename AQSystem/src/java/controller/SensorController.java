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

    private static final String SENSOR_PAGE = "sensor.jsp";
    private static final String MAIN_SENSOR_CONTROL = "MainController?action=Sensor";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String subAction = request.getParameter("subAction"); // Lấy subAction
        SensorDAO dao = new SensorDAO();
        String url = "sensor.jsp";
        boolean isRedirect = false;

        try ( Connection conn = DBUtils.getConnection()) {
            if ("filterByRoom".equals(subAction)) {
                int roomID = Integer.parseInt(request.getParameter("roomID"));
                request.setAttribute("SENSOR_LIST", dao.getSensorsByRoom(roomID, conn));
            } else if ("add".equals(subAction)) {
                int roomID = Integer.parseInt(request.getParameter("roomID"));
                String serialNo = request.getParameter("serialNo");
                String model = request.getParameter("model");
                boolean status = Boolean.parseBoolean(request.getParameter("status"));

                SensorDTO sensor = new SensorDTO(0, roomID, serialNo, model, status, LocalDateTime.now(), null);

                if (dao.addSensor(sensor, conn)) {
                    // Thành công thì nhảy về trang danh sách tổng
                    response.sendRedirect("MainController?action=Sensor");
                    isRedirect = true;
                }
            } else {
                // Mặc định load hết
                request.setAttribute("SENSOR_LIST", dao.getAllSensors(conn));
            }
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        } finally {
            if (!isRedirect) {
                request.getRequestDispatcher(url).forward(request, response);
            }
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
