package org.springframework.samples.petclinic.rest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.AppointmentMapper;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api")
public class AppointmentRestController {

    private final ClinicService clinicService;
    private final AppointmentMapper appointmentMapper;

    public AppointmentRestController(ClinicService clinicService, AppointmentMapper appointmentMapper) {
        this.clinicService = clinicService;
        this.appointmentMapper = appointmentMapper;
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDto>> listAppointments() {
        List<AppointmentDto> appointments = new ArrayList<>(appointmentMapper.toAppointmentDtos(
            this.clinicService.findUpcomingAppointments()));
        if (appointments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @PostMapping("/appointments")
    public ResponseEntity<AppointmentDto> addAppointment(@RequestBody AppointmentDto appointmentDto) {
        HttpHeaders headers = new HttpHeaders();
        Appointment appointment = appointmentMapper.toAppointment(appointmentDto);
        this.clinicService.saveAppointment(appointment);
        AppointmentDto dto = appointmentMapper.toAppointmentDto(appointment);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/appointments/{id}")
            .buildAndExpand(appointment.getId()).toUri());
        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }
}
