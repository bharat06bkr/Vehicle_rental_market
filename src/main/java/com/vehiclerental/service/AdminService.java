package com.vehiclerental.service;

import com.vehiclerental.dto.request.CategoryRequest;
import com.vehiclerental.dto.request.LocationRequest;
import com.vehiclerental.dto.response.*;
import com.vehiclerental.entity.*;
import com.vehiclerental.enums.*;
import com.vehiclerental.exception.DuplicateResourceException;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleCategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateOwnerStatus(Long ownerId, UserStatus status) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with ID: " + ownerId));

        if (owner.getRole() != Role.ROLE_OWNER) {
            throw new IllegalArgumentException("User with ID " + ownerId + " is not an OWNER");
        }

        owner.setStatus(status);
        User updatedOwner = userRepository.save(owner);
        return new UserResponse(updatedOwner);
    }

    public List<VehicleResponse> getPendingVehicles() {
        return vehicleRepository.findByApprovalStatus(VehicleApprovalStatus.PENDING).stream()
                .map(v -> new VehicleResponse(v, reviewRepository.getAverageRatingForVehicle(v.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public VehicleResponse reviewVehicle(Long vehicleId, VehicleApprovalStatus status, String rejectionReason) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId));

        vehicle.setApprovalStatus(status);
        if (status == VehicleApprovalStatus.REJECTED) {
            vehicle.setRejectionReason(rejectionReason);
            vehicle.setAvailabilityStatus(VehicleAvailabilityStatus.INACTIVE);
        } else if (status == VehicleApprovalStatus.APPROVED) {
            vehicle.setRejectionReason(null);
            vehicle.setAvailabilityStatus(VehicleAvailabilityStatus.AVAILABLE);
        }

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        Double avgRating = reviewRepository.getAverageRatingForVehicle(vehicleId);
        return new VehicleResponse(updatedVehicle, avgRating);
    }

    public DashboardStatsResponse getDashboardStats() {
        DashboardStatsResponse stats = new DashboardStatsResponse();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalOwners(userRepository.countByRole(Role.ROLE_OWNER));
        stats.setPendingOwners(userRepository.countByRoleAndStatus(Role.ROLE_OWNER, UserStatus.PENDING));
        stats.setTotalCustomers(userRepository.countByRole(Role.ROLE_CUSTOMER));
        stats.setTotalVehicles(vehicleRepository.count());
        stats.setPendingVehicles(vehicleRepository.countByApprovalStatus(VehicleApprovalStatus.PENDING));
        stats.setApprovedVehicles(vehicleRepository.countByApprovalStatus(VehicleApprovalStatus.APPROVED));
        stats.setActiveRentals(bookingRepository.countByStatus(BookingStatus.ACTIVE));
        stats.setCompletedBookings(bookingRepository.countByStatus(BookingStatus.COMPLETED));
        return stats;
    }

    // Category Management
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category name already exists: " + request.getName());
        }
        VehicleCategory category = new VehicleCategory(request.getName(), request.getDescription());
        return new CategoryResponse(categoryRepository.save(category));
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    // Location Management
    public LocationResponse createLocation(LocationRequest request) {
        Location location = new Location(request.getName(), request.getCity(), request.getState());
        return new LocationResponse(locationRepository.save(location));
    }

    public List<LocationResponse> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(LocationResponse::new)
                .collect(Collectors.toList());
    }
}
