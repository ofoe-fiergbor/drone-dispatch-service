package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.dto.*;

public interface DispatchService {
    DroneResponseDto registerDrone(DroneDto drone);
    MedicationResponseDto loadMedication(int droneId, MedicationDto body);
    ImageDto downloadImage(String name);
}
