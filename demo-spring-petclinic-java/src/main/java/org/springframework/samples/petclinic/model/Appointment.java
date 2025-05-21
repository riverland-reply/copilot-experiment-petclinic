package org.springframework.samples.petclinic.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Simple domain object representing an appointment between a pet and a vet.
 */
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "reason")
    private String reason;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "vet_id")
    private Vet vet;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Vet getVet() {
        return vet;
    }

    public void setVet(Vet vet) {
        this.vet = vet;
    }
}
