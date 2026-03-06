package controller;

import model.RoomDAO;
import model.RoomDTO;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

@WebServlet(name = "RoomController", urlPatterns = {"/rooms"})
public class RoomController extends HttpServlet {

    // Lấy DataSource từ server context [cite: 60]
    @Resource(name = "jdbc/AirQualityLabCare")
    private DataSource dataSource;
    private RoomDAO roomDAO;

    @Override
    public void init() throws ServletException {
        roomDAO = new RoomDAO(dataSource);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Lấy danh sách phòng và đẩy sang JSP [cite: 23, 105]
        List<RoomDTO> list = roomDAO.getAllRooms();
        request.setAttribute("listRooms", list);
        request.getRequestDispatcher("rooms.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            // Xử lý logic thêm phòng [cite: 22]
            String code = request.getParameter("code");
            String name = request.getParameter("name");
            // ... lấy các tham số khác và gọi roomDAO.insertRoom(...)
        } else if ("delete".equals(action)) {
            // Xử lý logic xóa mềm 
            int id = Integer.parseInt(request.getParameter("id"));
            roomDAO.deleteRoom(id);
        }
        
        // Điều hướng lại về trang danh sách sau khi thay đổi dữ liệu
        response.sendRedirect("rooms");
    }
}
