<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sensor Management</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; line-height: 1.6; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        .container { max-width: 1000px; margin: auto; }
        .form-section { background: #f4f4f4; padding: 15px; border-radius: 5px; margin-bottom: 20px; }
        .error { color: red; font-weight: bold; }
        .message { color: green; font-weight: bold; }
        .btn { padding: 5px 10px; cursor: pointer; }
        .inline-form { display: inline-block; margin-right: 15px; }
    </style>
</head>
<body>

<div class="container">
    <h2>Sensor Management System</h2>

    <%-- Display Error or Success Messages --%>
    <c:if test="${not empty ERROR}">
        <p class="error">${ERROR}</p>
    </c:if>
    <c:if test="${not empty MESSAGE}">
        <p class="message">${MESSAGE}</p>
    </c:if>

    <div class="form-section">
        <h3>Filters & Lookup</h3>
        
        <form action="SensorController" method="get" class="inline-form">
            <input type="hidden" name="action" value="filterByRoom">
            Room ID: <input type="number" name="roomID" required style="width: 60px;">
            <button type="submit" class="btn">Filter by Room</button>
        </form>

        <form action="SensorController" method="get" class="inline-form">
            <input type="hidden" name="action" value="checkInactive">
            Minutes: <input type="number" name="threshold" required style="width: 60px;">
            <button type="submit" class="btn">Check Offline</button>
        </form>

        <a href="SensorController?action=list"><button class="btn">View All</button></a>
    </div>

    <div class="form-section">
        <h3>Add New Sensor</h3>
        <form action="SensorController" method="post">
            <input type="hidden" name="action" value="add">
            Room ID: <input type="number" name="roomID" required style="width: 50px;">
            Serial No: <input type="text" name="serialNo" required>
            Model: <input type="text" name="model" required>
            Status: 
            <select name="status">
                <option value="true">Active</option>
                <option value="false">Inactive</option>
            </select>
            <button type="submit" class="btn" style="background-color: #4CAF50; color: white; border: none;">Add Sensor</button>
        </form>
    </div>

    <h3>Sensor List</h3>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Room ID</th>
                <th>Serial No</th>
                <th>Model</th>
                <th>Status</th>
                <th>Installed At</th>
                <th>Last Seen</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty SENSOR_LIST}">
                    <c:forEach var="s" items="${SENSOR_LIST}">
                        <tr>
                            <td>${s.sensorID}</td>
                            <td>${s.roomID}</td>
                            <td>${s.serialNo}</td>
                            <td>${s.model}</td>
                            <td>
                                <span style="font-weight: bold; color: ${s.status ? 'green' : 'red'}">
                                    ${s.status ? 'Active' : 'Offline'}
                                </span>
                            </td>
                            <td>${s.installedAt}</td>
                            <td>${s.lastSeenTs}</td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="7" style="text-align: center;">No sensor data found.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>

</body>
</html>
