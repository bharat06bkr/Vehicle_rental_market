package com.vehiclerental.dto.request;

import com.vehiclerental.enums.VehicleApprovalStatus;
import jakarta.validation.constraints.NotNull;

public class VehicleApprovalRequest {

    @NotNull(message = "Approval status is required (APPROVED or REJECTED)")
    private VehicleApprovalStatus status;

    private String rejectionReason;

    public VehicleApprovalRequest() {}

    public VehicleApprovalStatus getStatus() { return status; }
    public void setStatus(VehicleApprovalStatus status) { this.status = status; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
