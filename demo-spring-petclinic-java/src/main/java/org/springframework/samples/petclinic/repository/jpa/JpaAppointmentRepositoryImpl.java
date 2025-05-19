package org.springframework.samples.petclinic.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public class JpaAppointmentRepositoryImpl implements AppointmentRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Appointment appointment) {
        if (appointment.getId() == null) {
            this.em.persist(appointment);
        } else {
            this.em.merge(appointment);
        }
    }
}
