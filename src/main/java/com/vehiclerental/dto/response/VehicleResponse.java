package com.vehiclerental.dto.response;

import com.vehiclerental.entity.Vehicle;
import com.vehiclerental.enums.FuelType;
import com.vehiclerental.enums.TransmissionType;
import com.vehiclerental.enums.VehicleApprovalStatus;
import com.vehiclerental.enums.VehicleAvailabilityStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VehicleResponse {
    private Long id;
    private UserResponse owner;
    private CategoryResponse category;
    private LocationResponse location;
    private String vehicleNumber;
    private String brand;
    private String model;
    private Integer manufacturingYear;
    private FuelType fuelType;
    private TransmissionType transmission;
    private Integer seatingCapacity;
    private BigDecimal pricePerDay;
    private String description;
    private String imageUrl;
    private VehicleApprovalStatus approvalStatus;
    private String rejectionReason;
    private VehicleAvailabilityStatus availabilityStatus;
    private Double averageRating;
    private LocalDateTime createdAt;

    public VehicleResponse() {}

    public VehicleResponse(Vehicle vehicle, Double averageRating) {
        this.id = vehicle.getId();
        if (vehicle.getOwner() != null) {
            this.owner = new UserResponse(vehicle.getOwner());
        }
        if (vehicle.getCategory() != null) {
            this.category = new CategoryResponse(vehicle.getCategory());
        }
        if (vehicle.getLocation() != null) {
            this.location = new LocationResponse(vehicle.getLocation());
        }
        this.vehicleNumber = vehicle.getVehicleNumber();
        this.brand = vehicle.getBrand();
        this.model = vehicle.getModel();
        this.manufacturingYear = vehicle.getManufacturingYear();
        this.fuelType = vehicle.getFuelType();
        this.transmission = vehicle.getTransmission();
        this.seatingCapacity = vehicle.getSeatingCapacity();
        this.pricePerDay = vehicle.getPricePerDay();
        this.description = vehicle.getDescription();
        this.imageUrl = vehicle.getImageUrl();
        this.approvalStatus = vehicle.getApprovalStatus();
        this.rejectionReason = vehicle.getRejectionReason();
        this.availabilityStatus = vehicle.getAvailabilityStatus();
        this.averageRating = averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : null;
        this.createdAt = vehicle.getCreatedAt();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserResponse getOwner() { return owner; }
    public void setOwner(UserResponse owner) { this.owner = owner; }

    public CategoryResponse getCategory() { return category; }
    public void setCategory(CategoryResponse category) { this.category = category; }

    public LocationResponse getLocation() { return location; }
    public void setLocation(LocationResponse location) { this.location = location; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getManufacturingYear() { return manufacturingYear; }
    public void setManufacturingYear(Integer manufacturingYear) { this.manufacturingYear = manufacturingYear; }

    public FuelType getFuelType() { return fuelType; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }

    public TransmissionType getTransmission() { return transmission; }
    public void setTransmission(TransmissionType transmission) { this.transmission = transmission; }

    public Integer getSeatingCapacity() { return seatingCapacity; }
    public void setSeatingCapacity(Integer seatingCapacity) { this.seatingCapacity = seatingCapacity; }

    public BigDecimal getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(BigDecimal pricePerDay) { this.pricePerDay = pricePerDay; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public VehicleApprovalStatus getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(VehicleApprovalStatus approvalStatus) { this.approvalStatus = approvalStatus; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public VehicleAvailabilityStatus getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(VehicleAvailabilityStatus availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
