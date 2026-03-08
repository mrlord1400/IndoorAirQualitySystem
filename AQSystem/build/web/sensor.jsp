<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Sensor Management</title>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                margin: 30px;
                background-color: #f8f9fa;
            }
            .container {
                max-width: 1100px;
                margin: auto;
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }
            table {
                border-collapse: collapse;
                width: 100%;
                margin-top: 20px;
            }
            th, td {
                border: 1px solid #dee2e6;
                padding: 12px;
                text-align: left;
            }
            th {
                background-color: #007bff;
                color: white;
            }
            .form-section {
                background: #e9ecef;
                padding: 20px;
                border-radius: 5px;
                margin-bottom: 25px;
            }
            .btn-add {
                background-color: #28a745;
                color: white;
                border: none;
                padding: 10px 20px;
                cursor: pointer;
                border-radius: 4px;
            }
            .btn-view {
                background-color: #17a2b8;
                color: white;
                text-decoration: none;
                padding: 8px 15px;
                border-radius: 4px;
                display: inline-block;
            }
        </style>
    </head>
    <body>

        <div class="container">
            <h2>Sensor Management System</h2>

            <%-- Thông báo lỗi/Thành công --%>
            <c:if test="${not empty ERROR}"><p style="color:red;">${ERROR}</p></c:if>

                <div class="form-section">
                    <h3>Quick Filters</h3>
                    <form action="MainController" method="get" style="display:inline-block;">
                        <input type="hidden" name="action" value="Sensor">
                        <input type="hidden" name="subAction" value="filterByRoom">
                        Room ID: <input type="number" name="roomID" required style="width: 60px;">
                        <button type="submit">Filter</button>
                    </form>

                    <a href="MainController?action=Sensor" class="btn-view" style="margin-left: 20px;">View All Sensors</a>
                </div>

                <div class="form-section" style="background-color: #d4edda;">
                    <h3>Add New Sensor</h3>
                    <form action="MainController" method="post">
                        <input type="hidden" name="action" value="Sensor">
                        <input type="hidden" name="subAction" value="add">

                        Room ID: <input type="number" name="roomID" required style="width: 50px;">
                        Serial: <input type="text" name="serialNo" required placeholder="SN-123...">
                        Model: <input type="text" name="model" required placeholder="MQ-135...">
                        Status: 
                        <select name="status">
                            <option value="true">Active</option>
                            <option value="false">Inactive</option>
                        </select>
                        <button type="submit" class="btn-add">Add Sensor</button>
                    </form>
                </div>

                <h3>Sensor List</h3>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Room</th>
                            <th>Serial Number</th>
                            <th>Model</th>
                            <th>Status</th>
                            <th>Installed Date</th>
                            <th>Last Seen</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="s" items="${SENSOR_LIST}">
                        <tr>
                            <td>${s.sensorID}</td>
                            <td>Room ${s.roomID}</td>
                            <td><strong>${s.serialNo}</strong></td>
                            <td>${s.model}</td>
                            <td>
                                <span style="color: ${s.status ? 'green' : 'red'}; font-weight: bold;">
                                    ${s.status ? 'ONLINE' : 'OFFLINE'}
                                </span>
                            </td>
                            <td>${s.installedAt}</td>
                            <td>${s.lastSeenTs}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty SENSOR_LIST}">
                        <tr><td colspan="7" style="text-align:center;">No data found.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>

    </body>
</html>
