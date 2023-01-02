package io.iamofoe.dronedispatchservice.repository;

import io.iamofoe.dronedispatchservice.model.MedicationImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicationImageRepository extends JpaRepository<MedicationImage, Integer> {
    Optional<MedicationImage> findByName(String name);
}
