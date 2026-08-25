package com.spartans.railflex.service;

import com.spartans.railflex.entity.Booking;
import com.spartans.railflex.entity.BookingStatus;
import com.spartans.railflex.entity.RouteSegment;
import com.spartans.railflex.entity.Seat;
import com.spartans.railflex.entity.Station;
import com.spartans.railflex.entity.Train;
import com.spartans.railflex.repository.BookingRepository;
import com.spartans.railflex.repository.RouteSegmentRepository;
import com.spartans.railflex.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

// Finds the best available seat by checking journey-segment conflicts.
@Service
public class SeatAllocationService {

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final RouteSegmentRepository routeSegmentRepository;

    public SeatAllocationService(
            SeatRepository seatRepository,
            BookingRepository bookingRepository,
            RouteSegmentRepository routeSegmentRepository) {

        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
        this.routeSegmentRepository = routeSegmentRepository;
    }

    // Finds the available seat that creates the smallest remaining free space.
    public Optional<Seat> findAvailableSeat(
            Train train,
            Station sourceStation,
            Station destinationStation) {

        List<RouteSegment> routeSegments =
                routeSegmentRepository.findByTrainOrderBySegmentOrder(train);

        if (routeSegments.isEmpty()) {
            return Optional.empty();
        }

        Map<Long, Integer> stationPositions =
                buildStationPositions(routeSegments);

        Integer requestedStart =
                stationPositions.get(sourceStation.getId());

        Integer requestedEnd =
                stationPositions.get(destinationStation.getId());

        if (requestedStart == null || requestedEnd == null) {
            return Optional.empty();
        }

        if (requestedStart >= requestedEnd) {
            return Optional.empty();
        }

        int requestedLength = requestedEnd - requestedStart;
        int totalSegments = routeSegments.size();

        List<Seat> seats = seatRepository.findByTrain(train);

        Seat bestSeat = null;
        int smallestWastedSpace = Integer.MAX_VALUE;

        for (Seat seat : seats) {

            List<Booking> existingBookings =
                    bookingRepository.findByTrainAndSeatAndStatus(
                            train,
                            seat,
                            BookingStatus.CONFIRMED
                    );

            Set<Integer> occupiedSegments = new HashSet<>();
            boolean hasConflict = false;

            for (Booking booking : existingBookings) {

                Integer existingStart =
                        stationPositions.get(
                                booking.getSourceStation().getId()
                        );

                Integer existingEnd =
                        stationPositions.get(
                                booking.getDestinationStation().getId()
                        );

                if (existingStart == null || existingEnd == null) {
                    continue;
                }

                // Check whether this booking overlaps the requested journey.
                if (existingStart < requestedEnd
                        && requestedStart < existingEnd) {

                    hasConflict = true;
                    break;
                }

                // Mark the segments already occupied by this booking.
                for (int segment = existingStart;
                     segment < existingEnd;
                     segment++) {

                    occupiedSegments.add(segment);
                }
            }

            if (hasConflict) {
                continue;
            }

            // Calculate how much free space would remain after this booking.
            int remainingFreeSpace =
                    totalSegments
                            - occupiedSegments.size()
                            - requestedLength;

            if (remainingFreeSpace < smallestWastedSpace) {

                smallestWastedSpace = remainingFreeSpace;
                bestSeat = seat;
            }
        }

        return Optional.ofNullable(bestSeat);
    }

    // Converts route stations into ordered segment boundaries.
    private Map<Long, Integer> buildStationPositions(
            List<RouteSegment> routeSegments) {

        Map<Long, Integer> stationPositions = new HashMap<>();

        for (RouteSegment segment : routeSegments) {

            int order = segment.getSegmentOrder();

            stationPositions.put(
                    segment.getFromStation().getId(),
                    order
            );

            stationPositions.put(
                    segment.getToStation().getId(),
                    order + 1
            );
        }

        return stationPositions;
    }
}