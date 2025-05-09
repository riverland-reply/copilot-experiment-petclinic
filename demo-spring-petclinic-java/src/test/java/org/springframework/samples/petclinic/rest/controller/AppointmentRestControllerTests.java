
package org.springframework.samples.petclinic.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.mapper.AppointmentMapper;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.advice.ExceptionControllerAdvice;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.clinicService.ApplicationTestConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
class AppointmentRestControllerTests {

    @Autowired
    private AppointmentRestController appointmentRestController;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @MockBean
    private ClinicService clinicService;

    @MockBean
    private AppointmentService appointmentService;

    private MockMvc mockMvc;

    private List<Appointment> appointments;

    @BeforeEach
    void initAppointments() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(appointmentRestController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();

        appointments = new ArrayList<>();

        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("TestPet");

        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("James");
        vet.setLastName("Carter");

        Appointment appointment = new Appointment();
        appointment.setId(1);
        appointment.setPet(pet);
        appointment.setVet(vet);
        appointment.setAppointmentDateTimeStart(LocalDateTime.now());
        appointment.setAppointmentDateTimeEnd(LocalDateTime.now().plusHours(1));
        appointment.setReason("Annual checkup");
        appointments.add(appointment);

        when(this.clinicService.findPetById(1)).thenReturn(pet);
        when(this.clinicService.findVetById(1)).thenReturn(vet);
        when(this.appointmentService.findAllAppointments()).thenReturn(appointments);
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetAppointment() throws Exception {
        List<Appointment> allAppointments = appointments;

        this.mockMvc.perform(get("/api/appointments")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].petName").value("TestPet"))
                .andExpect(jsonPath("$[0].vetFirstName").value("James"))
                .andExpect(jsonPath("$[0].vetLastName").value("Carter"));

    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetAppointmentNotFound() throws Exception {
        when(this.appointmentService.findAllAppointments()).thenReturn(new ArrayList<>());

        this.mockMvc.perform(get("/api/appointments")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testCreateAppointmentSuccess() throws Exception {
        Appointment appointment = appointments.get(0);
        AppointmentDto appointmentDto = appointmentMapper.toAppointmentDto(appointment);
        appointmentDto.setId(null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        String appointmentJson = mapper.writeValueAsString(appointmentDto);

        this.mockMvc.perform(post("/api/appointments")
                        .content(appointmentJson)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }
}
