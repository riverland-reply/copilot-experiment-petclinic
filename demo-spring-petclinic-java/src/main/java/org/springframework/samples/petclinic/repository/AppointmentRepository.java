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
import org.springframework.samples.petclinic.model.Appointment;

/**
 * Repository class for <code>Appointment</code> domain objects
 * All method names are compliant with Spring Data naming conventions
 */
public interface AppointmentRepository {

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
     */
    Appointment findById(int id) throws DataAccessException;

    /**
     * Save an <code>Appointment</code> to the data store, either inserting or updating it.
     *
     * @param appointment the <code>Appointment</code> to save
     */
    void save(Appointment appointment) throws DataAccessException;

    /**
     * Delete an <code>Appointment</code> from the data store.
     *
     * @param appointment the <code>Appointment</code> to delete
     */
    void delete(Appointment appointment) throws DataAccessException;

    /**
     * Retrieve <code>Appointment</code>s from the data store for a specific pet.
     *
     * @param petId the id of the pet to search for
     * @return a <code>Collection</code> of <code>Appointment</code>s
     */
    Collection<Appointment> findByPetId(int petId) throws DataAccessException;

    /**
     * Retrieve <code>Appointment</code>s from the data store for a specific vet.
     *
     * @param vetId the id of the vet to search for
     * @return a <code>Collection</code> of <code>Appointment</code>s
     */
    Collection<Appointment> findByVetId(int vetId) throws DataAccessException;

    /**
     * Find appointments for a specific vet on a specific date.
     *
     * @param vetId the id of the vet
     * @param date the date to search for
     * @return a <code>List</code> of <code>Appointment</code>s
     */
    List<Appointment> findByVetIdAndDate(int vetId, LocalDate date) throws DataAccessException;

    /**
     * Check if a vet has any overlapping appointments at the specified date and time.
     * An appointment is considered to last 15 minutes.
     *
     * @param vetId the id of the vet
     * @param date the date to check
     * @param startTime the start time to check
     * @param endTime the end time to check
     * @param excludeId optional appointment id to exclude from the check (for updates)
     * @return true if there are overlapping appointments, false otherwise
     */
    boolean hasOverlappingAppointments(int vetId, LocalDate date, LocalTime startTime, LocalTime endTime, Integer excludeId) throws DataAccessException;
}