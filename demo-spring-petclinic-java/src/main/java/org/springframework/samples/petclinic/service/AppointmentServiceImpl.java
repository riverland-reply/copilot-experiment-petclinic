package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;
import org.springframework.samples.petclinic.rest.exceptions.AppointmentConflictException;
import org.springframework.samples.petclinic.rest.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ClinicService clinicService;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, ClinicService clinicService) {
        this.appointmentRepository = appointmentRepository;
        this.clinicService = clinicService;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Appointment> findAllAppointments() throws DataAccessException {
        return appointmentRepository.findAll();
    }

    @Override
    @Transactional
    public void saveAppointment(Appointment appointment, AppointmentDto appointmentDto) throws DataAccessException {
        StringBuilder errorMessage = new StringBuilder();

        if (appointment.getPet() == null) {
            errorMessage.append("Pet not found with id: ").append(appointmentDto.getPetId());
        }

        if (appointment.getVet() == null) {
            if (errorMessage.length() > 0) {
                errorMessage.append(" and ");
            }
            errorMessage.append("Vet not found with id: ").append(appointmentDto.getVetId());
        }

        if (errorMessage.length() > 0) {
            throw new ResourceNotFoundException(errorMessage.toString());
        }

        // Check for conflicting appointments
        LocalDateTime appointmentStart = appointment.getAppointmentDateTimeStart();
        LocalDateTime appointmentEnd = appointment.getAppointmentDateTimeEnd(); // Assuming 1-hour appointments

        List<Appointment> existingAppointments = appointmentRepository.findAll();

        boolean hasConflict = existingAppointments.stream()
                .filter(existing -> existing.getVet().getId().equals(appointment.getVet().getId()))
                .filter(existing -> !existing.getId().equals(appointment.getId())) // Exclude current appointment when updating
                .anyMatch(existing -> {
                    LocalDateTime existingStart = existing.getAppointmentDateTimeStart();
                    LocalDateTime existingEnd = existingStart.plusHours(1);
                    return !appointmentStart.isAfter(existingEnd) && !appointmentEnd.isBefore(existingStart);
                });

        //AppointmentConflictException
        if (hasConflict) {
            throw new AppointmentConflictException("Vet is already booked between " +
                    appointmentStart.toString() + " and " + appointmentEnd.toString());        }

        appointmentRepository.save(appointment);
    }

}
