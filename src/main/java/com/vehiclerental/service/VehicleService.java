package com.vehiclerental.service;

import com.vehiclerental.dto.response.VehicleResponse;
import com.vehiclerental.entity.Vehicle;
import com.vehiclerental.enums.VehicleApprovalStatus;
import com.vehiclerental.enums.VehicleAvailabilityStatus;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.repository.ReviewRepository;
import com.vehiclerental.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<VehicleResponse> searchApprovedVehicles(Long locationId, Long categoryId, BigDecimal maxPrice, String query) {
        String searchQuery = (query != null && !query.isBlank()) ? query.trim() : null;
        List<Vehicle> vehicles = vehicleRepository.searchApprovedVehicles(locationId, categoryId, maxPrice, searchQuery);

        return vehicles.stream()
                .map(v -> new VehicleResponse(v, reviewRepository.getAverageRatingForVehicle(v.getId())))
                .collect(Collectors.toList());
    }

    public VehicleResponse getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));

        Double avgRating = reviewRepository.getAverageRatingForVehicle(id);
        return new VehicleResponse(vehicle, avgRating);
    }
}
