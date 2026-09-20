package com.vehiclerental.dto.response;

import com.vehiclerental.entity.Review;
import java.time.LocalDateTime;

public class ReviewResponse {
    private Long id;
    private Long vehicleId;
    private String customerName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public ReviewResponse() {}

    public ReviewResponse(Review review) {
        this.id = review.getId();
        if (review.getVehicle() != null) {
            this.vehicleId = review.getVehicle().getId();
        }
        if (review.getCustomer() != null) {
            this.customerName = review.getCustomer().getName();
        }
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.createdAt = review.getCreatedAt();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
