package io.iamofoe.dronedispatchservice.converter;

import io.iamofoe.dronedispatchservice.dto.DroneResponseDto;
import io.iamofoe.dronedispatchservice.model.Drone;
import io.iamofoe.dronedispatchservice.model.Model;
import io.iamofoe.dronedispatchservice.model.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {DroneToResponseDtoConverter.class})
@ExtendWith(MockitoExtension.class)
class DroneToResponseDtoConverterTest {
    @Autowired
    private DroneToResponseDtoConverter underTest = new DroneToResponseDtoConverter();

    @Test
    void shouldConvertDroneEntityToResponseDto() {
        //GIVEN
        Drone droneEntity = new Drone()
                .setSerialNumber("given-s/n")
                .setWeightLimit(500)
                .setState(State.IDLE)
                .setModel(Model.HEAVYWEIGHT)
                .setBatteryCapacity(100);
        DroneResponseDto responseDto = DroneResponseDto.builder()
                .serialNumber("given-s/n")
                .model(Model.HEAVYWEIGHT)
                .weightLimit(500)
                .state(State.IDLE)
                .build();
        //WHEN
        DroneResponseDto result = underTest.convert(droneEntity);
        //THEN
        assertEquals(result.getSerialNumber(), responseDto.getSerialNumber());
        assertEquals(result.getModel(), responseDto.getModel());
        assertEquals(result.getState(), responseDto.getState());
        assertEquals(result.getWeightLimit(), responseDto.getWeightLimit());
    }
}