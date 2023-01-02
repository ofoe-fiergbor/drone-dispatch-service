package io.iamofoe.dronedispatchservice.dto;

import io.iamofoe.dronedispatchservice.model.Model;
import io.iamofoe.dronedispatchservice.model.State;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
@Builder
public class DroneResponseDto {
    int id;
    String serialNumber;
    Model model;
    double weightLimit;
    State state;
}
