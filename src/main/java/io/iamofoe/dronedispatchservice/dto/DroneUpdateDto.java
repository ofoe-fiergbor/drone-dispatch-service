package io.iamofoe.dronedispatchservice.dto;

import io.iamofoe.dronedispatchservice.model.State;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Value
@Getter
@Builder
@Schema(name = "DroneUpdateDto")
public class DroneUpdateDto {
    @NotNull @Range(min = 0, max = 100)
    int batteryCapacity;
    @NotNull
    State state;
}
