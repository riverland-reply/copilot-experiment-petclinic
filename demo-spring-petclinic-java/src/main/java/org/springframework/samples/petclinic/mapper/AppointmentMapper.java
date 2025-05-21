package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;

import java.util.Collection;

@Mapper
public interface AppointmentMapper {

    @Mapping(source = "petId", target = "pet.id")
    @Mapping(source = "vetId", target = "vet.id")
    Appointment toAppointment(AppointmentDto dto);

    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "vet.id", target = "vetId")
    AppointmentDto toAppointmentDto(Appointment appointment);

    Collection<AppointmentDto> toAppointmentDtos(Collection<Appointment> appointments);
}
