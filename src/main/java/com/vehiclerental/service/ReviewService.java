package com.vehiclerental.service;

import com.vehiclerental.dto.request.ReviewRequest;
import com.vehiclerental.dto.response.ReviewResponse;
import com.vehiclerental.entity.Booking;
import com.vehiclerental.entity.Review;
import com.vehiclerental.enums.BookingStatus;
import com.vehiclerental.exception.BookingException;
import com.vehiclerental.exception.DuplicateResourceException;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.exception.UnauthorizedException;
import com.vehiclerental.repository.BookingRepository;
import com.vehiclerental.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Transactional
    public ReviewResponse createReview(Long customerId, ReviewRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getCustomer().getId().equals(customerId)) {
            throw new UnauthorizedException("You can only review vehicles you have personally booked.");
        }

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new BookingException("Reviews can only be submitted after the booking is COMPLETED.");
        }

        if (reviewRepository.existsByBookingId(request.getBookingId())) {
            throw new DuplicateResourceException("You have already submitted a review for this booking.");
        }

        Review review = new Review();
        review.setCustomer(booking.getCustomer());
        review.setVehicle(booking.getVehicle());
        review.setBooking(booking);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);
        return new ReviewResponse(savedReview);
    }

    public List<ReviewResponse> getVehicleReviews(Long vehicleId) {
        return reviewRepository.findByVehicleIdOrderByCreatedAtDesc(vehicleId).stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }
}
