package com.vehiclerental.controller;

import com.vehiclerental.dto.request.CategoryRequest;
import com.vehiclerental.dto.request.LocationRequest;
import com.vehiclerental.dto.request.OwnerApprovalRequest;
import com.vehiclerental.dto.request.VehicleApprovalRequest;
import com.vehiclerental.dto.response.*;
import com.vehiclerental.enums.Role;
import com.vehiclerental.enums.VehicleApprovalStatus;
import com.vehiclerental.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("Users retrieved", adminService.getAllUsers()));
    }

    @GetMapping("/owners")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getOwners() {
        return ResponseEntity.ok(ApiResponse.success("Owners retrieved", adminService.getUsersByRole(Role.ROLE_OWNER)));
    }

    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getCustomers() {
        return ResponseEntity.ok(ApiResponse.success("Customers retrieved", adminService.getUsersByRole(Role.ROLE_CUSTOMER)));
    }

    @PutMapping("/owners/{id}/status")
    public ResponseEntity<ApiResponse<UserResponse>> updateOwnerStatus(
            @PathVariable Long id, @Valid @RequestBody OwnerApprovalRequest request) {
        UserResponse response = adminService.updateOwnerStatus(id, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success("Owner status updated to " + request.getStatus(), response));
    }

    @GetMapping("/vehicles/pending")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getPendingVehicles() {
        return ResponseEntity.ok(ApiResponse.success("Pending vehicles retrieved", adminService.getPendingVehicles()));
    }

    @PutMapping("/vehicles/{id}/approve")
    public ResponseEntity<ApiResponse<VehicleResponse>> approveVehicle(@PathVariable Long id) {
        VehicleResponse response = adminService.reviewVehicle(id, VehicleApprovalStatus.APPROVED, null);
        return ResponseEntity.ok(ApiResponse.success("Vehicle approved successfully", response));
    }

    @PutMapping("/vehicles/{id}/reject")
    public ResponseEntity<ApiResponse<VehicleResponse>> rejectVehicle(
            @PathVariable Long id, @RequestBody VehicleApprovalRequest request) {
        VehicleResponse response = adminService.reviewVehicle(id, VehicleApprovalStatus.REJECTED, request.getRejectionReason());
        return ResponseEntity.ok(ApiResponse.success("Vehicle rejected", response));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
        return ResponseEntity.ok(ApiResponse.success("Dashboard statistics retrieved", adminService.getDashboardStats()));
    }

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Category created", adminService.createCategory(request)));
    }

    @PostMapping("/locations")
    public ResponseEntity<ApiResponse<LocationResponse>> createLocation(@Valid @RequestBody LocationRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Location created", adminService.createLocation(request)));
    }
}
