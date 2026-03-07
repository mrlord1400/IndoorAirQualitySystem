/*package controller;

import model.ReadingDAO;
import model.ReadingDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ReadingController", urlPatterns = {"/reading-controller"})
public class ReadingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Lấy tham số tìm kiếm từ giao diện
        String rIdStr = request.getParameter("roomId");
        String pIdStr = request.getParameter("pollutantId");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");

        // 2. Xử lý logic phân trang (Pagination)
        int pageIndex = 1; 
        int pageSize = 20; // Số dòng hiển thị mỗi trang
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            pageIndex = Integer.parseInt(pageStr);
        }

        // Chuyển đổi kiểu dữ liệu cho DAO
        Integer roomId = (rIdStr != null && !rIdStr.isEmpty()) ? Integer.parseInt(rIdStr) : null;
        Integer pollutantId = (pIdStr != null && !pIdStr.isEmpty()) ? Integer.parseInt(pIdStr) : null;

        ReadingDAO dao = new ReadingDAO();

        // 3. Thực hiện truy vấn dữ liệu theo trang
        List<ReadingDTO> list = dao.searchAdvancedWithPaging(roomId, pollutantId, fromDate, toDate, pageIndex, pageSize);
        
        // 4. Tính toán tổng số trang để hiển thị thanh điều hướng
        int totalRecords = dao.getTotalRecords(roomId, pollutantId, fromDate, toDate);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // 5. Gửi dữ liệu sang JSP
        request.setAttribute("READINGS_LIST", list);
        request.setAttribute("TOTAL_PAGES", totalPages);
        request.setAttribute("CURRENT_PAGE", pageIndex);
        
        // Giả sử trang hiển thị của bạn là reading-list.jsp hoặc search.jsp
        request.getRequestDispatcher("reading-list.jsp").forward(request, response);
    }
}
