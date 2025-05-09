package org.springframework.samples.petclinic.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    @Autowired
    private ClinicService clinicService;

    public AppointmentDto toAppointmentDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(appointment.getId());
        appointmentDto.setAppointmentDateTimeStart(appointment.getAppointmentDateTimeStart());
        appointmentDto.setAppointmentDateTimeEnd(appointment.getAppointmentDateTimeEnd());
        appointmentDto.setReason(appointment.getReason());
        appointmentDto.setPetId(appointment.getPet().getId());
        appointmentDto.setVetId(appointment.getVet().getId());
        appointmentDto.setVetFirstName(appointment.getVet().getFirstName());
        appointmentDto.setVetLastName(appointment.getVet().getLastName());
        appointmentDto.setPetName(appointment.getPet().getName());
        return appointmentDto;
    }

    public Appointment toAppointment(AppointmentDto appointmentDto) {
        if (appointmentDto == null) {
            return null;
        }
        Appointment appointment = new Appointment();
        appointment.setId(appointmentDto.getId());
        appointment.setAppointmentDateTimeStart(appointmentDto.getAppointmentDateTimeStart());
        appointment.setAppointmentDateTimeEnd(appointmentDto.getAppointmentDateTimeEnd());
        appointment.setReason(appointmentDto.getReason());
        appointment.setPet(clinicService.findPetById(appointmentDto.getPetId()));
        appointment.setVet(clinicService.findVetById(appointmentDto.getVetId()));
        return appointment;
    }
}