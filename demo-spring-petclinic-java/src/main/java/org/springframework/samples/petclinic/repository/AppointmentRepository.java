package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
}