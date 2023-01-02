package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.converter.DroneDtoToEntityConverter;
import io.iamofoe.dronedispatchservice.dro.DroneDto;
import io.iamofoe.dronedispatchservice.exception.InvalidInputException;
import io.iamofoe.dronedispatchservice.model.Drone;
import io.iamofoe.dronedispatchservice.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DroneRepositoryService implements DroneService {
    private static final Logger LOG = LoggerFactory.getLogger(DroneRepositoryService.class);
    private final DroneRepository repository;
    private final DroneDtoToEntityConverter toEntityConverter;

    @Override
    public void saveDrone(DroneDto drone) {
        if (getDroneBySerialNumber(drone.getSerialNumber()).isPresent()) {
            LOG.info("DroneRepositoryService: Drone with SerialNumber: {} already exist", drone.getSerialNumber());
            throw new InvalidInputException("Drone with SerialNumber: %s already exist".formatted(drone.getSerialNumber()));
        }
        Drone entity = toEntityConverter.convert(drone);
        repository.save(Objects.requireNonNull(entity));
        LOG.info("DroneRepositoryService: Drone with SerialNumber: {} saved", drone.getSerialNumber());
    }

    @Override
    public Optional<Drone> getDroneBySerialNumber(String serialNumber) {
        return repository.getDroneBySerialNumber(serialNumber);
    }

    @Override
    public Optional<Drone> getDroneById(int id) {
        return repository.findById(id);
    }
}
