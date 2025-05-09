package org.springframework.samples.petclinic.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.assertj.core.api.Assertions.assertThat;

public class AppointmentJsonTest {
    private ObjectMapper mapper;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    @Test
    void appointmentSerializationDoesNotIncludePet() throws Exception {
        PetType type = new PetType();
        type.setId(1);
        type.setName("dog");
        Pet pet = new Pet();
        pet.setId(2);
        pet.setName("Buddy");
        pet.setType(type);
        Appointment appointment = new Appointment();
        appointment.setId(3);
        appointment.setDate(LocalDate.of(2025, 5, 9));
        appointment.setTime(LocalTime.of(14, 0));
        appointment.setReason("Checkup");
        appointment.setPet(pet);
        String json = mapper.writeValueAsString(appointment);
        // The serialized Appointment JSON should include the pet object (with id and name), but not owner or visits
        assertThat(json).contains("pet");
        assertThat(json).contains("Buddy");
        assertThat(json).doesNotContain("owner");
        assertThat(json).doesNotContain("visits");
    }

    @Test
    void petSerializationIncludesTypeButNoRecursion() throws Exception {
        PetType type = new PetType();
        type.setId(1);
        type.setName("cat");
        Pet pet = new Pet();
        pet.setId(2);
        pet.setName("Milo");
        pet.setType(type);
        String json = mapper.writeValueAsString(pet);
        assertThat(json).contains("cat"); // PetType should be serialized
        assertThat(json).doesNotContain("appointments"); // No recursion
    }
}
