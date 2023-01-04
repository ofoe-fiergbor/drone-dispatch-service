package io.iamofoe.dronedispatchservice.converter;

import io.iamofoe.dronedispatchservice.dto.MedicationResponseDto;
import io.iamofoe.dronedispatchservice.model.Medication;
import io.iamofoe.dronedispatchservice.model.MedicationImage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ContextConfiguration(classes = {MedicationToDtoConverter.class})
@ExtendWith(MockitoExtension.class)
class MedicationToDtoConverterTest {

    @Autowired
    private MedicationToDtoConverter underTest = new MedicationToDtoConverter();

    @Test
    void shouldConvertMedicationEntityToDto() {
        //GIVEN
        MedicationImage image = new MedicationImage().setName("image-name").setId(2);
        Medication medication = new Medication().setName("name").setCode("CODE").setImage(image).setId(1);
        MedicationResponseDto responseDto = MedicationResponseDto.builder().name("name").image("image-name").id(1).code("CODE").build();
        //WHEN
        var result = underTest.convert(medication);

        assertEquals(result.getId(), responseDto.getId());
        assertEquals(result.getName(), responseDto.getName());
        assertEquals(result.getCode(), responseDto.getCode());
    }

}