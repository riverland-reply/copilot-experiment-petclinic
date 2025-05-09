package org.springframework.samples.petclinic.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentRestController.class)
class AppointmentRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Appointment sampleAppointment;
    private Vet vet;
    private Pet pet;

    @BeforeEach
    void setup() {
        vet = new Vet();
        vet.setId(1);
        pet = new Pet();
        pet.setId(1);
        sampleAppointment = new Appointment();
        sampleAppointment.setId(1);
        sampleAppointment.setDate(LocalDate.now().plusDays(1));
        sampleAppointment.setTime(LocalTime.of(10, 0));
        sampleAppointment.setReason("Checkup");
        sampleAppointment.setVet(vet);
        sampleAppointment.setPet(pet);
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser
    void getUpcomingAppointments_shouldReturnUpcoming() throws Exception {
        Mockito.when(appointmentService.getUpcomingAppointments())
                .thenReturn(Collections.singletonList(sampleAppointment));
        mockMvc.perform(get("/api/appointments/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleAppointment.getId()));
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser
    void getUpcomingAppointments_shouldReturnEmpty() throws Exception {
        Mockito.when(appointmentService.getUpcomingAppointments())
                .thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/appointments/upcoming"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser
    void createAppointment_shouldSucceed() throws Exception {
        Mockito.when(appointmentService.createAppointment(any(Appointment.class)))
                .thenReturn(sampleAppointment);
        mockMvc.perform(post("/api/appointments")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleAppointment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleAppointment.getId()));
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser
    void createAppointment_shouldPreventDoubleBooking() throws Exception {
        Mockito.when(appointmentService.createAppointment(any(Appointment.class)))
                .thenThrow(new IllegalArgumentException("This vet is already booked for the selected date and time."));
        mockMvc.perform(post("/api/appointments")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleAppointment)))
                .andExpect(status().isConflict())
                .andExpect(content().string("This vet is already booked for the selected date and time."));
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser
    void createAppointment_shouldAllowDifferentVetSameTime() throws Exception {
        Appointment anotherAppointment = new Appointment();
        anotherAppointment.setId(2);
        anotherAppointment.setDate(sampleAppointment.getDate());
        anotherAppointment.setTime(sampleAppointment.getTime());
        anotherAppointment.setReason("Surgery");
        Vet anotherVet = new Vet();
        anotherVet.setId(2);
        anotherAppointment.setVet(anotherVet);
        anotherAppointment.setPet(pet);
        Mockito.when(appointmentService.createAppointment(any(Appointment.class)))
                .thenReturn(anotherAppointment);
        mockMvc.perform(post("/api/appointments")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(anotherAppointment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(anotherAppointment.getId()));
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser
    void createAppointment_shouldFailOnMissingFields() throws Exception {
        Appointment incomplete = new Appointment();
        incomplete.setVet(vet); // missing pet, date, and time
        Mockito.when(appointmentService.createAppointment(any(Appointment.class)))
                .thenThrow(new IllegalArgumentException("Missing required fields"));
        mockMvc.perform(post("/api/appointments")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incomplete)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Missing required fields"));
    }
}
