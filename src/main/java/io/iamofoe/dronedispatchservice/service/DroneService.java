package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.dro.DroneDto;
import io.iamofoe.dronedispatchservice.model.Drone;

import java.util.Optional;

public interface DroneService {
    void saveDrone(DroneDto drone);
    Optional<Drone> getDroneBySerialNumber(String serialNumber);
    Optional<Drone> getDroneById(int id);
}
