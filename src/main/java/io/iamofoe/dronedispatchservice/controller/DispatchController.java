package io.iamofoe.dronedispatchservice.controller;

import io.iamofoe.dronedispatchservice.dto.DroneDto;
import io.iamofoe.dronedispatchservice.dto.DroneResponseDto;
import io.iamofoe.dronedispatchservice.dto.MedicationDto;
import io.iamofoe.dronedispatchservice.dto.MedicationResponseDto;
import io.iamofoe.dronedispatchservice.service.DispatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/dispatch/drones")
@RequiredArgsConstructor
@Tag(name = "DispatchController", description = "REST API for dispatch controller.")
public class DispatchController {
    private final DispatchService dispatchService;

    @PostMapping
    @Operation(summary = "Register a drone")
    public ResponseEntity<DroneResponseDto> handleDroneRegistration(
            @Valid @RequestBody DroneDto drone
    ) {
        return new ResponseEntity<>(dispatchService.registerDrone(drone), CREATED);
    }

    @GetMapping("/available")
    @Operation(summary = "Check for available drones")
    public ResponseEntity<List<DroneResponseDto>> handleCheckForAvailableDrones() {
        return new ResponseEntity<>(dispatchService.getAvailableDrones(), OK);
    }
    @PostMapping(value = "/{droneId}/medications", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    @Operation(summary = "Load medication onto drone")
    public ResponseEntity<MedicationResponseDto> handleMedicationLoad(@PathVariable int droneId,
                                                                      @RequestParam @Pattern(regexp = "([A-Za-z0-9\\-_]+)") String name,
                                                                      @RequestParam @Min(0) double weight,
                                                                      @RequestParam @Pattern(regexp = "([A-Z0-9\\-]+)") String code,
                                                                      @RequestParam MultipartFile image
    ) {
        MedicationDto body = MedicationDto.builder().code(code).image(image).weight(weight).name(name).build();
        return new ResponseEntity<>(dispatchService.loadMedication(droneId, body), CREATED);
    }
    @GetMapping("/{droneId}/medications")
    @Operation(summary = "Check loaded medications on a drone")
    public ResponseEntity<List<MedicationResponseDto>> handleCheckingLoadedMedications(@PathVariable int droneId) {
        return new ResponseEntity<>(dispatchService.getLoadedMedicationForGivenDrone(droneId), OK);
    }

    @GetMapping("/medications/img/{name}")
    @Operation(summary = "Download an image")
    public ResponseEntity<Resource> handleImageDownload(@PathVariable String name) {
        var res = dispatchService.downloadImage(name);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + res.getName());
        headers.add(HttpHeaders.CONTENT_TYPE, res.getContentType());
        return new ResponseEntity<>(new ByteArrayResource(res.getImageData()), headers, OK);
    }
}
