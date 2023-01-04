package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.dto.*;

import java.util.List;

public interface DispatchService {
    DroneResponseDto registerDrone(DroneDto drone);
    MedicationResponseDto loadMedication(int droneId, MedicationDto body);
    ImageDto downloadImage(String name);
    List<MedicationResponseDto> getLoadedMedicationForGivenDrone(int droneId);
    List<DroneResponseDto> getAvailableDrones();
    List<DroneResponseDto> getAllDrones();
    BatteryLevelDto getBatteryLevelForDrone(int droneId);
    DroneResponseDto updateDrone(int droneId, DroneUpdateDto drone);

}
