package org.springframework.samples.petclinic.mapper;

import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.dto.AppointmentDto;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapperImpl implements AppointmentMapper {
    @Override
    public Appointment toAppointment(AppointmentDto appointmentDto) {
        if ( appointmentDto == null ) {
            return null;
        }

        Appointment appointment = new Appointment();
        appointment.setId(appointmentDto.getId());
        appointment.setDate(appointmentDto.getDate());
        appointment.setTime(appointmentDto.getTime());
        appointment.setReason(appointmentDto.getReason());
        appointment.setPet(appointmentDtoToPet(appointmentDto));
        appointment.setVet(appointmentDtoToVet(appointmentDto));

        return appointment;
    }

    protected Pet appointmentDtoToPet(AppointmentDto appointmentDto) {
        if ( appointmentDto == null ) {
            return null;
        }

        Pet pet = new Pet();

        pet.setId( appointmentDto.getPetId() );

        return pet;
    }

    protected Vet appointmentDtoToVet(AppointmentDto appointmentDto) {
        if ( appointmentDto == null ) {
            return null;
        }

        Vet vet = new Vet();

        vet.setId( appointmentDto.getVetId() );

        return vet;
    }

    @Override
    public AppointmentDto toAppointmentDto(Appointment appointment) {
        if ( appointment == null ) {
            return null;
        }

        AppointmentDto appointmentDto = new AppointmentDto();

        appointmentDto.setId(appointment.getId());
        appointmentDto.setDate(appointment.getDate());
        appointmentDto.setTime(appointment.getTime());
        appointmentDto.setReason(appointment.getReason());
        appointmentDto.setPetId(appointment.getPet().getId());
        appointmentDto.setVetId(appointment.getVet().getId());

        return appointmentDto;
    }

}
