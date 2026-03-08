<%@page import="java.util.List"%>
<%@page import="model.ReadingDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Air Quality Data</title>
        <style>
            table { width: 100%; border-collapse: collapse; margin-top: 20px; }
            th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
            th { background-color: #f2f2f2; }
            .btn-export { 
                display: inline-block; 
                padding: 10px 20px; 
                background-color: #28a745; 
                color: white; 
                text-decoration: none; 
                border-radius: 5px; 
                margin-bottom: 15px;
            }
            .btn-export:hover { background-color: #218838; }
        </style>
    </head>
    <body>
        <h1>Dashboard Theo Dõi Không Khí</h1>

        <a href="ExportController" class="btn-export">ExportCSV</a>

        <table>
            <thead>
                <tr>
                    <th>Reading ID</th>
                    <th>Sensor ID</th>
                    <th>Pollutant ID</th>
                    <th>Timestamp</th>
                    <th>Value</th>
                    <th>Quality Flag</th>
                    <th>Created At</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${listReading}" var="r">
                    <tr>
                        <td>${r.readingID}</td>
                        <td>${r.sensorID}</td>
                        <td>${r.pollutantID}</td>
                        <td>${r.ts}</td>
                        <td>${r.value}</td>
                        <td>${r.qualityFlag}</td>
                        <td>${r.createdAt}</td>
                    </tr>
                </c:forEach>
                
                <c:if test="${empty listReading}">
                    <tr>
                        <td colspan="7" style="text-align: center;">Chưa có dữ liệu. Vui lòng nhấn Search hoặc đợi Robot nạp dữ liệu.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </body>
</html>
