package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.samples.petclinic.model.Appointment;

public interface AppointmentService {

	Collection<Appointment> findAllAppointments();

	Optional<Appointment> findAppointmentById(int id);

	void saveAppointment(Appointment appointment);

	void deleteAppointment(Appointment appointment);

}