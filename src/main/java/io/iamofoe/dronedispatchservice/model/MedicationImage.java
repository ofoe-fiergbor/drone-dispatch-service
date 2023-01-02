package io.iamofoe.dronedispatchservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "medications_images", indexes = {
        @Index(name = "idx_medication_image", columnList = "id, medication_id")
})
@AllArgsConstructor
public class MedicationImage {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String type;
    @Lob
    private byte[] imageData;
    @OneToOne
    private Medication medication;
}
