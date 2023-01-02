package io.iamofoe.dronedispatchservice.dro;

import io.iamofoe.dronedispatchservice.model.Model;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
@Builder
public class DroneDto {
    String serialNumber;
    Model model;
    double weightLimit;
}
