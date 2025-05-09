package org.springframework.samples.petclinic.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/upcoming")
    public List<AppointmentDto> getUpcomingAppointments() {
        return appointmentService.getUpcomingAppointments().stream()
                .map(this::toDtoAppointment)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        try {
            Appointment saved = appointmentService.createAppointment(appointment);
            return new ResponseEntity<>(toDtoAppointment(saved), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    private AppointmentDto toDtoAppointment(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setDate(appointment.getDate());
        dto.setTime(appointment.getTime());
        dto.setReason(appointment.getReason());
        if (appointment.getPet() != null) {
            dto.setPetId(appointment.getPet().getId());
            dto.setPetName(appointment.getPet().getName());
        }
        if (appointment.getVet() != null) {
            dto.setVetId(appointment.getVet().getId());
            String vetName = "";
            if (appointment.getVet().getFirstName() != null) vetName += appointment.getVet().getFirstName();
            if (appointment.getVet().getLastName() != null) vetName += (vetName.isEmpty() ? "" : " ") + appointment.getVet().getLastName();
            dto.setVetName(vetName);
        }
        return dto;
    }
}

