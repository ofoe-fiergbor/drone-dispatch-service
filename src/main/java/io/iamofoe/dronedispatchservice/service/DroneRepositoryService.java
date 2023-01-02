package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.converter.DroneDtoToEntityConverter;
import io.iamofoe.dronedispatchservice.converter.DroneToResponseDtoConverter;
import io.iamofoe.dronedispatchservice.dto.BatteryLevelDto;
import io.iamofoe.dronedispatchservice.dto.DroneDto;
import io.iamofoe.dronedispatchservice.dto.DroneResponseDto;
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
        var criteria = new HashSet<>(Arrays.asList(IDLE, LOADING, LOADED));
        return repository.findAll().stream()
                .filter(drone -> criteria.contains(drone.getState()))
                .map(toResponseDtoConverter::convert)
                .toList();
    }

    @Override
    public BatteryLevelDto getBatteryLevelForDrone(int droneId) {
        return getDroneById(droneId)
                .map(drone -> BatteryLevelDto.builder().batteryPercentage(drone.getBatteryCapacity()).droneId(droneId).build())
                .orElseThrow(() -> new NotFoundException("Drone with id: %s not found".formatted(droneId)));
    }
}
