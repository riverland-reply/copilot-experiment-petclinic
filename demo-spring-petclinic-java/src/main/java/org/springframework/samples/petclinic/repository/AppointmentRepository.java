package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @EntityGraph(attributePaths = {"pet", "vet"})
    @Query("SELECT a FROM Appointment a WHERE (a.date > :today) OR (a.date = :today AND a.time >= :now)")
    List<Appointment> findUpcomingAppointments(@Param("today") LocalDate today, @Param("now") LocalTime now);

    boolean existsByVetIdAndDateAndTime(Integer vetId, LocalDate date, LocalTime time);
}
