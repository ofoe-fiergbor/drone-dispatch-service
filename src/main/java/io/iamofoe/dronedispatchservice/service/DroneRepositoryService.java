package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.converter.DroneDtoToEntityConverter;
import io.iamofoe.dronedispatchservice.converter.DroneToResponseDtoConverter;
import io.iamofoe.dronedispatchservice.dto.BatteryLevelDto;
import io.iamofoe.dronedispatchservice.dto.DroneDto;
import io.iamofoe.dronedispatchservice.dto.DroneResponseDto;
import io.iamofoe.dronedispatchservice.dto.DroneUpdateDto;
import io.iamofoe.dronedispatchservice.exception.InvalidInputException;
import io.iamofoe.dronedispatchservice.exception.NotFoundException;
import io.iamofoe.dronedispatchservice.model.Drone;
import io.iamofoe.dronedispatchservice.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static io.iamofoe.dronedispatchservice.model.State.*;

@Service
@RequiredArgsConstructor
public class DroneRepositoryService implements DroneService {

    private static final Logger LOG = LoggerFactory.getLogger(DroneRepositoryService.class);
    private final DroneRepository repository;
    private final DroneDtoToEntityConverter toEntityConverter;
    private final DroneToResponseDtoConverter toResponseDtoConverter;

    @Override
    public DroneResponseDto saveDrone(DroneDto drone) {
        if (getDroneBySerialNumber(drone.getSerialNumber()).isPresent()) {
            LOG.info("saveDrone: Drone with SerialNumber: {} already exist", drone.getSerialNumber());
            throw new InvalidInputException("Drone with SerialNumber: %s already exist".formatted(drone.getSerialNumber()));
        }
        Drone entity = toEntityConverter.convert(drone);
        Drone savedEntity = repository.save(Objects.requireNonNull(entity));
        LOG.info("saveDrone: Drone with SerialNumber: {} saved", drone.getSerialNumber());
        return toResponseDtoConverter.convert(savedEntity);
    }

    @Override
    public Optional<Drone> getDroneBySerialNumber(String serialNumber) {
        return repository.getDroneBySerialNumber(serialNumber);
    }

    @Override
    public Optional<Drone> getDroneById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<DroneResponseDto> getAvailableDrones() {
        var criteria = Set.of(IDLE, LOADING, LOADED);
        return repository.findAll().stream()
                .filter(drone -> criteria.contains(drone.getState()))
                .map(toResponseDtoConverter::convert)
                .toList();
    }

    @Override
    public BatteryLevelDto getBatteryLevelForDrone(int droneId) {
        return getDroneById(droneId)
                .map(drone -> BatteryLevelDto.builder().batteryPercentage(drone.getBatteryCapacity()).droneId(droneId).build())
                .orElseThrow(() -> {
                    LOG.debug("getBatteryLevelForDrone: Drone with id: {} not found.", droneId);
                    throw new NotFoundException("Drone with id: %s not found.".formatted(droneId));
                });
    }

    @Override
    public DroneResponseDto updateDrone(int droneId, DroneUpdateDto drone) {
        return getDroneById(droneId).map(foundDrone -> {
            if (drone.getState().equals(IDLE) && drone.getBatteryCapacity() < 25) {
                throw new InvalidInputException("Drone battery capacity is below 20%");
            }
            foundDrone.setState(drone.getState());
            foundDrone.setBatteryCapacity(drone.getBatteryCapacity());
            Drone updatedDrone = repository.save(foundDrone);
            LOG.info("updateDrone: Drone with id: {} updated successfully.", droneId);
            return toResponseDtoConverter.convert(updatedDrone);
        }).orElseThrow(() -> {
            LOG.debug("updateDrone: Drone with id: {} not found.", droneId);
            throw new NotFoundException("Drone with id: %s not found".formatted(droneId));
        });
    }

    @Override
    public List<DroneResponseDto> getAllDrones() {
        return repository.findAll().stream().map(toResponseDtoConverter::convert).toList();
    }
}
