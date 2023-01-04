package io.iamofoe.dronedispatchservice.converter;

import io.iamofoe.dronedispatchservice.dto.MedicationDto;
import io.iamofoe.dronedispatchservice.model.Medication;
import io.iamofoe.dronedispatchservice.model.MedicationImage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {MedicationDtoToEntityConverter.class})
@ExtendWith(MockitoExtension.class)
class MedicationDtoToEntityConverterTest {

    @MockBean
    private MultipartToImageConverter toImageConverter = new MultipartToImageConverter();
    @Autowired
    private MedicationDtoToEntityConverter underTest = new MedicationDtoToEntityConverter(toImageConverter);

    @Test
    void shouldConvertMedicationDtoToEntity() {
        //GIVEN
        var imageData = new byte[5];
        MockMultipartFile mockMultipartFile = new MockMultipartFile("mock-mulitpart-file", "fileName.png", "image/jpeg", imageData);
        MedicationImage medicationImage = new MedicationImage().setName("fileName.png").setImageData(imageData).setType("image/jpeg");
        MedicationDto medicationDto = MedicationDto.builder().weight(100).name("name").code("CODE").image(mockMultipartFile).build();
        Medication medication = new Medication().setCode("CODE").setName("name").setWeight(100).setImage(medicationImage);
        //WHEN
        var result = underTest.convert(medicationDto);
        //THEN
        assertEquals(result.getName(), medication.getName());
        assertEquals(result.getWeight(), medication.getWeight());
        assertEquals(result.getCode(), medication.getCode());
        assertEquals(result.getImage().getImageData(), medication.getImage().getImageData());
    }

}