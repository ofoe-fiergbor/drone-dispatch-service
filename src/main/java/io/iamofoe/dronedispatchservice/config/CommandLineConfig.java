package io.iamofoe.dronedispatchservice.config;

import io.iamofoe.dronedispatchservice.dto.DroneDto;
import io.iamofoe.dronedispatchservice.service.DroneService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static io.iamofoe.dronedispatchservice.model.Model.*;

@Configuration
public class CommandLineConfig {

    @Bean
    public CommandLineRunner loadDronesIntoDatabase(DroneService droneService) {
        DroneDto drone1 = DroneDto.builder().serialNumber("SERIAL-NUMBER-dI").model(HEAVYWEIGHT).weightLimit(500.00).build();
        DroneDto drone2 = DroneDto.builder().serialNumber("SERIAL-NUMBER-dII").model(LIGHTWEIGHT).weightLimit(150.00).build();
        DroneDto drone3 = DroneDto.builder().serialNumber("SERIAL-NUMBER-dIII").model(LIGHTWEIGHT).weightLimit(150.00).build();
        DroneDto drone4 = DroneDto.builder().serialNumber("SERIAL-NUMBER-dIV").model(HEAVYWEIGHT).weightLimit(500.00).build();
        DroneDto drone5 = DroneDto.builder().serialNumber("SERIAL-NUMBER-dV").model(MIDDLEWEIGHT).weightLimit(250.00).build();
        DroneDto drone6 = DroneDto.builder().serialNumber("SERIAL-NUMBER-dVI").model(CRUISERWEIGHT).weightLimit(350.00).build();
        DroneDto drone7 = DroneDto.builder().serialNumber("SERIAL-NUMBER-dVII").model(CRUISERWEIGHT).weightLimit(350.00).build();
        DroneDto drone8 = DroneDto.builder().serialNumber("SERIAL-NUMBER-dVIII").model(MIDDLEWEIGHT).weightLimit(250.00).build();
        DroneDto drone9 = DroneDto.builder().serialNumber("SERIAL-NUMBER-dIX").model(LIGHTWEIGHT).weightLimit(150.00).build();
        DroneDto drone0 = DroneDto.builder().serialNumber("SERIAL-NUMBER-dX").model(HEAVYWEIGHT).weightLimit(500.00).build();
        List<DroneDto> droneDtos = List.of(drone0, drone1, drone2, drone3, drone4, drone5, drone6, drone7, drone8, drone9);
        return args -> droneDtos.forEach(droneService::saveDrone);
    }
}
