package org.springframework.samples.petclinic.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@SecurityRequirement(name = "petclinic-api")
@RequestMapping("api/appointments")
public interface AppointmentsApi {

    @Operation(summary = "List all appointments")
    @ApiResponse(responseCode = "200", description = "List of appointments")
    @GetMapping("")
    ResponseEntity<?> getAppointments();

    @Operation(summary = "Create a new appointment")
    @ApiResponse(responseCode = "201", description = "Appointment created")
    @PostMapping("")
    ResponseEntity<?> addAppointment(@Valid @RequestBody AppointmentDto appointmentDto);
}