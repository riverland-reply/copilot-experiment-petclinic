package org.springframework.samples.petclinic.service;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;

import java.util.Collection;

public interface AppointmentService {
    Collection<Appointment> findAllAppointments() throws DataAccessException;
    void saveAppointment(Appointment appointment, AppointmentDto appointmentDto) throws DataAccessException;
}