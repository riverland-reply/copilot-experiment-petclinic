package org.springframework.samples.petclinic.mapper;

import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;

public interface AppointmentMapper {

    Appointment toAppointment(AppointmentDto appointmentDto);

    AppointmentDto toAppointmentDto(Appointment appointment);
}
