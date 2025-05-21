package org.springframework.samples.petclinic.rest.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Appointment;

import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.Collection;

@RestController
@CrossOrigin(origins = "http://localhost:8080", exposedHeaders = "errors, content-type")
@RequestMapping("/api/appointments")
public class AppointmentRestController {

    private final ClinicService clinicService;

    public AppointmentRestController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @GetMapping
    public ResponseEntity<Collection<Appointment>> all() {
        var result = clinicService.findAllAppointments();
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @GetMapping("/search")
    public ResponseEntity<Collection<Appointment>> search(@RequestParam int vetId) {
        var result = clinicService.findAppointmentsByVetId(vetId);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> get(@PathVariable("id") int id) {
        var appt = clinicService.findAppointmentById(id);
        if (appt == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(appt, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @PostMapping
    public ResponseEntity<Appointment> add(@RequestBody Appointment appointment, UriComponentsBuilder builder) {
        clinicService.saveAppointment(appointment);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> update(@PathVariable("id") int id, @RequestBody Appointment appointment) {
        Appointment current = clinicService.findAppointmentById(id);
        if (current == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        current.setDateTime(appointment.getDateTime());
        current.setDescription(appointment.getDescription());
        current.setVet(appointment.getVet());
        clinicService.saveAppointment(current);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        Appointment current = clinicService.findAppointmentById(id);
        if (current == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        clinicService.deleteAppointment(current);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
