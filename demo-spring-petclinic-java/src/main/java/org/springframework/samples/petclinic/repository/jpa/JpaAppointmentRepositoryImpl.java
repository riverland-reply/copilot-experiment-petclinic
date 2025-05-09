package org.springframework.samples.petclinic.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.CustomAppointmentRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public class JpaAppointmentRepositoryImpl implements CustomAppointmentRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean existsByVetAndDateAndTime(Vet vet, LocalDate date, LocalTime time) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE vet_id = :vet AND date = :date AND time = :time";
        Long count = (Long) em.createNativeQuery(sql)
                .setParameter("vet", vet)
                .setParameter("date", date)
                .setParameter("time", time)
                .getSingleResult();
        return count > 0;
    }
}
