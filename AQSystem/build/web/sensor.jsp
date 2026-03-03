<%-- 
    Document   : sensor
    Created on : 18/02/2026, 1:21:08 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Sensor Management</title>
    <style>
        .stale-sensor { background-color: #ffcccc; color: #cc0000; font-weight: bold; }
        .active-sensor { background-color: #e6ffed; color: #28a745; }
        .status-badge { padding: 5px; border-radius: 4px; }
    </style>
</head>
<body>
    <h2>Sensor List & Health Reports</h2>

    <form action="SensorController" method="GET">
        <input type="hidden" name="action" value="healthReport">
        <button type="submit">Find the sensor that has lost connection (>30 minutes)</button>
    </form>

    <table border="1" cellpadding="10">
        <thead>
            <tr>
                <th>ID</th>
                <th>Room</th>
                <th>Serial Number</th>
                <th>Model</th>
                <th>Last Seen</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="sensor" items="${SENSOR_LIST}">
                <tr class="${!sensor.status ? 'stale-sensor' : ''}">
                    <td>${sensor.sensorID} </td>
                    <td>${sensor.roomID} </td>
                    <td>${sensor.serialNo} </td>
                    <td>${sensor.model} </td>
                    <td>${sensor.lastSeenTs} </td>
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
</body>
</html>

