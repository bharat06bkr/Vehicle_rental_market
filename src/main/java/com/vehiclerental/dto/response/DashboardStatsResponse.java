package com.vehiclerental.dto.response;

public class DashboardStatsResponse {
    private long totalUsers;
    private long totalOwners;
    private long pendingOwners;
    private long totalCustomers;
    private long totalVehicles;
    private long pendingVehicles;
    private long approvedVehicles;
    private long activeRentals;
    private long completedBookings;

    public DashboardStatsResponse() {}

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalOwners() { return totalOwners; }
    public void setTotalOwners(long totalOwners) { this.totalOwners = totalOwners; }

    public long getPendingOwners() { return pendingOwners; }
    public void setPendingOwners(long pendingOwners) { this.pendingOwners = pendingOwners; }

    public long getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(long totalCustomers) { this.totalCustomers = totalCustomers; }

    public long getTotalVehicles() { return totalVehicles; }
    public void setTotalVehicles(long totalVehicles) { this.totalVehicles = totalVehicles; }

    public long getPendingVehicles() { return pendingVehicles; }
    public void setPendingVehicles(long pendingVehicles) { this.pendingVehicles = pendingVehicles; }

    public long getApprovedVehicles() { return approvedVehicles; }
    public void setApprovedVehicles(long approvedVehicles) { this.approvedVehicles = approvedVehicles; }

    public long getActiveRentals() { return activeRentals; }
    public void setActiveRentals(long activeRentals) { this.activeRentals = activeRentals; }

    public long getCompletedBookings() { return completedBookings; }
    public void setCompletedBookings(long completedBookings) { this.completedBookings = completedBookings; }
}
