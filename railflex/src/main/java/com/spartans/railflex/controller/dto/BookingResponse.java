package com.spartans.railflex.controller.dto;

// Represents the result returned after a booking attempt.
public class BookingResponse {

    private String status;
    private String message;
    private Long bookingId;
    private Long waitlistId;

    public BookingResponse() {
    }

    public BookingResponse(
            String status,
            String message,
            Long bookingId,
            Long waitlistId) {

        this.status = status;
        this.message = message;
        this.bookingId = bookingId;
        this.waitlistId = waitlistId;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public Long getWaitlistId() {
        return waitlistId;
    }
}