<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Retrieve the stats map from the request attribute
    Map<String, Integer> stats = (Map<String, Integer>) request.getAttribute("STATS");

    // Safety check: if stats is null, initialize with 0 to avoid NullPointerException
    int totalUsers = (stats != null && stats.get("totalUsers") != null) ? stats.get("totalUsers") : 0;
    int totalRooms = (stats != null && stats.get("totalRooms") != null) ? stats.get("totalRooms") : 0;
    int totalSensors = (stats != null && stats.get("totalSensors") != null) ? stats.get("totalSensors") : 0;
    int totalPollutants = (stats != null && stats.get("totalPollutants") != null) ? stats.get("totalPollutants") : 0;
    int totalRules = (stats != null && stats.get("totalRules") != null) ? stats.get("totalRules") : 0;
    int openAlerts = (stats != null && stats.get("openAlerts") != null) ? stats.get("openAlerts") : 0;
%>
<html>
    <head>
        <title>Admin Dashboard - Air Quality Lab Care</title>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f0f2f5;
                margin: 0;
                padding: 20px;
            }
            .header {
                background: #1a252f;
                color: white;
                padding: 20px;
                text-align: center;
                border-radius: 4px;
                margin-bottom: 30px;
            }
            .dashboard-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
                gap: 25px;
                max-width: 1200px;
                margin: 0 auto;
            }
            .card {
                background: white;
                padding: 25px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                text-align: center;
            }
            .card h3 {
                margin: 0;
                color: #95a5a6;
                font-size: 13px;
                text-transform: uppercase;
                letter-spacing: 1px;
            }
            .card .value {
                font-size: 36px;
                font-weight: bold;
                color: #2c3e50;
                margin: 15px 0;
            }
            .card a {
                display: inline-block;
                text-decoration: none;
                color: #3498db;
                font-size: 14px;
                font-weight: 600;
                padding: 8px 16px;
                border: 1px solid #3498db;
                border-radius: 4px;
                transition: 0.2s;
            }
            .card a:hover {
                background: #3498db;
                color: white;
            }
            .alert-card {
                border-top: 5px solid #e74c3c;
            }
            .logout-btn {
                background: #95a5a6;
                color: white;
                border: none;
                padding: 10px 25px;
                border-radius: 4px;
                cursor: pointer;
            }
            .logout-btn:hover {
                background: #7f8c8d;
            }
        </style>
    </head>
    <body>

        <div class="header">
            <h1>ADMIN DASHBOARD</h1>
            <p>System Overview & Resource Management</p>
        </div>

        <div class="dashboard-grid">
            <div class="card">
                <h3>Registered Users</h3>
                <div class="value"><%= totalUsers%></div>
                <a href="users.jsp">Manage Users</a>
            </div>

            <div class="card">
                <h3>Facilities / Rooms</h3>
                <div class="value"><%= totalRooms%></div>
                <a href="rooms.jsp">Manage Rooms</a>
            </div>

            <div class="card">
                <h3>IoT Sensors</h3>
                <div class="value"><%= totalSensors%></div>
                <a href="SensorController">Manage Sensors</a>
            </div>

            <div class="card">
                <h3>Pollutants</h3>
                <div class="value"><%= totalPollutants%></div>
                <a href="pollutants.jsp">Manage Pollutants</a>
            </div>

            <div class="card">
                <h3>Threshold Rules</h3>
                <div class="value"><%= totalRules%></div>
                <a href="rules.jsp">Manage Rules</a>
            </div>

            <div class="card alert-card">
                <h3 style="color: #e74c3c;">Critical Alerts</h3>
                <div class="value" style="color: #e74c3c;"><%= openAlerts%></div>
                <a href="alerts.jsp" style="color: #e74c3c; border-color: #e74c3c;">View Alerts</a>
            </div>
        </div>

        <div style="margin-top: 50px; text-align: center;">
            <form action="MainController" method="POST">
                <button type="submit" name="action" value="Logout" class="logout-btn">Sign Out</button>
            </form>
        </div>

    </body>
</html>
