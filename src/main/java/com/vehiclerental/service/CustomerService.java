package com.vehiclerental.service;

import com.vehiclerental.dto.request.BookingCreateRequest;
import com.vehiclerental.dto.response.BookingResponse;
import com.vehiclerental.dto.response.InspectionResponse;
import com.vehiclerental.entity.*;
import com.vehiclerental.enums.*;
import com.vehiclerental.exception.*;
import com.vehiclerental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleInspectionRepository inspectionRepository;

    @Transactional
    public BookingResponse createBooking(Long customerId, BookingCreateRequest request) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (customer.getStatus() != UserStatus.APPROVED) {
            throw new UnauthorizedException("Your customer account is not active");
        }

        if (request.getReturnDate().isBefore(request.getPickupDate())) {
            throw new BookingException("Return date cannot be before pickup date");
        }

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        if (vehicle.getApprovalStatus() != VehicleApprovalStatus.APPROVED) {
            throw new VehicleNotAvailableException("Vehicle is not approved for rental");
        }

        if (vehicle.getAvailabilityStatus() == VehicleAvailabilityStatus.INACTIVE ||
            vehicle.getAvailabilityStatus() == VehicleAvailabilityStatus.MAINTENANCE) {
            throw new VehicleNotAvailableException("Vehicle is currently unavailable for rental (" + vehicle.getAvailabilityStatus() + ")");
        }

        // Check for date overlaps with active bookings
        List<BookingStatus> activeStatuses = Arrays.asList(
                BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.ACTIVE
        );

        boolean hasOverlap = bookingRepository.hasOverlappingBooking(
                vehicle.getId(), request.getPickupDate(), request.getReturnDate(), activeStatuses
        );

        if (hasOverlap) {
            throw new VehicleNotAvailableException("Vehicle is already booked for the selected date range.");
        }

        Location pickupLocation = locationRepository.findById(request.getPickupLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Pickup location not found"));

        Location returnLocation = locationRepository.findById(request.getReturnLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Return location not found"));

        // Server-Side Price Calculation
        long days = ChronoUnit.DAYS.between(request.getPickupDate(), request.getReturnDate());
        int totalDays = (int) Math.max(1, days); // Minimum 1 day calculation
        BigDecimal totalAmount = vehicle.getPricePerDay().multiply(BigDecimal.valueOf(totalDays));

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setVehicle(vehicle);
        booking.setPickupLocation(pickupLocation);
        booking.setReturnLocation(returnLocation);
        booking.setPickupDate(request.getPickupDate());
        booking.setReturnDate(request.getReturnDate());
        booking.setTotalDays(totalDays);
        booking.setPricePerDay(vehicle.getPricePerDay());
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);
        return new BookingResponse(savedBooking);
    }

    public List<BookingResponse> getCustomerBookings(Long customerId) {
        return bookingRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(booking -> {
                    BookingResponse res = new BookingResponse(booking);
                    inspectionRepository.findByBookingId(booking.getId())
                            .ifPresent(insp -> res.setInspection(new InspectionResponse(insp)));
                    return res;
                })
                .collect(Collectors.toList());
    }

    public BookingResponse getBookingDetails(Long customerId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getCustomer().getId().equals(customerId) &&
            !booking.getVehicle().getOwner().getId().equals(customerId)) {
            throw new UnauthorizedException("You are not authorized to view this booking");
        }

        BookingResponse response = new BookingResponse(booking);
        inspectionRepository.findByBookingId(bookingId)
                .ifPresent(insp -> response.setInspection(new InspectionResponse(insp)));
        return response;
    }

    @Transactional
    public BookingResponse cancelBooking(Long customerId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getCustomer().getId().equals(customerId)) {
            throw new UnauthorizedException("You can only cancel your own bookings");
        }

        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new InvalidBookingStateException("Cannot cancel booking in state: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking savedBooking = bookingRepository.save(booking);
        return new BookingResponse(savedBooking);
    }
}
