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
            em.persist(appointment);
        } else {
            em.merge(appointment);

        }
    }

    @Override
    public Appointment findById(int id) throws DataAccessException {

        return em.find(Appointment.class, id);
    }

    @Override
    public Collection<Appointment> findByVetId(int vetId) throws DataAccessException {
        Query query = em.createQuery("SELECT a FROM Appointment a WHERE a.vet.id = :id");
        query.setParameter("id", vetId);

        return query.getResultList();
    }

    @Override

    public Collection<Appointment> findAll() throws DataAccessException {
        return em.createQuery("SELECT a FROM Appointment a", Appointment.class).getResultList();

    }

    @Override
    public void delete(Appointment appointment) throws DataAccessException {

        em.remove(em.contains(appointment) ? appointment : em.merge(appointment));
    }

    @Override
    public Collection<Appointment> findByVetIdAndDateTime(int vetId, LocalDateTime dateTime) throws DataAccessException {
        Query query = em.createQuery("SELECT a FROM Appointment a WHERE a.vet.id = :id AND a.dateTime = :dt");
        query.setParameter("id", vetId);
        query.setParameter("dt", dateTime);
        return query.getResultList();

    }
}
