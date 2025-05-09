/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Simple JavaBean domain object representing an appointment.
 */
@Entity
@Table(name = "appointments")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Appointment extends BaseEntity {

    @Column(name = "appointment_date", columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column(name = "appointment_time")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    @NotEmpty
    @Column(name = "reason")
    private String reason;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    @JsonIgnoreProperties({"owner", "visits"})
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "vet_id")
    @JsonIgnoreProperties({"specialties"})
    private Vet vet;

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Pet getPet() {
        return this.pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Vet getVet() {
        return this.vet;
    }

    public void setVet(Vet vet) {
        this.vet = vet;
    }
} 