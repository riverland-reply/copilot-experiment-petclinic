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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.springframework.dao.DataAccessException;

/**
 * Implementation of custom repository methods for {@link org.springframework.samples.petclinic.model.Appointment}
 */
public class SpringDataAppointmentRepositoryImpl implements AppointmentRepositoryOverride {

    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean hasOverlappingAppointments(int vetId, LocalDate date, LocalTime startTime, LocalTime endTime, Integer excludeId) throws DataAccessException {
        StringBuilder queryBuilder = new StringBuilder(
            "SELECT COUNT(a) FROM Appointment a " +
            "WHERE a.vet.id = :vetId " +
            "AND a.date = :date " +
            "AND ((a.time < :endTime AND a.time.plusMinutes(15) > :startTime))"
        );
        
        if (excludeId != null) {
            queryBuilder.append(" AND a.id != :excludeId");
        }
        
        TypedQuery<Long> query = this.em.createQuery(queryBuilder.toString(), Long.class);
        query.setParameter("vetId", vetId);
        query.setParameter("date", date);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        
        if (excludeId != null) {
            query.setParameter("excludeId", excludeId);
        }
        
        return query.getSingleResult() > 0;
    }
}