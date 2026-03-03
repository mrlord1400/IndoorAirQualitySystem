<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Dashboard</title>
        <style>
            body { font-family: Arial, sans-serif; display: flex; margin: 0; }
            .sidebar { width: 250px; height: 100vh; background: #333; color: white; padding: 20px; }
            .sidebar a { color: white; text-decoration: none; display: block; padding: 10px 0; border-bottom: 1px solid #444; }
            .sidebar a:hover { color: #ffcc00; }
            .main-content { flex-grow: 1; padding: 20px; background: #f4f4f4; }
            
            /* CSS cho bảng Sensor mượn từ file cũ của bạn */
            .stale-sensor { background-color: #ffcccc; color: #cc0000; font-weight: bold; }
            .status-badge { padding: 5px; border-radius: 4px; font-size: 12px; }
            .active-sensor { background-color: #e6ffed; color: #28a745; }
            table { width: 100%; border-collapse: collapse; background: white; }
        </style>
    </head>
    <body>

        <div class="sidebar">
            <h2>Admin Panel</h2>
            <a href="adminDashboard.jsp">🏠 Dashboard Home</a>
            
            <a href="SensorController?action=healthReport">📡 Sensor Management</a>
            
            <a href="LogoutController">🚪 Logout</a>
        </div>

        <div class="main-content">
            <h1>System Overview</h1>
            <hr>

            <c:if test="${not empty SENSOR_LIST}">
                <h3>List of Inactive Sensors (>30 mins)</h3>
                <table border="1" cellpadding="10">
                    <thead>
                        <tr style="background: #eee;">
                            <th>ID</th>
                            <th>Room</th>
                            <th>Serial Number</th>
                            <th>Last Seen</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="sensor" items="${SENSOR_LIST}">
                            <tr class="${!sensor.status ? 'stale-sensor' : ''}">
                                <td>${sensor.sensorID}</td>
                                <td>${sensor.roomID}</td>
                                <td>${sensor.serialNo}</td>
                                <td>${sensor.lastSeenTs}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${sensor.status}">
                                            <span class="status-badge active-sensor">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status-badge stale-sensor">Lost signal</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <c:if test="${empty SENSOR_LIST}">
                <p>Welcome back, Admin. Select a function from the sidebar to start.</p>
            </c:if>
        </div>

    </body>
</html>