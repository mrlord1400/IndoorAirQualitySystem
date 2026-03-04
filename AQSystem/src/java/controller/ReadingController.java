package controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ReadingDAO;
import model.ReadingDTO;

@WebServlet("/export") 
public class ReadingController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1.  thông tin phản hồi để trình duyệt hiểu đây là file CSV tải về
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"AirQualityData.csv\"");

        // 2. Lấy dữ liệu từ Database thông qua DAO
        ReadingDAO dao = new ReadingDAO();
        List<ReadingDTO> data = dao.getAllReadings();

        // 3. Dùng PrintWriter để "ghi" dữ liệu trực tiếp lên luồng tải của trình duyệt
        try (PrintWriter writer = response.getWriter()) {
            
            writer.println("ID,SensorID,PollutantID,Timestamp,Value,QualityFlag");

            for (ReadingDTO r : data) {
                StringBuilder sb = new StringBuilder();
                sb.append(r.getReadingID()).append(",")
                  .append(r.getSensorID()).append(",")
                  .append(r.getPollutantID()).append(",")
                  .append(r.getTs()).append(",")
                  .append(r.getValue()).append(",")
                  .append(r.getQualityFlag());
                
                writer.println(sb.toString()); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
