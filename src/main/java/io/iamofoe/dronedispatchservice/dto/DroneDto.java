package io.iamofoe.dronedispatchservice.dto;

import io.iamofoe.dronedispatchservice.model.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Getter
@Builder
@Schema(name = "DroneDto")
public class DroneDto {
    @NotNull
    @Size(max = 100)
    String serialNumber;
    @NotNull
    Model model;
    @NotNull @Max(500) @Min(0)
    double weightLimit;
}
