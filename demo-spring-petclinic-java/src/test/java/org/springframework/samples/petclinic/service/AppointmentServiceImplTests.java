
package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;
import org.springframework.samples.petclinic.rest.exceptions.AppointmentConflictException;
import org.springframework.samples.petclinic.rest.exceptions.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTests {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ClinicService clinicService;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Appointment appointment;
    private AppointmentDto appointmentDto;
    private Pet pet;
    private Vet vet;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        pet = new Pet();
        pet.setId(1);

        vet = new Vet();
        vet.setId(1);

        startTime = LocalDateTime.now();
        endTime = startTime.plusHours(1);

        appointment = new Appointment();
        appointment.setId(1);
        appointment.setPet(pet);
        appointment.setVet(vet);
        appointment.setAppointmentDateTimeStart(startTime);
        appointment.setAppointmentDateTimeEnd(endTime);

        appointmentDto = new AppointmentDto();
        appointmentDto.setId(1);
        appointmentDto.setPetId(1);
        appointmentDto.setVetId(1);
        appointmentDto.setAppointmentDateTimeStart(startTime);
        appointmentDto.setAppointmentDateTimeEnd(endTime);
    }

    @Test
    void findAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);

        when(appointmentRepository.findAll()).thenReturn(appointments);

        var result = appointmentService.findAllAppointments();

        assertEquals(1, result.size());
        verify(appointmentRepository).findAll();
    }

    @Test
    void saveAppointmentSuccessfully() {
        when(appointmentRepository.findAll()).thenReturn(new ArrayList<>());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        assertDoesNotThrow(() ->
                appointmentService.saveAppointment(appointment, appointmentDto)
        );

        verify(appointmentRepository).save(appointment);
    }

    @Test
    void saveAppointmentWithNullPet() {
        appointment.setPet(null);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> appointmentService.saveAppointment(appointment, appointmentDto)
        );

        assertTrue(exception.getMessage().contains("Pet not found"));
    }

    @Test
    void saveAppointmentWithNullVet() {
        appointment.setVet(null);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> appointmentService.saveAppointment(appointment, appointmentDto)
        );

        assertTrue(exception.getMessage().contains("Vet not found"));
    }

    @Test
    void saveAppointmentWithConflict() {
        List<Appointment> existingAppointments = new ArrayList<>();

        Appointment existingAppointment = new Appointment();
        existingAppointment.setId(2);
        existingAppointment.setVet(vet);
        existingAppointment.setAppointmentDateTimeStart(startTime);
        existingAppointment.setAppointmentDateTimeEnd(endTime);
        existingAppointments.add(existingAppointment);

        when(appointmentRepository.findAll()).thenReturn(existingAppointments);

        AppointmentConflictException exception = assertThrows(
                AppointmentConflictException.class,
                () -> appointmentService.saveAppointment(appointment, appointmentDto)
        );

        assertTrue(exception.getMessage().contains("Vet is already booked"));
    }

    @Test
    void saveAppointmentWithNoConflictDifferentVet() {
        List<Appointment> existingAppointments = new ArrayList<>();

        Vet differentVet = new Vet();
        differentVet.setId(2);

        Appointment existingAppointment = new Appointment();
        existingAppointment.setId(2);
        existingAppointment.setVet(differentVet);
        existingAppointment.setAppointmentDateTimeStart(startTime);
        existingAppointment.setAppointmentDateTimeEnd(endTime);
        existingAppointments.add(existingAppointment);

        when(appointmentRepository.findAll()).thenReturn(existingAppointments);

        assertDoesNotThrow(() ->
                appointmentService.saveAppointment(appointment, appointmentDto)
        );

        verify(appointmentRepository).save(appointment);
    }
}
