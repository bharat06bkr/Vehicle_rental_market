package com.vehiclerental.controller;

import com.vehiclerental.dto.response.ApiResponse;
import com.vehiclerental.dto.response.ReviewResponse;
import com.vehiclerental.dto.response.VehicleResponse;
import com.vehiclerental.service.ReviewService;
import com.vehiclerental.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> searchVehicles(
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String search) {
        List<VehicleResponse> vehicles = vehicleService.searchApprovedVehicles(locationId, categoryId, maxPrice, search);
        return ResponseEntity.ok(ApiResponse.success("Vehicles retrieved", vehicles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Vehicle details retrieved", vehicleService.getVehicleById(id)));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getVehicleReviews(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Reviews retrieved", reviewService.getVehicleReviews(id)));
    }
}
