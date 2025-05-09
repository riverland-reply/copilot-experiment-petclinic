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
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.AppointmentRepository;

/**
 * Spring Data JPA specialization of the {@link AppointmentRepository} interface
 */
public interface SpringDataAppointmentRepository extends AppointmentRepository, Repository<Appointment, Integer> {

    @Override
    @Query("SELECT appointment FROM Appointment appointment WHERE appointment.pet.id = :id")
    List<Appointment> findByPetId(@Param("id") Integer petId);

    @Override
    @Query("SELECT appointment FROM Appointment appointment WHERE appointment.vet.id = :id")
    List<Appointment> findByVetId(@Param("id") Integer vetId);

    @Override
    default List<Appointment> findAllUpcoming() {
        return findAllUpcoming(LocalDate.now());
    }
    
    @Override
    @Query("SELECT a FROM Appointment a WHERE a.vet.id = :vetId " +
           "AND a.date = :date " +
           "AND ((a.time >= :startTime AND a.time < :endTime) OR " +
           "(:startTime >= a.time AND :startTime < FUNCTION('ADDTIME', a.time, '00:15:00'))) " +
           "AND (:excludeId IS NULL OR a.id <> :excludeId)")
    List<Appointment> findOverlappingAppointments(
        @Param("vetId") Integer vetId, 
        @Param("date") LocalDate date, 
        @Param("startTime") LocalTime startTime, 
        @Param("endTime") LocalTime endTime, 
        @Param("excludeId") Integer excludeId);
}