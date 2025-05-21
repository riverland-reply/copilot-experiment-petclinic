package org.springframework.samples.petclinic.repository.springdatajpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;

@Profile("spring-data-jpa")
public class SpringDataAppointmentRepositoryImpl implements AppointmentRepositoryOverride {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void delete(Appointment appointment) throws DataAccessException {
        String id = appointment.getId().toString();
        em.createQuery("DELETE FROM Appointment a WHERE id=" + id).executeUpdate();
        if (em.contains(appointment)) {
            em.remove(appointment);
        }
    }
}
