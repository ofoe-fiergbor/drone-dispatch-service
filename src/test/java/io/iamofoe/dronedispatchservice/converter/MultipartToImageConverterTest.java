package io.iamofoe.dronedispatchservice.converter;

import io.iamofoe.dronedispatchservice.model.MedicationImage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {MultipartToImageConverter.class})
@ExtendWith(MockitoExtension.class)
class MultipartToImageConverterTest {

    @Autowired
    private MultipartToImageConverter underTest = new MultipartToImageConverter();

    @Test
    void shouldConvertMultipartToMedicationImage() {
        //GIVEN
        var imageData = new byte[5];
        MockMultipartFile mockMultipartFile = new MockMultipartFile("mock-mulitpart-file","fileName.png", "image/jpeg", imageData);
        MedicationImage medicationImage = new MedicationImage().setName("fileName.png").setType("image/jpeg").setImageData(imageData);
        //WHEN
        var result = underTest.convert(mockMultipartFile);
        //THEN
        assertEquals(result.getName(), medicationImage.getName());
        assertEquals(result.getType(), medicationImage.getType());
        assertEquals(result.getImageData(), medicationImage.getImageData());
    }

}