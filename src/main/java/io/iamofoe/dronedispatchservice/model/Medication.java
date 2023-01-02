package io.iamofoe.dronedispatchservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

import static javax.persistence.CascadeType.ALL;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "medications", indexes = {
        @Index(name = "idx_medication_drone", columnList = "id, drone_id")
})
@AllArgsConstructor
public class Medication {

    @Id
    @GeneratedValue
    private int id;

    @Pattern(regexp = "([A-Za-z0-9\\-_]+)")
    private String name;

    private double weight;

    @Pattern(regexp = "([A-Z0-9\\-]+)")
    @Column(unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "drone_id")
    private Drone drone;
    @OneToOne(mappedBy = "medication", cascade = ALL, orphanRemoval = true)
    private MedicationImage image;
}
