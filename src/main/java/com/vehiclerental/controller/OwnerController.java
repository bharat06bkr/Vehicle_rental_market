package com.vehiclerental.controller;

import com.vehiclerental.config.SecurityUtils;
import com.vehiclerental.dto.request.PickupRequest;
import com.vehiclerental.dto.request.ReturnRequest;
import com.vehiclerental.dto.request.VehicleRequest;
import com.vehiclerental.dto.response.ApiResponse;
import com.vehiclerental.dto.response.BookingResponse;
import com.vehiclerental.dto.response.VehicleResponse;
import com.vehiclerental.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner")
@PreAuthorize("hasAuthority('ROLE_OWNER')")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private SecurityUtils securityUtils;

    @PostMapping("/vehicles")
    public ResponseEntity<ApiResponse<VehicleResponse>> addVehicle(@Valid @RequestBody VehicleRequest request) {
        Long ownerId = securityUtils.getCurrentUserId();
        VehicleResponse response = ownerService.addVehicle(ownerId, request);
        return ResponseEntity.ok(ApiResponse.success("Vehicle submitted for Admin approval", response));
    }

    @PutMapping("/vehicles/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(
            @PathVariable Long id, @Valid @RequestBody VehicleRequest request) {
        Long ownerId = securityUtils.getCurrentUserId();
        VehicleResponse response = ownerService.updateVehicle(ownerId, id, request);
        return ResponseEntity.ok(ApiResponse.success("Vehicle updated successfully", response));
    }

    @GetMapping("/vehicles")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getMyVehicles() {
        Long ownerId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success("Vehicles retrieved", ownerService.getOwnerVehicles(ownerId)));
    }

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings() {
        Long ownerId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success("Booking requests retrieved", ownerService.getOwnerBookings(ownerId)));
    }

    @PutMapping("/bookings/{id}/accept")
    public ResponseEntity<ApiResponse<BookingResponse>> acceptBooking(@PathVariable Long id) {
        Long ownerId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success("Booking confirmed", ownerService.acceptBooking(ownerId, id)));
    }

    @PutMapping("/bookings/{id}/reject")
    public ResponseEntity<ApiResponse<BookingResponse>> rejectBooking(@PathVariable Long id) {
        Long ownerId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success("Booking rejected", ownerService.rejectBooking(ownerId, id)));
    }

    @PostMapping("/bookings/{id}/pickup")
    public ResponseEntity<ApiResponse<BookingResponse>> performPickup(
            @PathVariable Long id, @Valid @RequestBody PickupRequest request) {
        Long ownerId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success("Pickup recorded. Rental is now ACTIVE.", ownerService.performPickup(ownerId, id, request)));
    }

    @PostMapping("/bookings/{id}/return")
    public ResponseEntity<ApiResponse<BookingResponse>> performReturn(
            @PathVariable Long id, @Valid @RequestBody ReturnRequest request) {
        Long ownerId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success("Return recorded. Booking COMPLETED.", ownerService.performReturn(ownerId, id, request)));
    }
}
