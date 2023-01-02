package io.iamofoe.dronedispatchservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
@Builder
@Schema(name = "BatteryLevelDto")
public class BatteryLevelDto {
    int droneId;
    int batteryPercentage;
}
