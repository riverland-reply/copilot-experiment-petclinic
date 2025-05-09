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
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.dao.DataAccessException;

/**
 * Interface for custom repository methods for {@link org.springframework.samples.petclinic.model.Appointment}
 */
public interface AppointmentRepositoryOverride {

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