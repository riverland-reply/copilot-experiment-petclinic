package org.springframework.samples.petclinic.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentRestController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllUpcomingAppointments() {
        List<Appointment> appointments = appointmentService.findAllUpcomingAppointments();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        try {
            Appointment savedAppointment = appointmentService.saveAppointment(appointment);
            return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
} 