package io.iamofoe.dronedispatchservice.converter;

import io.iamofoe.dronedispatchservice.dto.DroneResponseDto;
import io.iamofoe.dronedispatchservice.model.Drone;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DroneToResponseDtoConverter implements Converter<Drone, DroneResponseDto> {
    @Override
    public DroneResponseDto convert(Drone source) {
        return DroneResponseDto.builder()
                .id(source.getId())
                .serialNumber(source.getSerialNumber())
                .model(source.getModel())
                .weightLimit(source.getWeightLimit())
                .batteryCapacity(source.getBatteryCapacity())
                .state(source.getState())
                .build();
    }
}
