package org.springframework.samples.petclinic.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
@Profile("jpa")
public class JpaAppointmentRepositoryImpl implements AppointmentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Appointment appointment) throws DataAccessException {
        if (appointment.getId() == null) {
            this.em.persist(appointment);
        } else {
            this.em.merge(appointment);
        }
    }

    @Override
    public Appointment findById(int id) throws DataAccessException {
        return this.em.find(Appointment.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Appointment> findUpcoming(LocalDateTime dateTime) throws DataAccessException {
        Query query = this.em.createQuery("SELECT a FROM Appointment a WHERE a.dateTime >= :now ORDER BY a.dateTime");
        query.setParameter("now", dateTime);
        return query.getResultList();
    }

    @Override
    public Appointment findByVetIdAndDateTime(int vetId, LocalDateTime dateTime) throws DataAccessException {
        Query query = this.em.createQuery("SELECT a FROM Appointment a WHERE a.vet.id = :vetId AND a.dateTime = :dt");
        query.setParameter("vetId", vetId);
        query.setParameter("dt", dateTime);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public void delete(Appointment appointment) throws DataAccessException {
        this.em.remove(this.em.contains(appointment) ? appointment : this.em.merge(appointment));
    }
}
