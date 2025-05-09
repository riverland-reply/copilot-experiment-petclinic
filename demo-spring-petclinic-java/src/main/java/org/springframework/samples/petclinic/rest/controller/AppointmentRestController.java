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
package org.springframework.samples.petclinic.rest.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Vitaliy Fedoriv
 */

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api/appointments")
public class AppointmentRestController {

    private final ClinicService clinicService;

    @Autowired
    public AppointmentRestController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @GetMapping(value = "")
    public ResponseEntity<Collection<Appointment>> getAllAppointments() {
        Collection<Appointment> appointments = this.clinicService.findAllAppointments();
        if (appointments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @GetMapping(value = "/future")
    public ResponseEntity<Collection<Appointment>> getFutureAppointments() {
        Collection<Appointment> allAppointments = this.clinicService.findAllAppointments();
        if (allAppointments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Filter appointments to include only those with dates in the future
        LocalDate today = LocalDate.now();
        Collection<Appointment> futureAppointments = allAppointments.stream()
            .filter(appointment -> appointment.getDate().isEqual(today) || appointment.getDate().isAfter(today))
            .collect(Collectors.toList());
        
        if (futureAppointments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(futureAppointments, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @GetMapping(value = "/{appointmentId}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable("appointmentId") int appointmentId) {
        Appointment appointment = this.clinicService.findAppointmentById(appointmentId);
        if (appointment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @GetMapping(value = "/pet/{petId}")
    public ResponseEntity<Collection<Appointment>> getAppointmentsByPetId(@PathVariable("petId") int petId) {
        Collection<Appointment> appointments = this.clinicService.findAppointmentsByPetId(petId);
        if (appointments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @GetMapping(value = "/vet/{vetId}")
    public ResponseEntity<Collection<Appointment>> getAppointmentsByVetId(@PathVariable("vetId") int vetId) {
        Collection<Appointment> appointments = this.clinicService.findAppointmentsByVetId(vetId);
        if (appointments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @PostMapping(value = "")
    public ResponseEntity<Appointment> addAppointment(@RequestBody @Valid Appointment appointment, BindingResult bindingResult, UriComponentsBuilder ucBuilder) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (appointment == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        
        // Validate pet and vet exist
        Pet pet = this.clinicService.findPetById(appointment.getPet().getId());
        if (pet == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        appointment.setPet(pet);
        
        Vet vet = this.clinicService.findVetById(appointment.getVet().getId());
        if (vet == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        appointment.setVet(vet);
        
        try {
            this.clinicService.saveAppointment(appointment);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        
        headers.setLocation(ucBuilder.path("/api/appointments/{id}").buildAndExpand(appointment.getId()).toUri());
        return new ResponseEntity<>(appointment, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @PutMapping(value = "/{appointmentId}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable("appointmentId") int appointmentId, @RequestBody @Valid Appointment appointment, BindingResult bindingResult) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (appointment == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        
        Appointment currentAppointment = this.clinicService.findAppointmentById(appointmentId);
        if (currentAppointment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Validate pet and vet exist
        Pet pet = this.clinicService.findPetById(appointment.getPet().getId());
        if (pet == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        appointment.setPet(pet);
        
        Vet vet = this.clinicService.findVetById(appointment.getVet().getId());
        if (vet == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        appointment.setVet(vet);
        
        currentAppointment.setDate(appointment.getDate());
        currentAppointment.setTime(appointment.getTime());
        currentAppointment.setReason(appointment.getReason());
        currentAppointment.setPet(appointment.getPet());
        currentAppointment.setVet(appointment.getVet());
        
        try {
            this.clinicService.saveAppointment(currentAppointment);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(currentAppointment, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @DeleteMapping(value = "/{appointmentId}")
    @Transactional
    public ResponseEntity<Void> deleteAppointment(@PathVariable("appointmentId") int appointmentId) {
        Appointment appointment = this.clinicService.findAppointmentById(appointmentId);
        if (appointment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.clinicService.deleteAppointment(appointment);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}