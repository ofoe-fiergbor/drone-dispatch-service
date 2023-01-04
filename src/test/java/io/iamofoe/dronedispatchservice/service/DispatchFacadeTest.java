package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.converter.MedicationDtoToEntityConverter;
import io.iamofoe.dronedispatchservice.converter.MedicationToDtoConverter;
import io.iamofoe.dronedispatchservice.dto.*;
import io.iamofoe.dronedispatchservice.model.Drone;
import io.iamofoe.dronedispatchservice.model.Medication;
import io.iamofoe.dronedispatchservice.model.MedicationImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.iamofoe.dronedispatchservice.model.Model.HEAVYWEIGHT;
import static io.iamofoe.dronedispatchservice.model.State.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {DispatchFacade.class})
class DispatchFacadeTest {
    @Mock
    private DroneService droneService;
    @Mock
    private MedicationService medicationService;
    @Mock
    private MedicationDtoToEntityConverter toEntityConverter;
    @Mock
    private MedicationToDtoConverter toDtoConverter;
    @Autowired
    private DispatchFacade underTest;
    private DroneResponseDto droneResponseDto;
    private DroneDto droneDto;
    private DroneUpdateDto droneUpdateDto;

    @BeforeEach
    void setUp() {
        underTest = new DispatchFacade(droneService, medicationService, toEntityConverter, toDtoConverter);
        droneResponseDto = DroneResponseDto.builder().id(1).serialNumber("S/N-1").weightLimit(500).model(HEAVYWEIGHT).state(IDLE).build();
        droneDto = DroneDto.builder().serialNumber("S/N-1").model(HEAVYWEIGHT).weightLimit(500).build();
        droneUpdateDto = DroneUpdateDto.builder().batteryCapacity(70).state(LOADED).build();
    }

    @Test
    void shouldGetAllDrones(){
        when(droneService.getAllDrones()).thenReturn(List.of(droneResponseDto));
        assertThat(underTest.getAllDrones().get(0)).isEqualTo(droneResponseDto);
        assertThat(underTest.getAllDrones().size()).isEqualTo(1);
    }

    @Test
    void shouldRegisterDroneSuccessfully() {
        when(droneService.saveDrone(droneDto)).thenReturn(droneResponseDto);
        assertThat(droneResponseDto).isEqualTo(underTest.registerDrone(droneDto));
    }

    @Test
    void shouldUpdateDroneSuccessfully () {
        int droneId = 1;
        DroneResponseDto updatedDrone = DroneResponseDto.builder().id(1).serialNumber("S/N-1").weightLimit(500).batteryCapacity(70).state(LOADED).build();
        when(droneService.updateDrone(droneId, droneUpdateDto)).thenReturn(updatedDrone);
        assertThat(updatedDrone).isEqualTo(underTest.updateDrone(droneId, droneUpdateDto));
    }

    @Test
    void shouldGetAllAvailableDrones() {
        DroneResponseDto droneResponseDto2 =  DroneResponseDto.builder().id(1).serialNumber("S/N-1").weightLimit(500).model(HEAVYWEIGHT).state(LOADED).build();
        when(droneService.getAvailableDrones()).thenReturn(List.of(droneResponseDto2));
        when(medicationService.getMedicationWeightForDrone(1)).thenReturn(100.00);

        assertThat(droneResponseDto2).isEqualTo(underTest.getAvailableDrones().get(0));
    }
    @Test
    void shouldGetNoAvailableDrones() {
        DroneResponseDto droneResponseDto2 =  DroneResponseDto.builder().id(1).serialNumber("S/N-1").weightLimit(500).model(HEAVYWEIGHT).state(LOADED).build();
        when(droneService.getAvailableDrones()).thenReturn(List.of(droneResponseDto2));
        when(medicationService.getMedicationWeightForDrone(1)).thenReturn(500.00);

        assertThat(0).isEqualTo(underTest.getAvailableDrones().size());
    }

    @Test
    void shouldGetBatteryLevelForDrone() {
        int droneId = 1;
        BatteryLevelDto batteryLevelDto = BatteryLevelDto.builder().droneId(droneId).batteryPercentage(70).build();
        when(droneService.getBatteryLevelForDrone(droneId)).thenReturn(batteryLevelDto);

        assertThat(batteryLevelDto).isEqualTo(underTest.getBatteryLevelForDrone(droneId));
    }
}