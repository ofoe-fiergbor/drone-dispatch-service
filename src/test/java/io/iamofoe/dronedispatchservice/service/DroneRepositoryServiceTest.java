package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.converter.DroneDtoToEntityConverter;
import io.iamofoe.dronedispatchservice.converter.DroneToResponseDtoConverter;
import io.iamofoe.dronedispatchservice.dto.DroneDto;
import io.iamofoe.dronedispatchservice.dto.DroneResponseDto;
import io.iamofoe.dronedispatchservice.dto.DroneUpdateDto;
import io.iamofoe.dronedispatchservice.exception.InvalidInputException;
import io.iamofoe.dronedispatchservice.exception.NotFoundException;
import io.iamofoe.dronedispatchservice.model.Drone;
import io.iamofoe.dronedispatchservice.model.Model;
import io.iamofoe.dronedispatchservice.model.State;
import io.iamofoe.dronedispatchservice.repository.DroneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {DroneRepositoryService.class})
class DroneRepositoryServiceTest {
    @Mock
    private DroneToResponseDtoConverter toDtoConverter;
    @Mock
    private DroneDtoToEntityConverter toEntityConverter;
    @Mock
    private DroneRepository repository;
    @Autowired
    private DroneRepositoryService underTest;

    private DroneDto body;
    private Drone drone;
    private DroneResponseDto responseDto;

    @BeforeEach
    void setUp() {
        //GIVEN
        underTest = new DroneRepositoryService(repository, toEntityConverter, toDtoConverter);
        body = DroneDto.builder()
                .serialNumber("given-s/n")
                .model(Model.LIGHTWEIGHT)
                .weightLimit(100)
                .build();
        drone = new Drone()
                .setId(1)
                .setSerialNumber("given-s/n")
                .setWeightLimit(100)
                .setState(State.IDLE)
                .setModel(Model.LIGHTWEIGHT)
                .setBatteryCapacity(100);
        responseDto = DroneResponseDto.builder()
                .id(1)
                .serialNumber("given-s/n")
                .model(Model.HEAVYWEIGHT)
                .state(State.IDLE)
                .batteryCapacity(100)
                .weightLimit(100)
                .build();
    }

    @Test
    void shouldRegisterDroneSuccessfully() {
        //WHEN
        when(underTest.getDroneBySerialNumber(anyString())).thenReturn(Optional.empty());
        when(toEntityConverter.convert(body)).thenReturn(drone);
        when(toDtoConverter.convert(drone)).thenReturn(responseDto);
        when(repository.save(drone)).thenReturn(drone);
        //THEN
        DroneResponseDto result = underTest.saveDrone(body);
        assertEquals(result, responseDto);
    }

    @Test
    void shouldThrowInvalidInputException_whenSerialNumberAlreadyExist() {
        //WHEN
        when(underTest.getDroneBySerialNumber(anyString())).thenReturn(Optional.of(drone));
        //THEN
        assertThrows(InvalidInputException.class, () -> underTest.saveDrone(body));
    }

    @Test
    void shouldGetAllAvailableDrones() {
        //WHEN
        when(repository.findAll()).thenReturn(List.of(drone));
        when(toDtoConverter.convert(drone)).thenReturn(responseDto);

        var result = underTest.getAvailableDrones();
        //THEN
        assertThat(result.get(0)).isEqualTo(responseDto);
    }

    @Test
    void shouldGetNoAvailableDrone() {
        //GIVEN
        Drone drone2 = new Drone()
                .setSerialNumber("given-s/n")
                .setWeightLimit(100)
                .setState(State.RETURNING)
                .setModel(Model.LIGHTWEIGHT)
                .setBatteryCapacity(100);
        //WHEN
        when(repository.findAll()).thenReturn(List.of(drone2));

        var result = underTest.getAvailableDrones();
        //THEN
        assertThat(0).isEqualTo(result.size());
    }

    @Test
    void shouldGetBatteryLevelForDrone() {
        when(underTest.getDroneById(1)).thenReturn(Optional.of(drone));

        var result = underTest.getBatteryLevelForDrone(1);

        assertThat(result.getBatteryPercentage()).isEqualTo(drone.getBatteryCapacity());
        assertThat(result.getDroneId()).isEqualTo(drone.getId());
    }

    @Test
    void shouldThrowNotFoundException_whenDroneIdNotFound() {
        when(underTest.getDroneById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> underTest.getBatteryLevelForDrone(1));
    }

    @Test
    void shouldGetAllDrones() {
        when(repository.findAll()).thenReturn(List.of(drone));
        when(toDtoConverter.convert(drone)).thenReturn(responseDto);
        assertThat(underTest.getAllDrones().get(0)).isEqualTo(responseDto);
        assertThat(underTest.getAllDrones().size()).isEqualTo(1);
    }
}