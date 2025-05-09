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
import java.util.Collection;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.AppointmentRepository;

/**
 * Spring Data JPA specialization of the {@link AppointmentRepository} interface
 */
@Profile("spring-data-jpa")
public interface SpringDataAppointmentRepository extends AppointmentRepository, Repository<Appointment, Integer>, AppointmentRepositoryOverride {

    @Override
    @Query("SELECT appointment FROM Appointment appointment WHERE appointment.pet.id = :id")
    Collection<Appointment> findByPetId(@Param("id") int id);

    @Override
    @Query("SELECT appointment FROM Appointment appointment WHERE appointment.vet.id = :id")
    Collection<Appointment> findByVetId(@Param("id") int id);

    @Override
    @Query("SELECT a FROM Appointment a WHERE a.vet.id = :vetId AND a.date = :date ORDER BY a.time")
    List<Appointment> findByVetIdAndDate(@Param("vetId") int vetId, @Param("date") LocalDate date);
}