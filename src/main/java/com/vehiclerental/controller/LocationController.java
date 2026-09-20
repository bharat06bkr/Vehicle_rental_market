package com.vehiclerental.controller;

import com.vehiclerental.dto.response.ApiResponse;
import com.vehiclerental.dto.response.LocationResponse;
import com.vehiclerental.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LocationResponse>>> getAllLocations() {
        return ResponseEntity.ok(ApiResponse.success("Locations retrieved", adminService.getAllLocations()));
    }
}
