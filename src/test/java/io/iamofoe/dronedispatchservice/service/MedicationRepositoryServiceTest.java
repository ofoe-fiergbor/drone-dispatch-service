package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.converter.MedicationToDtoConverter;
import io.iamofoe.dronedispatchservice.dto.MedicationResponseDto;
import io.iamofoe.dronedispatchservice.exception.BadRequestException;
import io.iamofoe.dronedispatchservice.exception.InvalidInputException;
import io.iamofoe.dronedispatchservice.model.*;
import io.iamofoe.dronedispatchservice.repository.MedicationImageRepository;
import io.iamofoe.dronedispatchservice.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {MedicationRepositoryService.class})
class MedicationRepositoryServiceTest {
    @Mock
    private MedicationRepository medicationRepository;
    @Mock
    private MedicationToDtoConverter toDtoConverter;
    @Mock
    private MedicationImageRepository imageRepository;
    @Autowired
    private MedicationRepositoryService underTest;
    private Drone drone;
    private MedicationImage medicationImage;
    private Medication medication;

    @BeforeEach
    void setUp() {
        underTest = new MedicationRepositoryService(medicationRepository, imageRepository, toDtoConverter);
        drone = new Drone()
                .setId(1)
                .setSerialNumber("given-s/n")
                .setWeightLimit(100)
                .setState(State.IDLE)
                .setModel(Model.LIGHTWEIGHT)
                .setBatteryCapacity(100);
        var imageData = new byte[5];
        medicationImage = new MedicationImage().setName("fileName.png").setImageData(imageData).setType("image/jpeg");
        medication = new Medication().setName("medication-name").setDrone(drone).setWeight(30).setImage(medicationImage).setCode("CODE-ABC");
    }

    @Test
    void shouldSaveMedicationSuccessfully() {
        //GIVEN
        MedicationResponseDto response = MedicationResponseDto.builder()
                .id(2)
                .name("medication-name")
                .weight(20)
                .code("CODE-ABC")
                .build();
        //WHEN
        when(medicationRepository.findMedicationByCode("CODE-ABC")).thenReturn(Optional.empty());
        when(underTest.getMedicationWeightForDrone(anyInt())).thenReturn(0.0);
        when(medicationRepository.save(medication)).thenReturn(medication);
        when(toDtoConverter.convert(medication)).thenReturn(response);
        //THEN
        var result = underTest.saveMedication(medication);
        assertEquals(result.getCode(), medication.getCode());
        assertEquals(result.getName(), medication.getName());
    }

    @Test
    void shouldThrowBadRequestException_whenMedicationCodeExist() {

        when(medicationRepository.findMedicationByCode("CODE-ABC")).thenReturn(Optional.of(medication));

        assertThrows(BadRequestException.class, () -> underTest.saveMedication(medication));
    }

    @Test
    void shouldThrowInvalidInputException_whenMedicationWillBeMoreThanDroneWeightLimit() {
        //WHEN
        when(medicationRepository.findMedicationByCode("CODE-ABC")).thenReturn(Optional.empty());
        when(underTest.getMedicationWeightForDrone(anyInt())).thenReturn(80.00);
        //THEN
        assertThrows(InvalidInputException.class, () -> underTest.saveMedication(medication));
    }

    @Test
    void shouldGetMedicationWeightForDrone() {
        int droneId = 1;
        when(medicationRepository.getTotalLoadWeightOnDrone(droneId)).thenReturn(35.00);
        assertThat(underTest.getMedicationWeightForDrone(droneId)).isEqualTo(35.00);
    }
}