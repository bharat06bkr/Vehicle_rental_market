package com.vehiclerental.dto.response;

import com.vehiclerental.entity.Booking;
import com.vehiclerental.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponse {
    private Long id;
    private UserResponse customer;
    private VehicleResponse vehicle;
    private LocationResponse pickupLocation;
    private LocationResponse returnLocation;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private Integer totalDays;
    private BigDecimal pricePerDay;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private InspectionResponse inspection;
    private LocalDateTime createdAt;

    public BookingResponse() {}

    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        if (booking.getCustomer() != null) {
            this.customer = new UserResponse(booking.getCustomer());
        }
        if (booking.getVehicle() != null) {
            this.vehicle = new VehicleResponse(booking.getVehicle(), null);
        }
        if (booking.getPickupLocation() != null) {
            this.pickupLocation = new LocationResponse(booking.getPickupLocation());
        }
        if (booking.getReturnLocation() != null) {
            this.returnLocation = new LocationResponse(booking.getReturnLocation());
        }
        this.pickupDate = booking.getPickupDate();
        this.returnDate = booking.getReturnDate();
        this.totalDays = booking.getTotalDays();
        this.pricePerDay = booking.getPricePerDay();
        this.totalAmount = booking.getTotalAmount();
        this.status = booking.getStatus();
        this.createdAt = booking.getCreatedAt();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserResponse getCustomer() { return customer; }
    public void setCustomer(UserResponse customer) { this.customer = customer; }

    public VehicleResponse getVehicle() { return vehicle; }
    public void setVehicle(VehicleResponse vehicle) { this.vehicle = vehicle; }

    public LocationResponse getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(LocationResponse pickupLocation) { this.pickupLocation = pickupLocation; }

    public LocationResponse getReturnLocation() { return returnLocation; }
    public void setReturnLocation(LocationResponse returnLocation) { this.returnLocation = returnLocation; }

    public LocalDate getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Integer getTotalDays() { return totalDays; }
    public void setTotalDays(Integer totalDays) { this.totalDays = totalDays; }

    public BigDecimal getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(BigDecimal pricePerDay) { this.pricePerDay = pricePerDay; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public InspectionResponse getInspection() { return inspection; }
    public void setInspection(InspectionResponse inspection) { this.inspection = inspection; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
