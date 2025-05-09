package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Appointment> findAllUpcomingAppointments() {
        return appointmentRepository.findAllUpcomingAppointments(LocalDate.now());
    }

    @Transactional
    public Appointment saveAppointment(Appointment appointment) throws DataAccessException {
        // Validate appointment date is not in the past
        if (appointment.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot schedule appointment in the past");
        }

        // Check for overlapping appointments
        List<Appointment> overlappingAppointments = appointmentRepository.findOverlappingAppointments(
            appointment.getVet(),
            appointment.getDate(),
            appointment.getTime()
        );

        if (!overlappingAppointments.isEmpty()) {
            throw new IllegalStateException("Vet is already booked for this date and time");
        }

        appointmentRepository.save(appointment);
        return appointment;
    }
} 