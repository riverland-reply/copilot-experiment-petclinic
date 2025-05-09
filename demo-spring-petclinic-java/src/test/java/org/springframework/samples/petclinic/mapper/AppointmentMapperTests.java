
package org.springframework.samples.petclinic.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;
import org.springframework.samples.petclinic.service.ClinicService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentMapperTests {

    @Mock
    private ClinicService clinicService;

    @InjectMocks
    private AppointmentMapper appointmentMapper;

    @Test
    void toAppointmentDtoWithNullAppointment() {
        assertNull(appointmentMapper.toAppointmentDto(null));
    }

    @Test
    void toAppointmentDtoWithValidAppointment() {
        // Arrange
        Appointment appointment = new Appointment();
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Max");

        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("John");
        vet.setLastName("Doe");

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);

        appointment.setId(1);
        appointment.setPet(pet);
        appointment.setVet(vet);
        appointment.setAppointmentDateTimeStart(start);
        appointment.setAppointmentDateTimeEnd(end);
        appointment.setReason("Check up");

        // Act
        AppointmentDto dto = appointmentMapper.toAppointmentDto(appointment);

        // Assert
        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals(1, dto.getPetId());
        assertEquals(1, dto.getVetId());
        assertEquals("Max", dto.getPetName());
        assertEquals("John", dto.getVetFirstName());
        assertEquals("Doe", dto.getVetLastName());
        assertEquals("Check up", dto.getReason());
        assertEquals(start, dto.getAppointmentDateTimeStart());
        assertEquals(end, dto.getAppointmentDateTimeEnd());
    }

    @Test
    void toAppointmentWithNullDto() {
        assertNull(appointmentMapper.toAppointment(null));
    }

    @Test
    void toAppointmentWithValidDto() {
        // Arrange
        AppointmentDto dto = new AppointmentDto();
        Pet pet = new Pet();
        pet.setId(1);

        Vet vet = new Vet();
        vet.setId(1);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);

        dto.setId(1);
        dto.setPetId(1);
        dto.setVetId(1);
        dto.setAppointmentDateTimeStart(start);
        dto.setAppointmentDateTimeEnd(end);
        dto.setReason("Check up");

        when(clinicService.findPetById(1)).thenReturn(pet);
        when(clinicService.findVetById(1)).thenReturn(vet);

        // Act
        Appointment appointment = appointmentMapper.toAppointment(dto);

        // Assert
        assertNotNull(appointment);
        assertEquals(1, appointment.getId());
        assertEquals(pet, appointment.getPet());
        assertEquals(vet, appointment.getVet());
        assertEquals("Check up", appointment.getReason());
        assertEquals(start, appointment.getAppointmentDateTimeStart());
        assertEquals(end, appointment.getAppointmentDateTimeEnd());

        verify(clinicService).findPetById(1);
        verify(clinicService).findVetById(1);
    }
}
