package org.springframework.samples.petclinic.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;

/**
 * Repository class for <code>Appointment</code> domain objects.
 * All method names are compliant with Spring Data naming conventions
 * so this interface can easily be extended for Spring Data.
 */
public interface AppointmentRepository {

    /**
     * Save an <code>Appointment</code> to the data store, either inserting or updating it.
     *
     * @param appointment the <code>Appointment</code> to save
     */
    void save(Appointment appointment) throws DataAccessException;

    /**
     * Retrieve all <code>Appointment</code>s from the data store.
     *
     * @return a <code>Collection</code> of <code>Appointment</code>s
     */
    Collection<Appointment> findAll() throws DataAccessException;

    /**
     * Retrieve an <code>Appointment</code> from the data store by id.
     *
     * @param id the id to search for
     * @return the <code>Appointment</code> if found
     * @throws DataAccessException if the appointment is not found
     */
    Appointment findById(int id) throws DataAccessException;

    /**
     * Delete an <code>Appointment</code> from the data store.
     *
     * @param appointment the <code>Appointment</code> to delete
     */
    void delete(Appointment appointment) throws DataAccessException;

    /**
     * Find appointments for a specific pet.
     *
     * @param petId the pet id to search for
     * @return a <code>List</code> of <code>Appointment</code>s
     */
    List<Appointment> findByPetId(Integer petId) throws DataAccessException;

    /**
     * Find appointments for a specific vet.
     *
     * @param vetId the vet id to search for
     * @return a <code>List</code> of <code>Appointment</code>s
     */
    List<Appointment> findByVetId(Integer vetId) throws DataAccessException;

    /**
     * Find appointments for a specific date.
     *
     * @param date the date to search for
     * @return a <code>List</code> of <code>Appointment</code>s
     */
    List<Appointment> findByDate(LocalDate date) throws DataAccessException;

    /**
     * Find appointments for a specific vet and date.
     *
     * @param vetId the vet id to search for
     * @param date the date to search for
     * @return a <code>List</code> of <code>Appointment</code>s
     */
    List<Appointment> findByVetIdAndDate(Integer vetId, LocalDate date) throws DataAccessException;

    /**
     * Find all upcoming appointments from today onwards.
     *
     * @param today the current date
     * @return a <code>List</code> of <code>Appointment</code>s
     */
    List<Appointment> findAllUpcomingAppointments(LocalDate today) throws DataAccessException;

    /**
     * Find overlapping appointments for a vet at a specific date and time.
     *
     * @param vetId the vet id to search for
     * @param date the date to search for
     * @param time the time to search for
     * @return a <code>List</code> of <code>Appointment</code>s
     */
    List<Appointment> findOverlappingAppointments(Integer vetId, LocalDate date, LocalTime time) throws DataAccessException;
} 