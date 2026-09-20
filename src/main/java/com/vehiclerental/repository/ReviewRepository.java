package com.vehiclerental.repository;

import com.vehiclerental.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByVehicleIdOrderByCreatedAtDesc(Long vehicleId);
    List<Review> findByVehicleOwnerIdOrderByCreatedAtDesc(Long ownerId);
    Optional<Review> findByBookingId(Long bookingId);
    Boolean existsByBookingId(Long bookingId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.vehicle.id = :vehicleId")
    Double getAverageRatingForVehicle(@Param("vehicleId") Long vehicleId);
}
