package io.iamofoe.dronedispatchservice.repository;

import io.iamofoe.dronedispatchservice.model.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditEventRepository extends JpaRepository<AuditEvent, Integer> {
}