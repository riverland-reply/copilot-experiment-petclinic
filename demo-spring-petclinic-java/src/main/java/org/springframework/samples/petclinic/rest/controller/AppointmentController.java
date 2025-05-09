package org.springframework.samples.petclinic.rest.controller;

import jakarta.validation.Valid;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.controller.dto.AppointmentCreationDTO;
import org.springframework.samples.petclinic.rest.controller.dto.AppointmentResponseDTO;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is a REST controller for managing appointments.
 * It provides endpoints for retrieving and creating appointments.
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    /**
         * Retrieves a list of upcoming appointments.
         * An appointment is considered upcoming if its date is in the future
         * or if it is scheduled for today but at a later time.
         *
         * @return a list of {@link AppointmentResponseDTO} representing the upcoming appointments
         */
    @GetMapping("/upcoming")
    public List<AppointmentResponseDTO> getUpcomingAppointments() {
        return appointmentService.findAll().stream()
                .filter(appointment -> appointment.getDate().isAfter(LocalDate.now()) ||
                        (appointment.getDate().isEqual(LocalDate.now()) && appointment.getTime().isAfter(LocalTime.now())))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    /**
         * Creates a new appointment based on the provided data.
         * The appointment details are validated before being saved.
         *
         * @param appointmentDTO the data transfer object containing the details of the appointment to be created
         * @return the created appointment as a {@link AppointmentResponseDTO}
         */
    @PostMapping
    public AppointmentResponseDTO createAppointment(@RequestBody @Valid AppointmentCreationDTO appointmentDTO) {
        Appointment appointment = mapToAppointment(appointmentDTO);
        return convertToDTO(appointmentService.save(appointment));
    }

    /**
         * Converts an Appointment entity to an AppointmentResponseDTO.
         * This method is used to transform the Appointment object into a format
         * suitable for API responses.
         *
         * @param appointment the Appointment entity to be converted
         * @return the corresponding AppointmentResponseDTO
         */
    private AppointmentResponseDTO convertToDTO(Appointment appointment) {
        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        dto.setId(appointment.getId());
        dto.setDate(appointment.getDate());
        dto.setTime(appointment.getTime());
        dto.setReason(appointment.getReason());
        dto.setPetId((long) (appointment.getPet() != null ? appointment.getPet().getId() : 0));
        dto.setVetId((long) (appointment.getVet() != null ? appointment.getVet().getId() : 0));
        return dto;
    }

    /**
         * Maps an AppointmentCreationDTO to an Appointment entity.
         * This method transforms the data transfer object into an Appointment object
         * and fetches the associated Pet and Vet entities from the database.
         *
         * @param appointmentDTO the data transfer object containing appointment details
         * @return the mapped Appointment entity
         */
    private Appointment mapToAppointment(AppointmentCreationDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        appointment.setDate(appointmentDTO.getDate());
        appointment.setTime(appointmentDTO.getTime());
        appointment.setReason(appointmentDTO.getReason());

        // Fetch Pet and Vet from the database
        Pet petReference = appointmentService.findPetById(appointmentDTO.getPetId());
        Vet vetReference = appointmentService.findVetById(appointmentDTO.getVetId());

        // Set the managed entities
        appointment.setPet(petReference);
        appointment.setVet(vetReference);

        return appointment;
    }


}