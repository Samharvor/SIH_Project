package com.spartans.railflex.repository;

import com.spartans.railflex.entity.Booking;
import com.spartans.railflex.entity.BookingStatus;
import com.spartans.railflex.entity.Seat;
import com.spartans.railflex.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByTrainAndSeatAndStatus(
            Train train,
            Seat seat,
            BookingStatus status
    );
}