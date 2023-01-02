package io.iamofoe.dronedispatchservice.converter;

import io.iamofoe.dronedispatchservice.dro.DroneDto;
import io.iamofoe.dronedispatchservice.model.Drone;
import io.iamofoe.dronedispatchservice.model.State;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DroneDtoToEntityConverter implements Converter<DroneDto, Drone> {
    @Override
    public Drone convert(DroneDto source) {
        return new Drone()
                .setSerialNumber(source.getSerialNumber())
                .setModel(source.getModel())
                .setWeightLimit(source.getWeightLimit())
                .setBatteryCapacity(100)
                .setState(State.IDLE);
    }
}
