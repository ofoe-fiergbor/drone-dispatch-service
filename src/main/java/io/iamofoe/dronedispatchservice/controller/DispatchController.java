package io.iamofoe.dronedispatchservice.controller;

import io.iamofoe.dronedispatchservice.dto.DroneResponseDto;
import io.iamofoe.dronedispatchservice.service.DispatchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/dispatch/drones")
@RequiredArgsConstructor
@Tag(name = "DispatchController", description = "REST API for dispatch controller.")
public class DispatchController {
    private final DispatchService dispatchService;

    @PostMapping
    public ResponseEntity<DroneResponseDto> handleDroneRegistration(
            @RequestParam(name = "serialNumber") String serialNumber
    ) {
        return new ResponseEntity<>(dispatchService.registerDrone(serialNumber), CREATED);
    }
}
