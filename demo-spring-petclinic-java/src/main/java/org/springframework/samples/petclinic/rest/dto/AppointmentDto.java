package org.springframework.samples.petclinic.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class AppointmentDto {
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime appointmentDateTimeStart;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime appointmentDateTimeEnd;
    private String reason;
    private Integer petId;
    private Integer vetId;
    private String vetFirstName;
    private String vetLastName;
    private String petName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getVetFirstName() {
        return vetFirstName;
    }

    public void setVetFirstName(String vetFirstName) {
        this.vetFirstName = vetFirstName;
    }

    public String getVetLastName() {
        return vetLastName;
    }

    public void setVetLastName(String vetLastName) {
        this.vetLastName = vetLastName;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }
}