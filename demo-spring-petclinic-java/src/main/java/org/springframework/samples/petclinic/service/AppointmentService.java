package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> getUpcomingAppointments() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        return appointmentRepository.findUpcomingAppointments(today, now);
    }

    public Appointment createAppointment(Appointment appointment) {
        boolean exists = appointmentRepository.existsByVetIdAndDateAndTime(
                appointment.getVet().getId(),
                appointment.getDate(),
                appointment.getTime()
        );
        if (exists) {
            throw new IllegalArgumentException("This vet is already booked for the selected date and time.");
        }
        return appointmentRepository.save(appointment);
    }
}
