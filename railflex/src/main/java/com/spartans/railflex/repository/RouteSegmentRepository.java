package com.spartans.railflex.repository;

import com.spartans.railflex.entity.RouteSegment;
import com.spartans.railflex.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteSegmentRepository extends JpaRepository<RouteSegment, Long> {

    List<RouteSegment> findByTrainOrderBySegmentOrder(Train train);
}