package org.springframework.samples.petclinic.rest.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDto {
    private Integer id;
    private LocalDate date;
    private LocalTime time;
    private String reason;
    private Integer petId;
    private Integer vetId; // Optional convenience field

    // Constructors
    public AppointmentDto() {}

    public AppointmentDto(Integer id, LocalDate date, LocalTime time, String reason, Integer petId, Integer vetId) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.petId = petId;
        this.vetId = vetId;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public Integer getVetId() {
        return vetId;
    }

    public void setVetId(Integer vetId) {
        this.vetId = vetId;
    }
}
