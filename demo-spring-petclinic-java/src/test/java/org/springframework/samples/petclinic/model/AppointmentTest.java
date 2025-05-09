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
package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link Appointment}
 */
public class AppointmentTest {

    private Appointment appointment;
    private Pet pet;
    private Vet vet;

    @BeforeEach
    void setup() {
        // Create a pet
        pet = new Pet();
        pet.setId(1);
        pet.setName("Max");
        
        // Create a vet
        vet = new Vet();
        vet.setId(1);
        vet.setFirstName("James");
        vet.setLastName("Carter");
        
        // Create an appointment
        appointment = new Appointment();
        appointment.setId(1);
        appointment.setDate(LocalDate.of(2023, 6, 15));
        appointment.setTime(LocalTime.of(14, 30));
        appointment.setReason("Annual checkup");
        appointment.setPet(pet);
        appointment.setVet(vet);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, appointment.getId());
        assertEquals(LocalDate.of(2023, 6, 15), appointment.getDate());
        assertEquals(LocalTime.of(14, 30), appointment.getTime());
        assertEquals("Annual checkup", appointment.getReason());
        assertEquals(pet, appointment.getPet());
        assertEquals(vet, appointment.getVet());
    }

    @Test
    void testDefaultConstructor() {
        Appointment newAppointment = new Appointment();
        assertNotNull(newAppointment.getDate());
        assertNotNull(newAppointment.getTime());
    }
}