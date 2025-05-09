package org.springframework.samples.petclinic.repository;

import org.springframework.samples.petclinic.model.Vet;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Custom repository interface for Appointment entity.
 * Contains custom query methods for appointment management.
 */
public interface CustomAppointmentRepository {
    boolean existsByVetAndDateAndTime(Vet vet, LocalDate date, LocalTime time);
}
