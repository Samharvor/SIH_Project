//Carries the passenger's journey details for a booking request.
package com.spartans.railflex.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingRequest {

    @NotNull(message = "Passenger ID is required")
    @Positive(message = "Passenger ID must be positive")
    private Long passengerId;

    @NotNull(message = "Train ID is required")
    @Positive(message = "Train ID must be positive")
    private Long trainId;

    @NotNull(message = "Source station ID is required")
    @Positive(message = "Source station ID must be positive")
    private Long sourceStationId;

    @NotNull(message = "Destination station ID is required")
    @Positive(message = "Destination station ID must be positive")
    private Long destinationStationId;

    public BookingRequest() {
    }

    public BookingRequest(Long passengerId,
                           Long trainId,
                           Long sourceStationId,
                           Long destinationStationId) {
        this.passengerId = passengerId;
        this.trainId = trainId;
        this.sourceStationId = sourceStationId;
        this.destinationStationId = destinationStationId;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public void setSourceStationId(Long sourceStationId) {
        this.sourceStationId = sourceStationId;
    }

    public Long getDestinationStationId() {
        return destinationStationId;
    }

    public void setDestinationStationId(Long destinationStationId) {
        this.destinationStationId = destinationStationId;
    }
}