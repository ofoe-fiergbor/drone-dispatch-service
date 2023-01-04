package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.converter.MedicationToDtoConverter;
import io.iamofoe.dronedispatchservice.dto.ImageDto;
import io.iamofoe.dronedispatchservice.dto.MedicationResponseDto;
import io.iamofoe.dronedispatchservice.exception.BadRequestException;
import io.iamofoe.dronedispatchservice.exception.InvalidInputException;
import io.iamofoe.dronedispatchservice.exception.NotFoundException;
import io.iamofoe.dronedispatchservice.model.Medication;
import io.iamofoe.dronedispatchservice.model.MedicationImage;
import io.iamofoe.dronedispatchservice.repository.MedicationImageRepository;
import io.iamofoe.dronedispatchservice.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MedicationRepositoryService implements MedicationService {
    private static final Logger LOG = LoggerFactory.getLogger(MedicationRepositoryService.class);

    private final MedicationRepository medicationRepository;
    private final MedicationImageRepository imageRepository;
    private final MedicationToDtoConverter toResponseConverter;

    @Override
    public MedicationResponseDto saveMedication(Medication medication) {
        double currUsedSpace = getMedicationWeightForDrone(medication.getDrone().getId());
        if (medicationRepository.findMedicationByCode(medication.getCode()).isPresent()) {
            LOG.debug("saveMedication: Medication with code: {} already exists", medication.getCode());
            throw new BadRequestException("Medication with code: %s already exists".formatted(medication.getCode()));
        }
        if (currUsedSpace + medication.getWeight() > medication.getDrone().getWeightLimit()) {
            LOG.debug("saveMedication: Adding this medication with code: {} will exceed the weight of the drone", medication.getCode());
            throw new InvalidInputException("Adding this medication with code: %s will exceed the weight of the drone".formatted(medication.getCode()));
        }
        Medication savedMedication = medicationRepository.save(Objects.requireNonNull(medication));
        LOG.info("saveMedication: Medication with code: {} has been loaded onto drone with id: {}", medication.getCode(), medication.getDrone().getId());
        saveMedicationImage(medication.getImage().setMedication(savedMedication));
        return toResponseConverter.convert(savedMedication);
    }

    @Override
    public void saveMedicationImage(MedicationImage image) {
        imageRepository.save(image);
    }

    @Override
    public ImageDto downloadImage(String name) {
        return imageRepository.findByName(name)
                .map(image -> {
                    LOG.info("downloadImage: Image with name: {} successfully retrieved", name);
                    return ImageDto.builder().name(image.getName()).imageData(image.getImageData()).contentType(image.getType()).build();
                })
                .orElseThrow(() -> {
                    LOG.debug("downloadImage: Image with name: {} not found", name);
                    throw new NotFoundException("Image with name: %s not found.".formatted(name));
                });
    }

    @Override
    public double getMedicationWeightForDrone(int droneId) {
        return medicationRepository.getTotalLoadWeightOnDrone(droneId);
    }
}
