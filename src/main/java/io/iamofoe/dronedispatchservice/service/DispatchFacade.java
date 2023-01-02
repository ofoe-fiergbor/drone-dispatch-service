package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.dto.DroneResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DispatchFacade implements DispatchService{
    private final DroneService droneService;

    @Override
    public DroneResponseDto registerDrone(String serialNumber) {
        return droneService.registerDrone(serialNumber);
    }
}
