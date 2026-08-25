package com.spartans.railflex.service;

import com.spartans.railflex.controller.dto.BookingRequest;
import com.spartans.railflex.controller.dto.BookingResponse;
import com.spartans.railflex.entity.Booking;
import com.spartans.railflex.entity.BookingStatus;
import com.spartans.railflex.entity.Passenger;
import com.spartans.railflex.entity.Seat;
import com.spartans.railflex.entity.Station;
import com.spartans.railflex.entity.Train;
import com.spartans.railflex.entity.WaitlistEntry;
import com.spartans.railflex.repository.BookingRepository;
import com.spartans.railflex.repository.PassengerRepository;
import com.spartans.railflex.repository.StationRepository;
import com.spartans.railflex.repository.TrainRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Coordinates the complete passenger booking process.
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final SeatAllocationService seatAllocationService;
    private final WaitlistService waitlistService;

    public BookingService(
            BookingRepository bookingRepository,
            PassengerRepository passengerRepository,
            TrainRepository trainRepository,
            StationRepository stationRepository,
            SeatAllocationService seatAllocationService,
            WaitlistService waitlistService) {

        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.trainRepository = trainRepository;
        this.stationRepository = stationRepository;
        this.seatAllocationService = seatAllocationService;
        this.waitlistService = waitlistService;
    }

    // Creates a confirmed booking or adds the passenger to the waitlist.
    public BookingResponse createBooking(BookingRequest request) {

        Passenger passenger = passengerRepository
                .findById(request.getPassengerId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Passenger not found"));

        Train train = trainRepository
                .findById(request.getTrainId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Train not found"));

        Station sourceStation = stationRepository
                .findById(request.getSourceStationId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Source station not found"));

        Station destinationStation = stationRepository
                .findById(request.getDestinationStationId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Destination station not found"));

        if (sourceStation.getId().equals(destinationStation.getId())) {
            throw new IllegalArgumentException(
                    "Source and destination cannot be the same");
        }

        if (sourceStation.getSequenceNumber()
                >= destinationStation.getSequenceNumber()) {

            throw new IllegalArgumentException(
                    "Source station must come before destination station");
        }

        Optional<Seat> availableSeat =
                seatAllocationService.findAvailableSeat(
                        train,
                        sourceStation,
                        destinationStation
                );

        // No suitable seat: add passenger to the waitlist.
        if (availableSeat.isEmpty()) {

            WaitlistEntry waitlistEntry =
                    waitlistService.addToWaitlist(
                            passenger,
                            train,
                            sourceStation,
                            destinationStation
                    );

            return new BookingResponse(
                    "WAITLISTED",
                    "No seat available. Passenger added to waitlist.",
                    null,
                    waitlistEntry.getId()
            );
        }

        // Suitable seat found: create the booking.
        Booking booking = new Booking();

        booking.setPassenger(passenger);
        booking.setTrain(train);
        booking.setSeat(availableSeat.get());
        booking.setSourceStation(sourceStation);
        booking.setDestinationStation(destinationStation);
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);

        return new BookingResponse(
                "BOOKED",
                "Booking confirmed.",
                savedBooking.getId(),
                null
        );
    }
}