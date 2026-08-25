//The repository layer is what lets our services talk to the database through JPA.
package com.spartans.railflex.repository;

import com.spartans.railflex.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}