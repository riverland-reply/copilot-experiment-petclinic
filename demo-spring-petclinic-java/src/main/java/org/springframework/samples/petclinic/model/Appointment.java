package org.springframework.samples.petclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    @Column(name = "appointment_datetime_start")
    @NotNull
    private LocalDateTime appointmentDateTimeStart;

    @Column(name = "appointment_datetime_end")
    @NotNull
    private LocalDateTime appointmentDateTimeEnd;

    @Column(name = "reason")
    @NotNull
    @Size(min = 3, max = 255)
    private String reason;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    @NotNull
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "vet_id")
    @NotNull
    private Vet vet;

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

    public LocalDateTime getAppointmentDateTimeStart() {
        return appointmentDateTimeStart;
    }

    public void setAppointmentDateTimeStart(LocalDateTime appointmentDateTimeStart) {
        this.appointmentDateTimeStart = appointmentDateTimeStart;
    }

    public LocalDateTime getAppointmentDateTimeEnd() {
        return appointmentDateTimeEnd;
    }

    public void setAppointmentDateTimeEnd(LocalDateTime appointmentDateTimeEnd) {
        this.appointmentDateTimeEnd = appointmentDateTimeEnd;
    }
}

