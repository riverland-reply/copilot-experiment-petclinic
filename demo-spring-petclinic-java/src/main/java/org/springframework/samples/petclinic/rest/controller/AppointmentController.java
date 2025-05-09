package org.springframework.samples.petclinic.rest.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.AppointmentMapper;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    private final AppointmentMapper appointmentMapper;

    private final ClinicService clinicService;

    public AppointmentController(AppointmentMapper appointmentMapper, ClinicService clinicService) {
        this.appointmentMapper = appointmentMapper;
        this.clinicService = clinicService;
    }

    @PostMapping("/appointments")

    public ResponseEntity<AppointmentDto> createAppointment(AppointmentDto appointmentDto) {
        HttpHeaders headers = new HttpHeaders();

        Appointment appointment = appointmentMapper.toAppointment(appointmentDto);
        this.clinicService.saveAppointment(appointment);
        //headers.setLocation(UriComponentsBuilder.newInstance().path("/api/pets/{id}").buildAndExpand(pet.getId()).toUri());
        return new ResponseEntity<>(appointmentMapper.toAppointmentDto(appointment), headers, HttpStatus.CREATED);

    }

}

