<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Lab Manager Dashboard | AirQuality LabCare</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <style>
            body {
                background-color: #f4f7f6;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }
            .sidebar {
                min-height: 100vh;
                background: #2c3e50;
                color: white;
            }
            .sidebar .nav-link {
                color: #bdc3c7;
                transition: 0.3s;
                margin: 5px 0;
            }
            .sidebar .nav-link:hover, .sidebar .nav-link.active {
                color: white;
                background: #34495e;
                border-radius: 5px;
            }
            .card {
                border: none;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0,0,0,0.1);
                margin-bottom: 25px;
            }
            .card-header {
                background-color: #fff;
                border-bottom: 1px solid #eee;
                font-weight: bold;
            }
            .stat-card {
                transition: transform 0.2s;
            }
            .stat-card:hover {
                transform: translateY(-5px);
            }
        </style>
    </head>
    <body>

        <div class="container-fluid">
            <div class="row">
                <nav class="col-md-2 d-none d-md-block sidebar p-3">
                    <div class="text-center mb-4">
                        <h4 class="text-info"><i class="fas fa-wind"></i> LabCare AI</h4>
                        <hr class="bg-light">
                    </div>
                    <ul class="nav flex-column">
                        <li class="nav-item"><a class="nav-link active" href="#"><i class="fas fa-tachometer-alt me-2"></i> Dashboard</a></li>
                        <li class="nav-item"><a class="nav-link" href="#alertsSection"><i class="fas fa-exclamation-circle me-2"></i> Active Alerts</a></li>
                        <li class="nav-item"><a class="nav-link" href="#rulesSection"><i class="fas fa-shield-alt me-2"></i> Threshold Rules</a></li>
                        <li class="nav-item"><a class="nav-link" href="#"><i class="fas fa-microscope me-2"></i> Managed Rooms</a></li>
                    </ul>
                </nav>

                <main class="col-md-10 ms-sm-auto px-md-4">
                    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                        <h1 class="h2 text-secondary">Lab Manager Dashboard</h1>
                        <div class="btn-toolbar mb-2 mb-md-0">
                            <span class="me-3 align-self-center text-muted">Welcome, <strong>${sessionScope.username != null ? sessionScope.username : 'Manager'}</strong></span>
                            <a href="logout" class="btn btn-outline-danger btn-sm"><i class="fas fa-sign-out-alt"></i> Sign Out</a>
                        </div>
                    </div>

                    <div class="row text-center mb-2">
                        <div class="col-md-4">
                            <div class="card stat-card border-start border-danger border-5 p-3">
                                <div class="text-muted small text-uppercase fw-bold">Active Alerts</div>
                                <div class="h3 fw-bold text-danger">3</div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="card stat-card border-start border-primary border-5 p-3">
                                <div class="text-muted small text-uppercase fw-bold">Managed Rooms</div>
                                <div class="h3 fw-bold text-primary">5</div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="card stat-card border-start border-success border-5 p-3">
                                <div class="text-muted small text-uppercase fw-bold">Active Sensors</div>
                                <div class="h3 fw-bold text-success">12</div>
                            </div>
                        </div>
                    </div>

                    <div id="alertsSection">
                        <div class="card">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <span><i class="fas fa-bell text-warning me-2"></i> Real-time System Alerts</span>
                                <button class="btn btn-sm btn-link text-decoration-none">View History</button>
                            </div>
                            <div class="card-body p-0">
                                <jsp:include page="LMAlert.jsp" />
                            </div>
                        </div>
                    </div>

                    <div id="rulesSection">
                        <div class="card">
                            <div class="card-header">
                                <i class="fas fa-tasks text-primary me-2"></i> Safety Threshold Configurations
                            </div>
                            <div class="card-body p-0">
                                <jsp:include page="rule.jsp" />
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
