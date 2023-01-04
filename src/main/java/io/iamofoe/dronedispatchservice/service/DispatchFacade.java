package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.converter.MedicationDtoToEntityConverter;
import io.iamofoe.dronedispatchservice.converter.MedicationToDtoConverter;
import io.iamofoe.dronedispatchservice.dto.*;
import io.iamofoe.dronedispatchservice.exception.InvalidInputException;
import io.iamofoe.dronedispatchservice.exception.NotFoundException;
import io.iamofoe.dronedispatchservice.model.Drone;
import io.iamofoe.dronedispatchservice.model.Medication;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static io.iamofoe.dronedispatchservice.model.State.*;

@Service
@RequiredArgsConstructor
public class DispatchFacade implements DispatchService {
    private static final Logger LOG = LoggerFactory.getLogger(DispatchFacade.class);
    private static final int MIN_DRONE_BATTERY_LEVEL = 25;

    private final DroneService droneService;
    private final MedicationService medicationService;
    private final MedicationDtoToEntityConverter toEntityConverter;
    private final MedicationToDtoConverter toDtoConverter;

    @Override
    public DroneResponseDto registerDrone(DroneDto drone) {
        return droneService.saveDrone(drone);
    }

    @Override
    public MedicationResponseDto loadMedication(int droneId, MedicationDto body) {
        var availableCriteria = new HashSet<>(Arrays.asList(LOADING, IDLE));
        return droneService.getDroneById(droneId).map(drone -> {
            Medication medication = toEntityConverter.convert(body);
            if (!hasEnoughBatteryPower(drone)) {
                LOG.debug("loadMedication: Drone: {}'s battery level is below {}", droneId, MIN_DRONE_BATTERY_LEVEL);
                throw new InvalidInputException("Drone: %s's battery level is below %s".formatted(droneId, MIN_DRONE_BATTERY_LEVEL));
            }
            if (!availableCriteria.contains(drone.getState())) {
                LOG.debug("loadMedication: Drone: {} is not available for loading", droneId);
                throw new InvalidInputException("loadMedication: Drone: %s is not available for loading".formatted(droneId));
            }
            drone.getMedications().add(medication);
            drone.setState(LOADING);

            if (getExpectedLoadWeight(droneId, medication) == drone.getWeightLimit()) {
                drone.setState(LOADED);
            }
            medication.setDrone(drone);
            return medicationService.saveMedication(medication);
        }).orElseThrow(() -> {
            LOG.debug("loadMedication: Drone with Id: {} not found", droneId);
            throw new NotFoundException("Drone with Id: %s not found".formatted(droneId));
        });
    }

    private double getExpectedLoadWeight(int droneId, Medication medication) {
        return Math.ceil(medicationService.getMedicationWeightForDrone(droneId) + medication.getWeight());
    }

    @Override
    public ImageDto downloadImage(String name) {
        return medicationService.downloadImage(name);
    }

    @Override
    public List<MedicationResponseDto> getLoadedMedicationForGivenDrone(int droneId) {
        return droneService.getDroneById(droneId).map(drone -> {
                    LOG.info("loadMedication: {} medication(s) found for drone:{}", drone.getMedications().size(), droneId);
                    return drone.getMedications().stream().map(toDtoConverter::convert).toList();
                })
                .orElseThrow(() -> {
                    LOG.debug("loadMedication: Drone with Id: {} not found", droneId);
                    throw new NotFoundException("Drone with Id: %s not found".formatted(droneId));
                });
    }

    @Override
    public List<DroneResponseDto> getAvailableDrones() {
        return droneService.getAvailableDrones().stream()
                .map(dto -> {
                    if (dto.getState().equals(LOADED) &&
                            medicationService.getMedicationWeightForDrone(dto.getId()) < dto.getWeightLimit()) {
                        return dto;
                    }
                    if (dto.getState().equals(LOADED)) {
                        return null;
                    }
                    return dto;
                }).filter(Objects::nonNull).toList();
    }

    @Override
    public List<DroneResponseDto> getAllDrones() {
        return droneService.getAllDrones();
    }

    @Override
    public BatteryLevelDto getBatteryLevelForDrone(int droneId) {
        return droneService.getBatteryLevelForDrone(droneId);
    }

    @Override
    public DroneResponseDto updateDrone(int droneId, DroneUpdateDto drone) {
        return droneService.updateDrone(droneId, drone);
    }

    private boolean hasEnoughBatteryPower(Drone drone) {
        return drone.getBatteryCapacity() >= MIN_DRONE_BATTERY_LEVEL;
    }
}
