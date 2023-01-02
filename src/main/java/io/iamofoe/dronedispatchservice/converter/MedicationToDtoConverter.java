package io.iamofoe.dronedispatchservice.converter;

import io.iamofoe.dronedispatchservice.dto.MedicationResponseDto;
import io.iamofoe.dronedispatchservice.model.Medication;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MedicationToDtoConverter implements Converter<Medication, MedicationResponseDto> {
    @Override
    public MedicationResponseDto convert(Medication source) {
        return MedicationResponseDto.builder()
                .id(source.getId())
                .code(source.getCode())
                .name(source.getName())
                .image(source.getImage().getName())
                .weight(source.getWeight())
                .build();
    }
}
