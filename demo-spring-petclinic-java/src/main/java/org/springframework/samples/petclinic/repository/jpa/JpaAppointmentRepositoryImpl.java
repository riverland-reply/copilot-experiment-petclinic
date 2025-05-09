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
package org.springframework.samples.petclinic.repository.jpa;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA implementation of the {@link AppointmentRepository} interface.
 */
@Repository
public class JpaAppointmentRepositoryImpl implements AppointmentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Appointment> findAll() throws DataAccessException {
        return this.em.createQuery("SELECT a FROM Appointment a", Appointment.class).getResultList();
    }

    @Override
    public Appointment findById(int id) throws DataAccessException {
        return this.em.find(Appointment.class, id);
    }

    @Override
    public void save(Appointment appointment) throws DataAccessException {
        if (appointment.getId() == null) {
            this.em.persist(appointment);
        } else {
            this.em.merge(appointment);
        }
    }

    @Override
    public void delete(Appointment appointment) throws DataAccessException {
        String appointmentId = appointment.getId().toString();
        this.em.createQuery("DELETE FROM Appointment appointment WHERE id=" + appointmentId).executeUpdate();
        if (em.contains(appointment)) {
            em.remove(appointment);
        }
    }

    @Override
    public Collection<Appointment> findByPetId(int petId) throws DataAccessException {
        Query query = this.em.createQuery("SELECT appointment FROM Appointment appointment WHERE appointment.pet.id = :id");
        query.setParameter("id", petId);
        return query.getResultList();
    }

    @Override
    public Collection<Appointment> findByVetId(int vetId) throws DataAccessException {
        Query query = this.em.createQuery("SELECT appointment FROM Appointment appointment WHERE appointment.vet.id = :id");
        query.setParameter("id", vetId);
        return query.getResultList();
    }

    @Override
    public List<Appointment> findByVetIdAndDate(int vetId, LocalDate date) throws DataAccessException {
        TypedQuery<Appointment> query = this.em.createQuery(
            "SELECT a FROM Appointment a WHERE a.vet.id = :vetId AND a.date = :date ORDER BY a.time",
            Appointment.class);
        query.setParameter("vetId", vetId);
        query.setParameter("date", date);
        return query.getResultList();
    }

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