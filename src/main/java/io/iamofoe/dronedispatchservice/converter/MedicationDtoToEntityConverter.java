package io.iamofoe.dronedispatchservice.converter;

import io.iamofoe.dronedispatchservice.dto.MedicationDto;
import io.iamofoe.dronedispatchservice.model.Medication;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicationDtoToEntityConverter implements Converter<MedicationDto, Medication> {
    private final MultipartToImageConverter toImageConverter;
    @Override
    public Medication convert(MedicationDto source) {
        return new Medication()
                .setCode(source.getCode())
                .setWeight(source.getWeight())
                .setImage(toImageConverter.convert(source.getImage()))
                .setName(source.getName());
    }
}
