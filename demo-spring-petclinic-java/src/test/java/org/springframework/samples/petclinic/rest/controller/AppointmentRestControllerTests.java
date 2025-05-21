package org.springframework.samples.petclinic.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.mapper.AppointmentMapper;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.advice.ExceptionControllerAdvice;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.clinicService.ApplicationTestConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
class AppointmentRestControllerTests {

    @Autowired
    private AppointmentRestController appointmentRestController;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @MockitoBean
    private ClinicService clinicService;

    private MockMvc mockMvc;
    private List<Appointment> appointments;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(appointmentRestController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();
        appointments = new ArrayList<>();
        Appointment appt = new Appointment();
        appt.setId(1);
        appt.setDateTime(LocalDateTime.now().plusDays(1));
        appt.setReason("checkup");
        appt.setPet(new Pet());
        appt.setVet(new Vet());
        appointments.add(appt);
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testListAppointmentsSuccess() throws Exception {
        given(this.clinicService.findUpcomingAppointments()).willReturn(appointments);
        this.mockMvc.perform(get("/api/appointments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(1));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testCreateAppointmentSuccess() throws Exception {
        Appointment appt = appointments.get(0);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        AppointmentDto dto = appointmentMapper.toAppointmentDto(appt);
        String json = mapper.writeValueAsString(dto);
        this.mockMvc.perform(post("/api/appointments")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
