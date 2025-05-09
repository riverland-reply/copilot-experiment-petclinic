package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Vet;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Repository interface for managing Appointment entities.
 * Extends JpaRepository for basic CRUD operations and CustomAppointmentRepository for custom queries.
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, CustomAppointmentRepository {
    boolean existsByVetAndDateAndTime(Vet vet, LocalDate date, LocalTime time);
}