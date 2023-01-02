package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.converter.DroneDtoToEntityConverter;
import io.iamofoe.dronedispatchservice.converter.DroneToResponseDtoConverter;
import io.iamofoe.dronedispatchservice.dto.DroneDto;
import io.iamofoe.dronedispatchservice.dto.DroneResponseDto;
import io.iamofoe.dronedispatchservice.exception.BadRequestException;
import io.iamofoe.dronedispatchservice.exception.InvalidInputException;
import io.iamofoe.dronedispatchservice.exception.NotFoundException;
import io.iamofoe.dronedispatchservice.model.Drone;
import io.iamofoe.dronedispatchservice.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static io.iamofoe.dronedispatchservice.model.State.IDLE;
import static io.iamofoe.dronedispatchservice.model.State.LOADING;

@Service
@RequiredArgsConstructor
public class DroneRepositoryService implements DroneService {
    private static final Logger LOG = LoggerFactory.getLogger(DroneRepositoryService.class);
    private static final int MIN_BATTERY_CAPACITY = 25;
    private final DroneRepository repository;
    private final DroneDtoToEntityConverter toEntityConverter;
    private final DroneToResponseDtoConverter toResponseDtoConverter;

    @Override
    public void saveDrone(DroneDto drone) {
        if (getDroneBySerialNumber(drone.getSerialNumber()).isPresent()) {
            LOG.info("saveDrone: Drone with SerialNumber: {} already exist", drone.getSerialNumber());
            throw new InvalidInputException("Drone with SerialNumber: %s already exist".formatted(drone.getSerialNumber()));
        }
        Drone entity = toEntityConverter.convert(drone);
        repository.save(Objects.requireNonNull(entity));
        LOG.info("saveDrone: Drone with SerialNumber: {} saved", drone.getSerialNumber());
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
    public DroneResponseDto registerDrone(String serialNumber) {
        return getDroneBySerialNumber(serialNumber).map(drone -> {
            if (!drone.getState().equals(IDLE)) {
                LOG.debug("registerDrone: Drone with S/N: {} is already registered.", serialNumber);
                throw new BadRequestException("Drone with S/N: %s is already registered".formatted(serialNumber));
            }
            if (drone.getBatteryCapacity() < MIN_BATTERY_CAPACITY) {
                LOG.debug("registerDrone: Drone with S/N {} has battery capacity below {}.", serialNumber, MIN_BATTERY_CAPACITY);
                throw new BadRequestException("Drone with S/N %s has battery capacity below %s".formatted(serialNumber, MIN_BATTERY_CAPACITY));
            }
            drone.setState(LOADING);
            Drone registeredDrone = repository.save(drone);
            LOG.info("registerDrone: Drone with S/N: {} registered successfully.", serialNumber);
            return toResponseDtoConverter.convert(registeredDrone);
        }).orElseThrow(() -> {
            LOG.debug("registerDrone: Drone with S/N {} not found", serialNumber);
            throw new NotFoundException("Drone with S/N %s not found".formatted(serialNumber));
        });
    }
}
