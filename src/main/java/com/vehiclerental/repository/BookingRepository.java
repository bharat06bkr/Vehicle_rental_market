package com.vehiclerental.repository;

import com.vehiclerental.entity.Booking;
import com.vehiclerental.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    List<Booking> findByVehicleOwnerIdOrderByCreatedAtDesc(Long ownerId);

    List<Booking> findByVehicleIdOrderByCreatedAtDesc(Long vehicleId);

    List<Booking> findByStatus(BookingStatus status);

    long countByStatus(BookingStatus status);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.vehicle.id = :vehicleId " +
           "AND b.status IN (:activeStatuses) " +
           "AND (b.pickupDate <= :returnDate AND b.returnDate >= :pickupDate)")
    boolean hasOverlappingBooking(@Param("vehicleId") Long vehicleId,
                                  @Param("pickupDate") LocalDate pickupDate,
                                  @Param("returnDate") LocalDate returnDate,
                                  @Param("activeStatuses") List<BookingStatus> activeStatuses);

    boolean existsByCustomerIdAndVehicleIdAndStatus(Long customerId, Long vehicleId, BookingStatus status);
}
