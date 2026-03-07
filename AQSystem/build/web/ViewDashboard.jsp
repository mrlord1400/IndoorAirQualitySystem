<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>IAQ Lab Care - Monitoring Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f4f4; color: #333; font-family: 'Segoe UI', Arial, sans-serif; }
        .nav-header { background: #2c3e50; color: white; padding: 1rem 0; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .kpi-box { background: white; padding: 20px; border-radius: 4px; border: 1px solid #ddd; text-align: center; }
        .kpi-value { font-size: 2rem; font-weight: bold; margin: 5px 0; }
        .main-card { background: white; padding: 20px; border-radius: 4px; border: 1px solid #ddd; height: 100%; }
        .table thead { background-color: #f8f9fa; }
        .alert-row { border-left: 4px solid #e74c3c; padding: 10px; margin-bottom: 10px; background: #fdf2f2; border-radius: 4px; }
        .text-small { font-size: 0.85rem; }
    </style>
</head>
<body>

<div class="nav-header mb-4">
    <div class="container d-flex justify-content-between align-items-center">
        <h4 class="mb-0">AIR QUALITY LAB CARE</h4>
        <div class="text-small">
            User: <strong>${sessionScope.user.fullName}</strong> 
            <span class="mx-2">|</span> 
            <a href="logout" class="text-white-50 text-decoration-none">Sign Out</a>
        </div>
    </div>
</div>

<div class="container">
    <div class="row g-3 mb-4">
        <div class="col-md-3">
            <div class="kpi-box shadow-sm">
                <div class="text-muted text-small fw-bold">ACTIVE ALERTS</div>
                <div class="kpi-value text-danger">${activeAlertCount}</div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="kpi-box shadow-sm">
                <div class="text-muted text-small fw-bold">ONLINE SENSORS</div>
                <div class="kpi-value text-primary">${onlineSensorCount}</div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="kpi-box shadow-sm">
                <div class="text-muted text-small fw-bold">ROOMS MONITORED</div>
                <div class="kpi-value text-dark">${totalRooms}</div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="kpi-box shadow-sm">
                <div class="text-muted text-small fw-bold">SYSTEM STATUS</div>
                <div class="kpi-value text-success">STABLE</div>
            </div>
        </div>
    </div>

    <div class="row g-4">
        <div class="col-lg-8">
            <div class="main-card shadow-sm">
                <h5 class="mb-3">Live Laboratory Status</h5>
                <div class="table-responsive">
                    <table class="table table-hover border">
                        <thead>
                            <tr class="text-secondary text-small text-uppercase">
                                <th>Room Name</th>
                                <th>Location</th>
                                <th class="text-center">CO2 (ppm)</th>
                                <th class="text-center">PM2.5 (ug/m3)</th>
                                <th>Latest Sync</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${roomSummaries}" var="rs">
                                <tr>
                                    <td><strong>${rs.roomName}</strong> <br><small class="text-muted">${rs.roomCode}</small></td>
                                    <td class="text-small">${rs.location}</td>
                                    <td class="text-center fw-bold ${rs.co2Value > 1000 ? 'text-danger' : 'text-dark'}">
                                        ${rs.co2Value != null ? rs.co2Value : '--'}
                                    </td>
                                    <td class="text-center fw-bold ${rs.pm25Value > 50 ? 'text-danger' : 'text-dark'}">
                                        ${rs.pm25Value != null ? rs.pm25Value : '--'}
                                    </td>
                                    <td class="text-small text-muted">
                                        <fmt:formatDate value="${rs.lastSeen}" pattern="yyyy-MM-dd HH:mm"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="col-lg-4">
            <div class="main-card shadow-sm">
                <h5 class="mb-3">Open Alerts</h5>
                <c:if test="${empty openAlerts}">
                    <div class="text-center py-5">
                        <p class="text-muted">No active alerts at this time.</p>
                    </div>
                </c:if>
                <c:forEach items="${openAlerts}" var="alert">
                    <div class="alert-row">
                        <div class="d-flex justify-content-between mb-1">
                            <span class="badge bg-danger text-uppercase" style="font-size: 0.7rem;">${alert.severity}</span>
                            <span class="text-muted text-small">
                                <fmt:formatDate value="${alert.startTs}" pattern="HH:mm"/>
                            </span>
                        </div>
                        <div class="fw-bold text-small">${alert.roomName}</div>
                        <div class="text-muted text-small">${alert.message}</div>
                    </div>
                </c:forEach>
                <div class="mt-3">
                    <a href="viewHistory" class="btn btn-sm btn-outline-secondary w-100">View History Log</a>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
