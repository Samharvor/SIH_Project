package com.spartans.railflex.repository;

import com.spartans.railflex.entity.Seat;
import com.spartans.railflex.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByTrain(Train train);
}