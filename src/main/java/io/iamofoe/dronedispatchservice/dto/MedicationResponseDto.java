package io.iamofoe.dronedispatchservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
@Builder
@Schema(name = "MedicationDto")
public class MedicationResponseDto {
    int id;
    String name;
    double weight;
    String code;
    String image;
}
