package com.spartans.railflex.repository;

import com.spartans.railflex.entity.Train;
import com.spartans.railflex.entity.WaitlistEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Stores and retrieves passengers waiting for an available seat.
public interface WaitlistRepository extends JpaRepository<WaitlistEntry, Long> {

    // Gets the waitlist for a train, highest priority first,
    // and older requests first when priorities are equal.
    List<WaitlistEntry> findByTrainOrderByPriorityDescCreatedAtAsc(Train train);
}