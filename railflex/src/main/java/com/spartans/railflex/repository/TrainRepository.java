package com.spartans.railflex.repository;

import com.spartans.railflex.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainRepository extends JpaRepository<Train, Long> {
}