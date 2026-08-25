package com.spartans.railflex.service;

import com.spartans.railflex.entity.Passenger;
import com.spartans.railflex.entity.Station;
import com.spartans.railflex.entity.Train;
import com.spartans.railflex.entity.WaitlistEntry;
import com.spartans.railflex.repository.WaitlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Manages passengers waiting when no suitable seat is available.
@Service
public class WaitlistService {

    private final WaitlistRepository waitlistRepository;

    public WaitlistService(WaitlistRepository waitlistRepository) {
        this.waitlistRepository = waitlistRepository;
    }

    // Adds a passenger to the waitlist with a priority based on journey length.
    public WaitlistEntry addToWaitlist(
            Passenger passenger,
            Train train,
            Station sourceStation,
            Station destinationStation) {

        int priority = calculatePriority(
                sourceStation,
                destinationStation
        );

        WaitlistEntry entry = new WaitlistEntry(
                passenger,
                train,
                sourceStation,
                destinationStation,
                priority
        );

        return waitlistRepository.save(entry);
    }

    // Longer journeys receive higher priority.
    private int calculatePriority(
            Station sourceStation,
            Station destinationStation) {

        return destinationStation.getSequenceNumber()
                - sourceStation.getSequenceNumber();
    }

    // Returns the current waitlist for a train in priority order.
    public List<WaitlistEntry> getWaitlist(Train train) {
        return waitlistRepository
                .findByTrainOrderByPriorityDescCreatedAtAsc(train);
    }
}