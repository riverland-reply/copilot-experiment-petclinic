package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;

import java.time.LocalDateTime;
import java.util.Collection;

public interface AppointmentRepository {

    void save(Appointment appointment) throws DataAccessException;

    Appointment findById(int id) throws DataAccessException;

    Collection<Appointment> findByVetId(int vetId) throws DataAccessException;

    Collection<Appointment> findAll() throws DataAccessException;

    void delete(Appointment appointment) throws DataAccessException;

    Collection<Appointment> findByVetIdAndDateTime(int vetId, LocalDateTime dateTime) throws DataAccessException;
}
