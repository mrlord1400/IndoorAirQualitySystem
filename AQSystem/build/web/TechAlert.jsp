<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Operational Alerts</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3>System Alerts Management</h3>
            <a href="TechDashboard.jsp" class="btn btn-outline-secondary">Back to Dashboard</a>
        </div>

        <div class="card mb-3 py-2 px-3 shadow-sm">
            <form action="MainController" method="GET" class="row g-3 align-items-center">
                <input type="hidden" name="action" value="filterAlerts">
                <div class="col-auto">
                    <select name="status" class="form-select">
                        <option value="OPEN">New (OPEN)</option>
                        <option value="Acknowledged">In Progress (ACK)</option>
                        <option value="Closed">Resolved (CLOSED)</option>
                    </select>
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-primary">Apply Filter</button>
                </div>
            </form>
        </div>

        <div class="card shadow-sm">
            <div class="card-body p-0">
                <table class="table table-striped table-bordered mb-0">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Timestamp</th>
                            <th>Room & Sensor</th>
                            <th>Location</th>
                            <th>Status</th>
                            <th>Operation</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${alertList}">
                            <tr>
                                <td>${item.alertId}</td>
                                <td>${item.startTs}</td>
                                <td>
                                    <strong>${item.roomCode}</strong><br>
                                    <small class="text-muted">SN: ${item.sensorSerial}</small>
                                </td>
                                <td>${item.pollutantName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.status == 'OPEN'}"><span class="badge bg-danger">OPEN</span></c:when>
                                        <c:when test="${item.status == 'Acknowledged'}"><span class="badge bg-primary">ACK</span></c:when>
                                        <c:otherwise><span class="badge bg-success">CLOSED</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div class="btn-group">
                                        <c:if test="${item.status == 'OPEN'}">
                                            <button class="btn btn-sm btn-outline-primary" onclick="showActionModal(${item.alertId}, 'ack')">Acknowledge</button>
                                        </c:if>
                                        
                                        <c:if test="${item.status == 'Acknowledged'}">
                                            <button class="btn btn-sm btn-outline-success" onclick="showActionModal(${item.alertId}, 'close')">Close Alert</button>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="modal fade" id="actionModal" tabindex="-1">
        <div class="modal-dialog">
            <form action="AlertController" method="POST" class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Handling Alert #<span id="displayId"></span></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="alertId" id="inputAlertId">
                    <input type="hidden" name="action" id="inputAction">
                    <div class="mb-3">
                        <label class="form-label">Resolution Note:</label>
                        <textarea name="note" class="form-control" rows="3" placeholder="Ví dụ: Đã thay pin cảm biến, Đang thông gió..." required></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Confirm Action</button>
                </div>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function showActionModal(id, actionType) {
            document.getElementById('displayId').innerText = id;
            document.getElementById('inputAlertId').value = id;
            document.getElementById('inputAction').value = actionType;
            new bootstrap.Modal(document.getElementById('actionModal')).show();
        }
    </script>
</body>
</html>
