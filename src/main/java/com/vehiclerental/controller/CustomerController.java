package com.vehiclerental.controller;

import com.vehiclerental.config.SecurityUtils;
import com.vehiclerental.dto.request.BookingCreateRequest;
import com.vehiclerental.dto.request.ReviewRequest;
import com.vehiclerental.dto.response.ApiResponse;
import com.vehiclerental.dto.response.BookingResponse;
import com.vehiclerental.dto.response.ReviewResponse;
import com.vehiclerental.service.CustomerService;
import com.vehiclerental.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private SecurityUtils securityUtils;

    @PostMapping("/bookings")
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@Valid @RequestBody BookingCreateRequest request) {
        Long customerId = securityUtils.getCurrentUserId();
        BookingResponse response = customerService.createBooking(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Booking request created successfully", response));
    }

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings() {
        Long customerId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved", customerService.getCustomerBookings(customerId)));
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingDetails(@PathVariable Long id) {
        Long customerId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success("Booking details retrieved", customerService.getBookingDetails(customerId, id)));
    }

    @PutMapping("/bookings/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable Long id) {
        Long customerId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled", customerService.cancelBooking(customerId, id)));
    }

    @PostMapping("/reviews")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(@Valid @RequestBody ReviewRequest request) {
        Long customerId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success("Review submitted successfully", reviewService.createReview(customerId, request)));
    }
}
