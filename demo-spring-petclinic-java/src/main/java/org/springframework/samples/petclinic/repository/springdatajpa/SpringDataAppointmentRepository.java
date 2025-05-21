package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.AppointmentRepository;

@Profile("spring-data-jpa")
public interface SpringDataAppointmentRepository extends AppointmentRepository, Repository<Appointment, Integer>, AppointmentRepositoryOverride {
}
