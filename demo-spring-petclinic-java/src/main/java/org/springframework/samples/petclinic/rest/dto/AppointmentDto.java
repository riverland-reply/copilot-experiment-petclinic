package org.springframework.samples.petclinic.rest.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDto {
    private Integer id;
    private LocalDate date;
    private LocalTime time;
    private String reason;
    private Integer petId;
    private String petName;
    private Integer vetId;
    private String vetName;

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Integer getPetId() { return petId; }
    public void setPetId(Integer petId) { this.petId = petId; }
    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }
    public Integer getVetId() { return vetId; }
    public void setVetId(Integer vetId) { this.vetId = vetId; }
    public String getVetName() { return vetName; }
    public void setVetName(String vetName) { this.vetName = vetName; }
}
