<%-- LMAlert.jsp --%>
<div class="table-responsive">
    <table class="table align-middle mb-0">
        <thead class="table-dark">
            <tr>
                <th class="ps-3">Timestamp</th>
                <th>Room</th>
                <th>Issue</th>
                <th>Severity</th>
                <th>Status</th>
                <th class="text-center">Action</th>
            </tr>
        </thead>
        <tbody>
            <%-- 
               Assuming you are looping through a list of AlertDTOs named 'alertList' 
               For demonstration, I'm using the logic for one row:
            --%>
            <tr>
                <td class="ps-3 small text-muted">2026-03-05 14:22</td>
                <td><strong>LAB-01</strong></td>
                <td>CO2 levels exceeded safety threshold</td>
                <td><span class="badge bg-danger">HIGH</span></td>
                <td>
                    <%-- Display status with colors --%>
                    <span class="fw-bold ${alert.status == 'OPEN' ? 'text-danger' : 'text-warning'}">
                        <i class="fas fa-circle-dot me-1"></i>${alert.status}
                    </span>
                </td>
                <td class="text-center">
                    <div class="btn-group">
                        <%-- Show ACK button only if status is OPEN --%>
                        <button type="button" class="btn btn-sm btn-warning fw-bold" 
                                onclick="openActionModal('${alert.alertId}', 'acknowledge', 'ACK')">
                            ACK
                        </button>

                        <%-- Show CLOSE button if status is OPEN or ACK --%>
                        <button type="button" class="btn btn-sm btn-success fw-bold" 
                                onclick="openActionModal('${alert.alertId}', 'close', 'CLOSE')">
                            CLOSE
                        </button>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
</div>

<div class="modal fade" id="alertActionModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="AlertController" method="POST">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalTitle">Process Alert</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="alertId" id="modalAlertId">
                    <input type="hidden" name="action" id="modalActionType">

                    <div class="mb-3">
                        <label class="form-label fw-bold">Note / Action Taken:</label>
                        <textarea name="note" class="form-control" rows="3" placeholder="Enter what was done to resolve this..." required></textarea>
                        <div class="form-text">This note will be saved in the Alert History.</div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" id="submitBtn" class="btn btn-primary">Confirm Action</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    /**
     * Opens the modal and populates hidden fields for the AlertController
     * @param {number} id - The alertId
     * @param {string} action - 'acknowledge' or 'close' (matches your Controller logic)
     * @param {string} label - Display text for the UI
     */
    function openActionModal(id, action, label) {
        document.getElementById('modalAlertId').value = id;
        document.getElementById('modalActionType').value = action;
        document.getElementById('modalTitle').innerText = label + ' Alert #' + id;

        // Change button color based on action
        const submitBtn = document.getElementById('submitBtn');
        if (action === 'close') {
            submitBtn.className = 'btn btn-success';
            submitBtn.innerText = 'Confirm Close';
        } else {
            submitBtn.className = 'btn btn-warning text-dark';
            submitBtn.innerText = 'Confirm Acknowledge';
        }

        // Show the modal
        var myModal = new bootstrap.Modal(document.getElementById('alertActionModal'));
        myModal.show();
    }
</script>
