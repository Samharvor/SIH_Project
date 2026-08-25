package com.spartans.railflex.repository;

import com.spartans.railflex.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
}