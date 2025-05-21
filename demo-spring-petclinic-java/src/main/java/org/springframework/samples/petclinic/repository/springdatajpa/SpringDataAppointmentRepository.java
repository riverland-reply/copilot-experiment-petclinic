package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Profile("spring-data-jpa")
public interface SpringDataAppointmentRepository extends AppointmentRepository, Repository<Appointment, Integer> {

    @Override
    @Query("SELECT a FROM Appointment a WHERE a.dateTime >= :now ORDER BY a.dateTime")
    Collection<Appointment> findUpcoming(@Param("now") LocalDateTime now);

    @Override
    @Query("SELECT a FROM Appointment a WHERE a.vet.id = :vetId AND a.dateTime = :dt")
    Appointment findByVetIdAndDateTime(@Param("vetId") int vetId, @Param("dt") LocalDateTime dateTime);
}
