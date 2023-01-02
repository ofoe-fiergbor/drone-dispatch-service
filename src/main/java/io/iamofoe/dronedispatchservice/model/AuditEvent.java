package io.iamofoe.dronedispatchservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "audit_events")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuditEvent {
    @Id
    @GeneratedValue
    private int id;
    @Enumerated(EnumType.STRING)
    private EventType type;
    private String description;
    private ZonedDateTime timestamp;
}
