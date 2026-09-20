package com.vehiclerental.dto.request;

import com.vehiclerental.enums.UserStatus;
import jakarta.validation.constraints.NotNull;

public class OwnerApprovalRequest {

    @NotNull(message = "User status is required (APPROVED, REJECTED, or SUSPENDED)")
    private UserStatus status;

    public OwnerApprovalRequest() {}

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
}
