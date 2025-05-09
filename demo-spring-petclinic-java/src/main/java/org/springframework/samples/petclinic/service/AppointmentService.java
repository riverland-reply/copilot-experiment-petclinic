package org.springframework.samples.petclinic.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing appointments, pets, and vets.
 */
@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;
    private final VetRepository vetRepository;


    public AppointmentService(AppointmentRepository appointmentRepository, PetRepository petRepository, VetRepository vetRepository) {
        this.appointmentRepository = appointmentRepository;
        this.petRepository = petRepository;
        this.vetRepository = vetRepository;
    }

    /**
         * Retrieves all appointments from the repository.
         *
         * @return a list of all appointments
         */
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    /**
         * Retrieves an appointment by its ID.
         *
         * @param id the ID of the appointment to retrieve
         * @return an Optional containing the appointment if found, or empty if not found
         */
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    /**
         * Saves a new appointment to the repository.
         * Ensures that no duplicate appointment exists for the same vet, date, and time.
         *
         * @param appointment the appointment to save
         * @return the saved appointment
         * @throws IllegalArgumentException if an appointment with the same vet, date, and time already exists
         */
    public Appointment save(Appointment appointment) {
        boolean exists = appointmentRepository.existsByVetAndDateAndTime(
                appointment.getVet(), appointment.getDate(), appointment.getTime()
        );

        if (exists) {
            throw new IllegalArgumentException("An appointment with the same vet, date, and time already exists.");
        }

        return appointmentRepository.save(appointment);
    }


    /**
         * Retrieves a pet by its ID.
         *
         * @param petId the ID of the pet to retrieve
         * @return the pet with the specified ID
         * @throws EntityNotFoundException if no pet is found with the given ID
         */
    public Pet findPetById(int petId) {
        return Optional.ofNullable(petRepository.findById(petId))
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + petId));
    }

    /**
         * Retrieves a vet by its ID.
         *
         * @param vetId the ID of the vet to retrieve
         * @return the vet with the specified ID
         * @throws EntityNotFoundException if no vet is found with the given ID
         */
    public Vet findVetById(int vetId) {
        return Optional.ofNullable(vetRepository.findById(vetId))
                .orElseThrow(() -> new EntityNotFoundException("Vet not found with ID: " + vetId));
    }
}