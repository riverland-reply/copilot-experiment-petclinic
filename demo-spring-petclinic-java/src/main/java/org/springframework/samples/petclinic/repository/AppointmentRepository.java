package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends Repository<Appointment, Integer> {

    @Query("SELECT a FROM Appointment a WHERE a.date >= :today ORDER BY a.date, a.time")
    @Transactional(readOnly = true)
    List<Appointment> findAllUpcomingAppointments(@Param("today") LocalDate today);

    @Query("SELECT a FROM Appointment a WHERE a.vet = :vet AND a.date = :date AND a.time = :time")
    @Transactional(readOnly = true)
    List<Appointment> findOverlappingAppointments(
        @Param("vet") Vet vet,
        @Param("date") LocalDate date,
        @Param("time") LocalTime time
    );

    @Transactional
    void save(Appointment appointment);
} 