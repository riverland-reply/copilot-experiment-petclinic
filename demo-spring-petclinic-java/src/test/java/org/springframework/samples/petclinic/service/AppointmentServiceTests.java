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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTests {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment appointment;
    private Vet vet;
    private Pet pet;

    @BeforeEach
    void setup() {
        vet = new Vet();
        vet.setId(1);
        vet.setFirstName("James");
        vet.setLastName("Carter");

        pet = new Pet();
        pet.setId(1);
        pet.setName("Leo");

        appointment = new Appointment();
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setTime(LocalTime.of(14, 30));
        appointment.setReason("Annual checkup");
        appointment.setVet(vet);
        appointment.setPet(pet);
    }

    @Test
    void shouldFindAllUpcomingAppointments() {
        // given
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        when(appointmentRepository.findAllUpcomingAppointments(any())).thenReturn(appointments);

        // when
        List<Appointment> foundAppointments = appointmentService.findAllUpcomingAppointments();

        // then
        assertThat(foundAppointments).hasSize(1);
        assertThat(foundAppointments.get(0)).isEqualTo(appointment);
    }

    @Test
    void shouldSaveAppointmentWhenNoOverlap() {
        // given
        when(appointmentRepository.findOverlappingAppointments(
            appointment.getVet(),
            appointment.getDate(),
            appointment.getTime()
        )).thenReturn(new ArrayList<>());

        // when
        Appointment savedAppointment = appointmentService.saveAppointment(appointment);

        // then
        assertThat(savedAppointment).isNotNull();
        assertThat(savedAppointment.getVet()).isEqualTo(vet);
        assertThat(savedAppointment.getPet()).isEqualTo(pet);
    }

    @Test
    void shouldThrowExceptionWhenOverlappingAppointmentExists() {
        // given
        List<Appointment> overlappingAppointments = new ArrayList<>();
        overlappingAppointments.add(new Appointment());
        when(appointmentRepository.findOverlappingAppointments(
            appointment.getVet(),
            appointment.getDate(),
            appointment.getTime()
        )).thenReturn(overlappingAppointments);

        // when/then
        assertThatThrownBy(() -> appointmentService.saveAppointment(appointment))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Vet is already booked");
    }

    @Test
    void shouldNotAllowAppointmentInThePast() {
        // given
        appointment.setDate(LocalDate.now().minusDays(1));

        // when/then
        assertThatThrownBy(() -> appointmentService.saveAppointment(appointment))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Cannot schedule appointment in the past");
    }
} 