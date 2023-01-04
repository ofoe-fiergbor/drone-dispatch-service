package io.iamofoe.dronedispatchservice.service;

import io.iamofoe.dronedispatchservice.model.AuditEvent;
import io.iamofoe.dronedispatchservice.model.EventType;
import io.iamofoe.dronedispatchservice.repository.AuditEventRepository;
import io.iamofoe.dronedispatchservice.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DroneBatteryCheckTask {
    private static final Logger LOG = LoggerFactory.getLogger(DroneBatteryCheckTask.class);
    private final DroneRepository droneRepository;
    private final AuditEventRepository auditEventRepository;

    @Scheduled(fixedDelay = 60_000 * 30) //EVERY 30 MIN
    public void checkBatteryLevels() {
        var drones = droneRepository.findAll();
        List<AuditEvent> events = new ArrayList<>();
        drones.forEach(drone -> {
            var event = new AuditEvent().setType(EventType.BATTERY_LEVEL)
                    .setTimestamp(ZonedDateTime.now())
                    .setDescription("Battery level for drone: %s with S/N: %s is at %s%%".formatted(drone.getId(), drone.getSerialNumber(), drone.getBatteryCapacity()));
            events.add(event);
            LOG.info("checkBatteryLevels: Battery level for drone: {} with S/N: {} is at {}%", drone.getId(), drone.getSerialNumber(), drone.getBatteryCapacity());
        });
        auditEventRepository.saveAll(events);
    }
}
