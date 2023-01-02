package io.iamofoe.dronedispatchservice.repository;

import io.iamofoe.dronedispatchservice.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MedicationRepository extends JpaRepository<Medication, Integer> {
    @Query(value = "select coalesce(sum(cast(weight AS decimal(18,2))), 0.0) from medications m where drone_id = :droneId", nativeQuery = true)
    double getTotalLoadWeightOnDrone(@Param("droneId") int droneId);
    Optional<Medication> findMedicationByCode(String code);
}
