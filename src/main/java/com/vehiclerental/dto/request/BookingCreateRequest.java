package com.vehiclerental.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class BookingCreateRequest {

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotNull(message = "Pickup location ID is required")
    private Long pickupLocationId;

    @NotNull(message = "Return location ID is required")
    private Long returnLocationId;

    @NotNull(message = "Pickup date is required")
    @FutureOrPresent(message = "Pickup date must be today or in the future")
    private LocalDate pickupDate;

    @NotNull(message = "Return date is required")
    @FutureOrPresent(message = "Return date must be today or in the future")
    private LocalDate returnDate;

    public BookingCreateRequest() {}

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public Long getPickupLocationId() { return pickupLocationId; }
    public void setPickupLocationId(Long pickupLocationId) { this.pickupLocationId = pickupLocationId; }

    public Long getReturnLocationId() { return returnLocationId; }
    public void setReturnLocationId(Long returnLocationId) { this.returnLocationId = returnLocationId; }

    public LocalDate getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
}
