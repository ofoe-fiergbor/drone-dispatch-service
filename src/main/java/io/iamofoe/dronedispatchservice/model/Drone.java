package io.iamofoe.dronedispatchservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "drones")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Drone {
    @Id
    @GeneratedValue
    private int id;
    @Version
    private int version;
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String serialNumber;
    @Enumerated(EnumType.STRING)
    private Model model;
    @Max(500)
    @Column(nullable = false)
    private double weightLimit;
    @Range(min = 0, max = 100)
    @Column(nullable = false)
    private int batteryCapacity;
    @Enumerated(EnumType.STRING)
    private State state;
    @OneToMany(mappedBy = "drone")
    private Set<Medication> medication = new HashSet<>();
}
