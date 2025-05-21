package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Repository class for <code>Appointment</code> domain objects.
 */
public interface AppointmentRepository {

    void save(Appointment appointment) throws DataAccessException;

    Appointment findById(int id) throws DataAccessException;

    Collection<Appointment> findUpcoming(LocalDateTime dateTime) throws DataAccessException;

    Appointment findByVetIdAndDateTime(int vetId, LocalDateTime dateTime) throws DataAccessException;

    void delete(Appointment appointment) throws DataAccessException;
}
