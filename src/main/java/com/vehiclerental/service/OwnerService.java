package com.vehiclerental.service;

import com.vehiclerental.dto.request.PickupRequest;
import com.vehiclerental.dto.request.ReturnRequest;
import com.vehiclerental.dto.request.VehicleRequest;
import com.vehiclerental.dto.response.BookingResponse;
import com.vehiclerental.dto.response.InspectionResponse;
import com.vehiclerental.dto.response.VehicleResponse;
import com.vehiclerental.entity.*;
import com.vehiclerental.enums.*;
import com.vehiclerental.exception.DuplicateResourceException;
import com.vehiclerental.exception.InvalidBookingStateException;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.exception.UnauthorizedException;
import com.vehiclerental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleCategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleInspectionRepository inspectionRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public VehicleResponse addVehicle(Long ownerId, VehicleRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        if (owner.getStatus() != UserStatus.APPROVED) {
            throw new UnauthorizedException("Owner is not approved by Admin to list vehicles.");
        }

        if (vehicleRepository.existsByVehicleNumber(request.getVehicleNumber())) {
            throw new DuplicateResourceException("Vehicle number already registered: " + request.getVehicleNumber());
        }

        VehicleCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found"));

        Vehicle vehicle = new Vehicle();
        vehicle.setOwner(owner);
        vehicle.setCategory(category);
        vehicle.setLocation(location);
        vehicle.setVehicleNumber(request.getVehicleNumber());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setManufacturingYear(request.getManufacturingYear());
        vehicle.setFuelType(request.getFuelType());
        vehicle.setTransmission(request.getTransmission());
        vehicle.setSeatingCapacity(request.getSeatingCapacity());
        vehicle.setPricePerDay(request.getPricePerDay());
        vehicle.setDescription(request.getDescription());
        vehicle.setImageUrl(request.getImageUrl());
        vehicle.setApprovalStatus(VehicleApprovalStatus.PENDING);
        vehicle.setAvailabilityStatus(VehicleAvailabilityStatus.AVAILABLE);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return new VehicleResponse(savedVehicle, null);
    }

    @Transactional
    public VehicleResponse updateVehicle(Long ownerId, Long vehicleId, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        if (!vehicle.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedException("You do not own this vehicle");
        }

        VehicleCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found"));

        vehicle.setCategory(category);
        vehicle.setLocation(location);
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setManufacturingYear(request.getManufacturingYear());
        vehicle.setFuelType(request.getFuelType());
        vehicle.setTransmission(request.getTransmission());
        vehicle.setSeatingCapacity(request.getSeatingCapacity());
        vehicle.setPricePerDay(request.getPricePerDay());
        vehicle.setDescription(request.getDescription());
        if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            vehicle.setImageUrl(request.getImageUrl());
        }
        if (request.getAvailabilityStatus() != null) {
            vehicle.setAvailabilityStatus(request.getAvailabilityStatus());
        }

        // Resubmit rejected vehicle for admin approval
        if (vehicle.getApprovalStatus() == VehicleApprovalStatus.REJECTED) {
            vehicle.setApprovalStatus(VehicleApprovalStatus.PENDING);
            vehicle.setRejectionReason(null);
        }

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        Double avgRating = reviewRepository.getAverageRatingForVehicle(vehicleId);
        return new VehicleResponse(updatedVehicle, avgRating);
    }

    public List<VehicleResponse> getOwnerVehicles(Long ownerId) {
        return vehicleRepository.findByOwnerId(ownerId).stream()
                .map(v -> new VehicleResponse(v, reviewRepository.getAverageRatingForVehicle(v.getId())))
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getOwnerBookings(Long ownerId) {
        return bookingRepository.findByVehicleOwnerIdOrderByCreatedAtDesc(ownerId).stream()
                .map(booking -> {
                    BookingResponse res = new BookingResponse(booking);
                    inspectionRepository.findByBookingId(booking.getId())
                            .ifPresent(insp -> res.setInspection(new InspectionResponse(insp)));
                    return res;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponse acceptBooking(Long ownerId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getVehicle().getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedException("You do not own the vehicle for this booking");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new InvalidBookingStateException("Booking cannot be accepted from state: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        Booking savedBooking = bookingRepository.save(booking);
        return new BookingResponse(savedBooking);
    }

    @Transactional
    public BookingResponse rejectBooking(Long ownerId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getVehicle().getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedException("You do not own the vehicle for this booking");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new InvalidBookingStateException("Booking cannot be rejected from state: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.REJECTED);
        Booking savedBooking = bookingRepository.save(booking);
        return new BookingResponse(savedBooking);
    }

    @Transactional
    public BookingResponse performPickup(Long ownerId, Long bookingId, PickupRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getVehicle().getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedException("You do not own the vehicle for this booking");
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new InvalidBookingStateException("Vehicle pickup can only be recorded for CONFIRMED bookings");
        }

        VehicleInspection inspection = inspectionRepository.findByBookingId(bookingId)
                .orElseGet(() -> {
                    VehicleInspection insp = new VehicleInspection();
                    insp.setBooking(booking);
                    return insp;
                });

        inspection.setPickupOdometer(request.getPickupOdometer());
        inspection.setPickupFuelLevel(request.getPickupFuelLevel());
        inspection.setPickupNotes(request.getPickupNotes());
        inspection.setPickupDateTime(LocalDateTime.now());
        inspectionRepository.save(inspection);

        booking.setStatus(BookingStatus.ACTIVE);
        booking.getVehicle().setAvailabilityStatus(VehicleAvailabilityStatus.RENTED);
        vehicleRepository.save(booking.getVehicle());
        Booking savedBooking = bookingRepository.save(booking);

        BookingResponse response = new BookingResponse(savedBooking);
        response.setInspection(new InspectionResponse(inspection));
        return response;
    }

    @Transactional
    public BookingResponse performReturn(Long ownerId, Long bookingId, ReturnRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getVehicle().getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedException("You do not own the vehicle for this booking");
        }

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new InvalidBookingStateException("Vehicle return can only be recorded for ACTIVE rentals");
        }

        VehicleInspection inspection = inspectionRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Pickup inspection record missing for booking"));

        inspection.setReturnOdometer(request.getReturnOdometer());
        inspection.setReturnFuelLevel(request.getReturnFuelLevel());
        inspection.setReturnNotes(request.getReturnNotes());
        inspection.setDamageNotes(request.getDamageNotes());
        inspection.setReturnDateTime(LocalDateTime.now());
        inspectionRepository.save(inspection);

        booking.setStatus(BookingStatus.COMPLETED);
        booking.getVehicle().setAvailabilityStatus(VehicleAvailabilityStatus.AVAILABLE);
        vehicleRepository.save(booking.getVehicle());
        Booking savedBooking = bookingRepository.save(booking);

        BookingResponse response = new BookingResponse(savedBooking);
        response.setInspection(new InspectionResponse(inspection));
        return response;
    }
}
