package com.vehiclerental.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_inspections")
public class VehicleInspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Column(name = "pickup_odometer")
    private Double pickupOdometer;

    @Column(name = "pickup_fuel_level", length = 50)
    private String pickupFuelLevel;

    @Column(name = "pickup_notes", columnDefinition = "TEXT")
    private String pickupNotes;

    @Column(name = "pickup_date_time")
    private LocalDateTime pickupDateTime;

    @Column(name = "return_odometer")
    private Double returnOdometer;

    @Column(name = "return_fuel_level", length = 50)
    private String returnFuelLevel;

    @Column(name = "return_notes", columnDefinition = "TEXT")
    private String returnNotes;

    @Column(name = "return_date_time")
    private LocalDateTime returnDateTime;

    @Column(name = "damage_notes", columnDefinition = "TEXT")
    private String damageNotes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public VehicleInspection() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
