package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.dao.DataAccessException;

@Profile("spring-data-jpa")
public interface AppointmentRepositoryOverride {
    void delete(Appointment appointment) throws DataAccessException;
}
