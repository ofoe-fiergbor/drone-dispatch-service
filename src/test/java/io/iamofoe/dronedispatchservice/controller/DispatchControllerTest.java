package io.iamofoe.dronedispatchservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.iamofoe.dronedispatchservice.dto.*;
import io.iamofoe.dronedispatchservice.service.DispatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static io.iamofoe.dronedispatchservice.model.Model.HEAVYWEIGHT;
import static io.iamofoe.dronedispatchservice.model.Model.LIGHTWEIGHT;
import static io.iamofoe.dronedispatchservice.model.State.DELIVERING;
import static io.iamofoe.dronedispatchservice.model.State.IDLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DispatchController.class})
@WebMvcTest(controllers = DispatchController.class)
class DispatchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DispatchService dispatchService;
    private final String URI = "/api/v1/dispatch/drones";

    @Test
    void shouldGetAllDrones() throws Exception {
        DroneResponseDto responseDto = DroneResponseDto.builder().id(1).serialNumber("S/N-1").weightLimit(500).model(HEAVYWEIGHT).state(IDLE).build();

        when(dispatchService.getAllDrones()).thenReturn(List.of(responseDto));
        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(List.of(responseDto))));
    }
    @Test
    void shouldRegisterDroneSuccessfully() throws Exception {
        //GIVEN
        DroneDto input = DroneDto.builder().serialNumber("S/N-1").model(HEAVYWEIGHT).weightLimit(500).build();
        DroneResponseDto responseDto = DroneResponseDto.builder().id(1).serialNumber("S/N-1").weightLimit(500).model(HEAVYWEIGHT).state(IDLE).build();
        //WHEN
        when(dispatchService.registerDrone(input)).thenReturn(responseDto);
        mockMvc.perform(post(URI)
                        .content(objectMapper.writeValueAsString(input))
                        .contentType(APPLICATION_JSON))
                //THEN
                .andExpect(status().isCreated())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void shouldThrowBadRequestException_whenDoesNotMeetValidation() throws Exception {
        //GIVEN
        DroneDto input = DroneDto.builder().model(HEAVYWEIGHT).weightLimit(600).build();
        //WHEN
        mockMvc.perform(post(URI)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                //THEN
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateDroneSuccessfully() throws Exception {
        //GIVEN
        int droneId = 1;
        DroneUpdateDto updateDto = DroneUpdateDto.builder().batteryCapacity(70).state(DELIVERING).build();
        DroneResponseDto responseDto = DroneResponseDto.builder().id(1).serialNumber("S/N-1").weightLimit(500).model(HEAVYWEIGHT).state(IDLE).build();
        //THEN
        when(dispatchService.updateDrone(droneId, updateDto)).thenReturn(responseDto);
        mockMvc.perform(put("%s/%s".formatted(URI, droneId))
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void shouldThrowBadRequestException_whenInputHasWrongValidation() throws Exception {
        //GIVEN
        int droneId = 1;
        DroneUpdateDto updateDto = DroneUpdateDto.builder().batteryCapacity(120).state(DELIVERING).build();
        //THEN
        mockMvc.perform(put("%s/%s".formatted(URI, droneId))
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowBadRequestException_whenDroneIdIsOfWrongType() throws Exception {
        //GIVEN
        String droneId = "id-1";
        DroneUpdateDto updateDto = DroneUpdateDto.builder().batteryCapacity(70).state(DELIVERING).build();
        //WHEN
        mockMvc.perform(put("%s/%s".formatted(URI, droneId))
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(APPLICATION_JSON))
                //THEN
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnAvailableDronesSuccessfully() throws Exception {
        //GIVEN
        var drones = List.of(DroneResponseDto.builder().id(1).weightLimit(200).state(IDLE).model(LIGHTWEIGHT).build());
        //WHEN
        when(dispatchService.getAvailableDrones()).thenReturn(drones);
        mockMvc.perform(get("%s/available".formatted(URI)))
                //THEN
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(drones)));
    }

    @Test
    void shouldCheckForBatteryLevelSuccessfully() throws Exception {
        //GIVEN
        int droneId = 1;
        BatteryLevelDto response = BatteryLevelDto.builder().batteryPercentage(30).droneId(1).build();
        //WHEN
        when(dispatchService.getBatteryLevelForDrone(1)).thenReturn(response);
        mockMvc.perform(get("%s/%s/battery-level".formatted(URI, droneId)))
                //THEN
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(response)));
    }

    @Test
    void shouldCheckLoadedMedicationOnADroneSuccessfully() throws Exception {
        //GIVEN
        int droneId = 1;
        List<MedicationResponseDto> medications = List.of(MedicationResponseDto.builder().build());
        //WHEN
        when(dispatchService.getLoadedMedicationForGivenDrone(droneId)).thenReturn(medications);
        mockMvc.perform(get("%s/%s/medications".formatted(URI, droneId)))
                //THEN
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(medications)));

    }
}