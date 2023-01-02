package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.dto.ImageDto;
import io.iamofoe.dronedispatchservice.dto.MedicationResponseDto;
import io.iamofoe.dronedispatchservice.model.Medication;
import io.iamofoe.dronedispatchservice.model.MedicationImage;

public interface MedicationService {
    MedicationResponseDto saveMedication(Medication medication);
    void saveMedicationImage(MedicationImage image);
    ImageDto downloadImage(String name);
    double getMedicationWeightForDrone(int droneId);
}
