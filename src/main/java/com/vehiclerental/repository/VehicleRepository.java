package com.vehiclerental.repository;

import com.vehiclerental.entity.Vehicle;
import com.vehiclerental.enums.VehicleApprovalStatus;
import com.vehiclerental.enums.VehicleAvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    List<Vehicle> findByOwnerId(Long ownerId);
    
    List<Vehicle> findByApprovalStatus(VehicleApprovalStatus approvalStatus);
    
    List<Vehicle> findByApprovalStatusAndAvailabilityStatus(
            VehicleApprovalStatus approvalStatus, VehicleAvailabilityStatus availabilityStatus);

    @Query("SELECT v FROM Vehicle v WHERE v.approvalStatus = 'APPROVED' AND v.availabilityStatus = 'AVAILABLE' " +
           "AND (:locationId IS NULL OR v.location.id = :locationId) " +
           "AND (:categoryId IS NULL OR v.category.id = :categoryId) " +
           "AND (:maxPrice IS NULL OR v.pricePerDay <= :maxPrice) " +
           "AND (:searchQuery IS NULL OR LOWER(v.brand) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR LOWER(v.model) LIKE LOWER(CONCAT('%', :searchQuery, '%')))")
    List<Vehicle> searchApprovedVehicles(
            @Param("locationId") Long locationId,
            @Param("categoryId") Long categoryId,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("searchQuery") String searchQuery);

    long countByApprovalStatus(VehicleApprovalStatus approvalStatus);
    
    long countByApprovalStatusAndAvailabilityStatus(
            VehicleApprovalStatus approvalStatus, VehicleAvailabilityStatus availabilityStatus);
            
    Boolean existsByVehicleNumber(String vehicleNumber);
}
