/*
 * Copyright 2002-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.advice.ExceptionControllerAdvice;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
class AppointmentRestControllerTests {

    @Autowired
    private AppointmentRestController appointmentRestController;

    @MockitoBean
    private ClinicService clinicService;

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
        pet.setName("Fluffy");

        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("James");
        vet.setLastName("Carter");

        // Past appointment
        Appointment pastAppointment = new Appointment();
        pastAppointment.setId(1);
        pastAppointment.setDate(LocalDate.now().minusDays(7));
        pastAppointment.setTime(LocalTime.of(10, 0));
        pastAppointment.setReason("Annual checkup");
        pastAppointment.setPet(pet);
        pastAppointment.setVet(vet);
        appointments.add(pastAppointment);

        // Today's appointment
        Appointment todayAppointment = new Appointment();
        todayAppointment.setId(2);
        todayAppointment.setDate(LocalDate.now());
        todayAppointment.setTime(LocalTime.of(14, 30));
        todayAppointment.setReason("Vaccination");
        todayAppointment.setPet(pet);
        todayAppointment.setVet(vet);
        appointments.add(todayAppointment);

        // Future appointment
        Appointment futureAppointment = new Appointment();
        futureAppointment.setId(3);
        futureAppointment.setDate(LocalDate.now().plusDays(7));
        futureAppointment.setTime(LocalTime.of(16, 0));
        futureAppointment.setReason("Follow-up");
        futureAppointment.setPet(pet);
        futureAppointment.setVet(vet);
        appointments.add(futureAppointment);
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetAllAppointments() throws Exception {
        given(this.clinicService.findAllAppointments()).willReturn(appointments);

        mockMvc.perform(get("/api/appointments")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(1))
            .andExpect(jsonPath("$.[1].id").value(2))
            .andExpect(jsonPath("$.[2].id").value(3));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetFutureAppointments() throws Exception {
        given(this.clinicService.findAllAppointments()).willReturn(appointments);

        mockMvc.perform(get("/api/appointments/future")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(2)) // Today's appointment
            .andExpect(jsonPath("$.[1].id").value(3)); // Future appointment
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testCreateAppointment() throws Exception {
        Appointment newAppointment = appointments.get(2);
        
        when(this.clinicService.findPetById(1)).thenReturn(newAppointment.getPet());
        when(this.clinicService.findVetById(1)).thenReturn(newAppointment.getVet());
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules(); // For LocalDate and LocalTime serialization
        
        mockMvc.perform(post("/api/appointments")
            .content(mapper.writeValueAsString(newAppointment))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }
}