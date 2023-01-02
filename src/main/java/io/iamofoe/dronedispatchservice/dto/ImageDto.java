package io.iamofoe.dronedispatchservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
@Builder
public class ImageDto {
    String name;
    String contentType;
    byte[] imageData;
}
