/*
 * Copyright 2002-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Appointment;

/**
 * Repository class for <code>Appointment</code> domain objects
 */
public interface AppointmentRepository {

    /**
     * Save an <code>Appointment</code> to the data store, either inserting or updating it.
     *
     * @param appointment the <code>Appointment</code> to save
     * @see BaseEntity#isNew
     */
    void save(Appointment appointment) throws DataAccessException;

    /**
     * Retrieve an <code>Appointment</code> from the data store by id.
     *
     * @param id the id to search for
     * @return the <code>Appointment</code> if found
     * @throws org.springframework.dao.DataRetrievalFailureException if not found
     */
    Appointment findById(int id) throws DataAccessException;

    /**
     * Retrieve all <code>Appointment</code>s from the data store.
     *
     * @return a <code>Collection</code> of <code>Appointment</code>s
     */
    Collection<Appointment> findAll() throws DataAccessException;

    /**
     * Delete an <code>Appointment</code> from the data store by id.
     *
     * @param appointment the <code>Appointment</code> to delete
     */
    void delete(Appointment appointment) throws DataAccessException;

    /**
     * Retrieve <code>Appointment</code>s from the data store by pet id.
     *
     * @param petId the id to search for
     * @return a <code>Collection</code> of <code>Appointment</code>s
     */
    List<Appointment> findByPetId(Integer petId);

    /**
     * Retrieve <code>Appointment</code>s from the data store by vet id.
     *
     * @param vetId the id to search for
     * @return a <code>Collection</code> of <code>Appointment</code>s
     */
    List<Appointment> findByVetId(Integer vetId);

    @Query("SELECT a FROM Appointment a WHERE a.date >= :today ORDER BY a.date, a.time")
    List<Appointment> findAllUpcoming(@Param("today") LocalDate today);

    /**
     * Retrieve all upcoming <code>Appointment</code>s from the data store.
     * Upcoming appointments are those with a date on or after the current date.
     *
     * @return a <code>Collection</code> of upcoming <code>Appointment</code>s
     */
    List<Appointment> findAllUpcoming() throws DataAccessException;
    
    /**
     * Find overlapping appointments for a vet at a specific date and time range.
     * This is used to prevent double-booking a vet.
     *
     * @param vetId the id of the vet
     * @param date the date of the appointment
     * @param startTime the start time of the appointment
     * @param endTime the end time of the appointment
     * @param excludeId optional appointment id to exclude from the search (for updates)
     * @return a <code>List</code> of overlapping <code>Appointment</code>s
     */
    List<Appointment> findOverlappingAppointments(Integer vetId, LocalDate date, 
                                                LocalTime startTime, LocalTime endTime, 
                                                Integer excludeId) throws DataAccessException;
}