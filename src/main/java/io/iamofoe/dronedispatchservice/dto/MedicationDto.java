package io.iamofoe.dronedispatchservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
@Getter
@Builder
@Schema(name = "MedicationDto")
public class MedicationDto {
    @Pattern(regexp = "([A-Za-z0-9\\-_]+)")
    String name;
    @NotNull
    @Min(0)
    double weight;
    @Pattern(regexp = "([A-Z0-9\\-]+)")
    String code;

    MultipartFile image;
}