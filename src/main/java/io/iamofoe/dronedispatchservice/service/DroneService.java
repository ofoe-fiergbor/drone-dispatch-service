package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.dto.BatteryLevelDto;
import io.iamofoe.dronedispatchservice.dto.DroneDto;
import io.iamofoe.dronedispatchservice.dto.DroneResponseDto;
import io.iamofoe.dronedispatchservice.model.Drone;

import java.util.List;
import java.util.Optional;

public interface DroneService {
    DroneResponseDto saveDrone(DroneDto drone);
    Optional<Drone> getDroneBySerialNumber(String serialNumber);
    Optional<Drone> getDroneById(int id);
    List<DroneResponseDto> getAvailableDrones();
    BatteryLevelDto getBatteryLevelForDrone(int droneId);
}
