package io.iamofoe.dronedispatchservice.converter;

import io.iamofoe.dronedispatchservice.dto.DroneDto;
import io.iamofoe.dronedispatchservice.model.Drone;
import io.iamofoe.dronedispatchservice.model.Model;
import io.iamofoe.dronedispatchservice.model.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ContextConfiguration(classes = {DroneDtoToEntityConverter.class})
@ExtendWith(MockitoExtension.class)
class DroneDtoToEntityConverterTest {
    @Autowired
    private DroneDtoToEntityConverter underTest = new DroneDtoToEntityConverter();

    @Test
    void shouldConvertDroneDtoTOEntity() {

        //GIVEN
        DroneDto droneDto = DroneDto.builder()
                .serialNumber("given-s/n")
                .model(Model.HEAVYWEIGHT)
                .weightLimit(500)
                .build();
        Drone droneEntity = new Drone()
                .setSerialNumber("given-s/n")
                .setWeightLimit(500)
                .setState(State.IDLE)
                .setModel(Model.HEAVYWEIGHT)
                .setBatteryCapacity(100);

        //WHEN
        Drone result = underTest.convert(droneDto);
        //THEN
        assertEquals(droneEntity.getSerialNumber(), result.getSerialNumber());
        assertEquals(droneEntity.getWeightLimit(), result.getWeightLimit());
        assertEquals(droneEntity.getState(), result.getState());
        assertEquals(droneEntity.getBatteryCapacity(), result.getBatteryCapacity());
    }
}