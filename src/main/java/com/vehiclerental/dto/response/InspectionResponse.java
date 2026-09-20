package com.vehiclerental.dto.response;

import com.vehiclerental.entity.VehicleInspection;

import java.time.LocalDateTime;

public class InspectionResponse {
    private Long id;
    private Long bookingId;
    private Double pickupOdometer;
    private String pickupFuelLevel;
    private String pickupNotes;
    private LocalDateTime pickupDateTime;
    private Double returnOdometer;
    private String returnFuelLevel;
    private String returnNotes;
    private LocalDateTime returnDateTime;
    private String damageNotes;

    public InspectionResponse() {}

    public InspectionResponse(VehicleInspection inspection) {
        this.id = inspection.getId();
        if (inspection.getBooking() != null) {
            this.bookingId = inspection.getBooking().getId();
        }
        this.pickupOdometer = inspection.getPickupOdometer();
        this.pickupFuelLevel = inspection.getPickupFuelLevel();
        this.pickupNotes = inspection.getPickupNotes();
        this.pickupDateTime = inspection.getPickupDateTime();
        this.returnOdometer = inspection.getReturnOdometer();
        this.returnFuelLevel = inspection.getReturnFuelLevel();
        this.returnNotes = inspection.getReturnNotes();
        this.returnDateTime = inspection.getReturnDateTime();
        this.damageNotes = inspection.getDamageNotes();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Double getPickupOdometer() { return pickupOdometer; }
    public void setPickupOdometer(Double pickupOdometer) { this.pickupOdometer = pickupOdometer; }

    public String getPickupFuelLevel() { return pickupFuelLevel; }
    public void setPickupFuelLevel(String pickupFuelLevel) { this.pickupFuelLevel = pickupFuelLevel; }

    public String getPickupNotes() { return pickupNotes; }
    public void setPickupNotes(String pickupNotes) { this.pickupNotes = pickupNotes; }

    public LocalDateTime getPickupDateTime() { return pickupDateTime; }
    public void setPickupDateTime(LocalDateTime pickupDateTime) { this.pickupDateTime = pickupDateTime; }

    public Double getReturnOdometer() { return returnOdometer; }
    public void setReturnOdometer(Double returnOdometer) { this.returnOdometer = returnOdometer; }

    public String getReturnFuelLevel() { return returnFuelLevel; }
    public void setReturnFuelLevel(String returnFuelLevel) { this.returnFuelLevel = returnFuelLevel; }

    public String getReturnNotes() { return returnNotes; }
    public void setReturnNotes(String returnNotes) { this.returnNotes = returnNotes; }

    public LocalDateTime getReturnDateTime() { return returnDateTime; }
    public void setReturnDateTime(LocalDateTime returnDateTime) { this.returnDateTime = returnDateTime; }

    public String getDamageNotes() { return damageNotes; }
    public void setDamageNotes(String damageNotes) { this.damageNotes = damageNotes; }
}
