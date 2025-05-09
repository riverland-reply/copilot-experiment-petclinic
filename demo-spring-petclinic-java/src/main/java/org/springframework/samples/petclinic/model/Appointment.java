/*
 * Copyright 2002-2024 the original author or authors.
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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Simple JavaBean domain object representing an appointment.
 */
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    /**
     * Holds value of property date.
     */
    @NotNull
    @Column(name = "appointment_date", columnDefinition = "DATE")
    private LocalDate date;

    /**
     * Holds value of property time.
     */
    @NotNull
    @Column(name = "appointment_time", columnDefinition = "TIME")
    private LocalTime time;

    /**
     * Holds value of property reason.
     */
    @NotEmpty
    @Column(name = "reason")
    private String reason;

    /**
     * Holds value of property pet.
     */
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    /**
     * Holds value of property vet.
     */
    @ManyToOne
    @JoinColumn(name = "vet_id")
    private Vet vet;

    /**
     * Creates a new instance of Appointment for the current date
     */
    public Appointment() {
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }

    /**
     * Getter for property date.
     *
     * @return Value of property date.
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Setter for property date.
     *
     * @param date New value of property date.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Getter for property time.
     *
     * @return Value of property time.
     */
    public LocalTime getTime() {
        return this.time;
    }

    /**
     * Setter for property time.
     *
     * @param time New value of property time.
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * Getter for property reason.
     *
     * @return Value of property reason.
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Setter for property reason.
     *
     * @param reason New value of property reason.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Getter for property pet.
     *
     * @return Value of property pet.
     */
    public Pet getPet() {
        return this.pet;
    }

    /**
     * Setter for property pet.
     *
     * @param pet New value of property pet.
     */
    public void setPet(Pet pet) {
        this.pet = pet;
    }

    /**
     * Getter for property vet.
     *
     * @return Value of property vet.
     */
    public Vet getVet() {
        return this.vet;
    }

    /**
     * Setter for property vet.
     *
     * @param vet New value of property vet.
     */
    public void setVet(Vet vet) {
        this.vet = vet;
    }
}