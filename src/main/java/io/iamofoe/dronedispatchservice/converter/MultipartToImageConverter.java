package io.iamofoe.dronedispatchservice.converter;

import io.iamofoe.dronedispatchservice.exception.InvalidInputException;
import io.iamofoe.dronedispatchservice.model.MedicationImage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class MultipartToImageConverter implements Converter<MultipartFile, MedicationImage> {
    @Override
    public MedicationImage convert(MultipartFile source) {
        try {
            return new MedicationImage()
                    .setType(source.getContentType())
                    .setName(source.getOriginalFilename())
                    .setImageData(source.getBytes());
        } catch (IOException e) {
            throw new InvalidInputException("Could not process file: %s".formatted(source.getOriginalFilename()));
        }
    }
}
