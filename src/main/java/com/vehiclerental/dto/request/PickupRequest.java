package com.vehiclerental.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PickupRequest {

    @NotNull(message = "Pickup odometer reading is required")
    @Positive(message = "Pickup odometer reading must be positive")
    private Double pickupOdometer;

    private String pickupFuelLevel;
    private String pickupNotes;

    public PickupRequest() {}

    public Double getPickupOdometer() { return pickupOdometer; }
    public void setPickupOdometer(Double pickupOdometer) { this.pickupOdometer = pickupOdometer; }

    public String getPickupFuelLevel() { return pickupFuelLevel; }
    public void setPickupFuelLevel(String pickupFuelLevel) { this.pickupFuelLevel = pickupFuelLevel; }

    public String getPickupNotes() { return pickupNotes; }
    public void setPickupNotes(String pickupNotes) { this.pickupNotes = pickupNotes; }
}
