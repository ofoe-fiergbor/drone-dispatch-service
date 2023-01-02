package io.iamofoe.dronedispatchservice.repository;

import io.iamofoe.dronedispatchservice.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Integer> {
    Optional<Drone> getDroneBySerialNumber(String serialNumber);
}
