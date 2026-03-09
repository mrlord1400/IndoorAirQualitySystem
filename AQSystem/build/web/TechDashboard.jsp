<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Technician Dashboard | AirQuality LabCare</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-bold" href="MainController?action=Dashboard">TECH-LAB CARE</a>
            <div class="ms-auto d-flex align-items-center">
                <span class="navbar-text text-white me-3">
                    Welcome, <span class="text-info fw-bold"><c:out value="${sessionScope.LOGIN_USER.username}" default="User"/></span>
                </span>
                <a href="MainController?action=Login" class="btn btn-sm btn-outline-danger">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <c:if test="${not empty sessionScope.MSG_SUCCESS}">
            <div class="alert alert-success alert-dismissible fade show shadow-sm">
                <c:out value="${sessionScope.MSG_SUCCESS}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <% session.removeAttribute("MSG_SUCCESS"); %>
        </c:if>

        <div class="row g-4">
            <div class="col-md-3">
                <div class="card border-0 shadow-sm">
                    <div class="list-group list-group-flush rounded">
                        <div class="list-group-item bg-secondary text-white fw-bold py-3">MENU</div>
                        <%-- Khớp với các hằng số trong MainController của bạn --%>
                        <a href="MainController?action=Dashboard" class="list-group-item list-group-item-action active">
                            Overview Dashboard
                        </a>
                        <a href="MainController?action=Alert" class="list-group-item list-group-item-action">
                            Alert Management
                        </a>
                        <a href="MainController?action=Sensor" class="list-group-item list-group-item-action">
                            Sensor Maintenance
                        </a>
                        <a href="MainController?action=Reading" class="list-group-item list-group-item-action">
                            Air Quality Logs
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-md-9">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h4 class="fw-bold">Operational Overview</h4>
                </div>

                <div class="row g-3 mb-4">
                    <div class="col-md-6">
                        <div class="card bg-white border-start border-danger border-4 shadow-sm">
                            <div class="card-body">
                                <h6 class="text-muted small text-uppercase">Urgent Alerts</h6>
                                <h2 class="fw-bold text-danger"><c:out value="${requestScope.OPEN_ALERTS_COUNT}" default="0"/></h2>
                                <a href="MainController?action=Alert" class="small text-decoration-none">View all active alerts &rarr;</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="card bg-white border-start border-info border-4 shadow-sm">
                            <div class="card-body">
                                <h6 class="text-muted small text-uppercase">System Health</h6>
                                <h2 class="fw-bold text-info">Stable</h2>
                                <a href="MainController?action=Sensor" class="small text-decoration-none">Check sensor status &rarr;</a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="card border-0 shadow-sm">
                    <div class="card-header bg-white py-3">
                        <h5 class="mb-0 fw-bold">Recent Tasks</h5>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light text-secondary">
                                <tr>
                                    <th>ID</th>
                                    <th>Location</th>
                                    <th>Severity</th>
                                    <th>Message</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${requestScope.ALERTS_LIST}">
                                    <tr>
                                        <td>#${item.alertId}</td>
                                        <td><strong>${item.roomCode}</strong></td>
                                        <td>
                                            <span class="badge ${item.severity == 'HIGH' ? 'bg-danger' : 'bg-warning'}">
                                                ${item.severity}
                                            </span>
                                        </td>
                                        <td class="small">${item.message}</td>
                                        <td>
                                            <%-- Gửi trực tiếp qua AlertController để xử lý --%>
                                            <a href="AlertController?alertId=${item.alertId}&action=ack" 
                                               class="btn btn-sm btn-primary">Acknowledge</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty requestScope.ALERTS_LIST}">
                                    <tr>
                                        <td colspan="5" class="text-center py-5 text-muted italic">
                                            No pending alerts found in your area.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
