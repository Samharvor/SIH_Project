package com.spartans.railflex.controller;

import com.spartans.railflex.controller.dto.BookingRequest;
import com.spartans.railflex.controller.dto.BookingResponse;
import com.spartans.railflex.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Receives booking requests and returns the booking or waitlist result.
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Creates a booking or adds the passenger to the waitlist.
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request) {

        BookingResponse response =
                bookingService.createBooking(request);

        return ResponseEntity.ok(response);
    }
}