package org.springframework.samples.petclinic.rest.controller;

import java.util.Collection;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Collection<Appointment>> getAllAppointments() {
        Collection<Appointment> appointments = this.appointmentService.findAllAppointments();
        if (appointments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping(value = "/{appointmentId}", produces = "application/json")
    public ResponseEntity<Appointment> getAppointment(@PathVariable("appointmentId") int appointmentId) {
        Optional<Appointment> appointment = this.appointmentService.findAppointmentById(appointmentId);
        if (!appointment.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(appointment.get(), HttpStatus.OK);
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<Appointment> addAppointment(@RequestBody Appointment appointment, BindingResult bindingResult, UriComponentsBuilder ucBuilder) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (appointment == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toString());
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        this.appointmentService.saveAppointment(appointment);
        headers.setLocation(ucBuilder.path("/api/appointments/{id}").buildAndExpand(appointment.getId()).toUri());
        return new ResponseEntity<>(appointment, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{appointmentId}", produces = "application/json")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable("appointmentId") int appointmentId, @RequestBody Appointment appointment, BindingResult bindingResult) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (appointment == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toString());
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
 }
 Optional<Appointment> currentAppointment = this.appointmentService.findAppointmentById(appointmentId);
        if (currentAppointment == null || currentAppointment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Appointment arr = currentAppointment.get();
        arr.setDate(appointment.getDate());
        arr.setTime(appointment.getTime());
        arr.setReason(appointment.getReason());
        arr.setPet(appointment.getPet()); // Assuming Pet has getId() or similar
        arr.setVet(appointment.getVet());
        this.appointmentService.saveAppointment(arr);
        return new ResponseEntity<>(arr, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{appointmentId}", produces = "application/json")
    public ResponseEntity<Void> deleteAppointment(@PathVariable("appointmentId") int appointmentId) {
        Optional<Appointment> appointment = this.appointmentService.findAppointmentById(appointmentId);
        if (!appointment.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.appointmentService.deleteAppointment(appointment.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}