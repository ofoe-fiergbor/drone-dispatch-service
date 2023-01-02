package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.dto.DroneResponseDto;

public interface DispatchService {
    DroneResponseDto registerDrone(String serialNumber);
}
