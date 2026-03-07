<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Air Quality Lab Care - View Readings</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        .pagination { margin-top: 20px; }
        .table-container { margin-top: 30px; }
    </style>
</head>
<body class="container mt-4">

    <h2 class="mb-4 text-primary">Air Quality History Data</h2>

    <div class="card card-body bg-light mb-4">
        <form action="reading-controller" method="GET" class="row g-3">
            <div class="col-md-2">
                <label class="form-label font-weight-bold">Room ID</label>
                <input type="number" name="roomId" class="form-control" placeholder="Ex: 1" value="${param.roomId}">
            </div>
            <div class="col-md-2">
                <label class="form-label font-weight-bold">Pollutant ID</label>
                <input type="number" name="pollutantId" class="form-control" placeholder="Ex: 1" value="${param.pollutantId}">
            </div>
            <div class="col-md-3">
                <label class="form-label font-weight-bold">From Date</label>
                <input type="datetime-local" name="fromDate" class="form-control" value="${param.fromDate}">
            </div>
            <div class="col-md-3">
                <label class="form-label font-weight-bold">To Date</label>
                <input type="datetime-local" name="toDate" class="form-control" value="${param.toDate}">
            </div>
            <div class="col-md-2 d-flex align-items-end">
                <button type="submit" class="btn btn-primary w-100">Search</button>
            </div>
        </form>
    </div>

    <div class="table-container">
        <table class="table table-hover table-bordered shadow-sm">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Sensor ID</th>
                    <th>Pollutant</th>
                    <th>Timestamp</th>
                    <th>Value</th>
                    <th>Quality Flag</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${READINGS_LIST}" var="r">
                    <tr>
                        <td>${r.readingID}</td>
                        <td>${r.sensorID}</td>
                        <td>${r.pollutantID}</td>
                        <td>${r.ts}</td>
                        <td class="fw-bold text-primary">${r.value}</td>
                        <td>
                            <c:choose>
                                <c:when test="${r.qualityFlag == 'NORMAL'}">
                                    <span class="badge bg-success">NORMAL</span>
                                </c:when>
                                <c:when test="${r.qualityFlag == 'WARNING'}">
                                    <span class="badge bg-warning text-dark">WARNING</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger">${r.qualityFlag}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                
                <c:if test="${empty READINGS_LIST}">
                    <tr>
                        <td colspan="6" class="text-center text-muted">No readings found for the selected criteria.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <nav aria-label="Page navigation">
        <ul class="pagination justify-content-center">
            <c:forEach begin="1" end="${TOTAL_PAGES}" var="i">
                <li class="page-item ${i == CURRENT_PAGE ? 'active' : ''}">
                    <a class="page-link" 
                       href="reading-controller?page=${i}&roomId=${param.roomId}&pollutantId=${param.pollutantId}&fromDate=${param.fromDate}&toDate=${param.toDate}">
                       ${i}
                    </a>
                </li>
            </c:forEach>
        </ul>
    </nav>

</body>
</html>
