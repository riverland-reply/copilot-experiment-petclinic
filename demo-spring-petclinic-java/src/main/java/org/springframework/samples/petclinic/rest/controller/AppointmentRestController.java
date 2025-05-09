package org.springframework.samples.petclinic.rest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.AppointmentMapper;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.rest.api.AppointmentsApi;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;
import org.springframework.samples.petclinic.rest.exceptions.AppointmentConflictException;
import org.springframework.samples.petclinic.rest.exceptions.ResourceNotFoundException;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api/appointments")
public class AppointmentRestController implements AppointmentsApi {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    public AppointmentRestController(AppointmentService appointmentService, AppointmentMapper appointmentMapper) {
        this.appointmentService = appointmentService;
        this.appointmentMapper = appointmentMapper;
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @GetMapping
    public ResponseEntity<?> getAppointments() {
        Collection<Appointment> appointments = this.appointmentService.findAllAppointments();
        if (appointments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No appointments present in the calendar");
        }
        return new ResponseEntity<>(
                appointments.stream()
                        .map(appointmentMapper::toAppointmentDto)
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @PostMapping
    public ResponseEntity<?> addAppointment(@RequestBody @Valid AppointmentDto appointmentDto) {
        HttpHeaders headers = new HttpHeaders();
        try {
        Appointment appointment = appointmentMapper.toAppointment(appointmentDto);
        this.appointmentService.saveAppointment(appointment, appointmentDto);
        headers.setLocation(
                UriComponentsBuilder.newInstance()
                        .path("/api/appointments/{id}")
                        .buildAndExpand(appointment.getId())
                        .toUri()
        );
        return new ResponseEntity<>(appointmentMapper.toAppointmentDto(appointment), headers, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AppointmentConflictException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict for the appointment: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating appointment: " + e.getMessage());
        }

    }
}