
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AppointmentService } from '../appointment.service';
import { Appointment } from '../appointment';

@Component({
  selector: 'app-appointment-list',
  templateUrl: './appointment-list.component.html',
  styleUrls: ['./appointment-list.component.css']
})
export class AppointmentListComponent implements OnInit {
  appointments: Appointment[] = [];
  appointmentForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private appointmentService: AppointmentService,
    private formBuilder: FormBuilder
  ) {
    this.appointmentForm = this.formBuilder.group({
      appointmentDateTimeStart: ['', Validators.required],
      appointmentDateTimeEnd: ['', Validators.required],
      reason: ['', Validators.required],
      petId: ['', Validators.required],
      vetId: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.loadAppointments();
  }

  loadAppointments() {
    this.appointmentService.getAppointments().subscribe(
      appointments => this.appointments = appointments,
      error => this.errorMessage = error
    );
  }

  onSubmit() {
    if (this.appointmentForm.valid) {
      this.appointmentService.addAppointment(this.appointmentForm.value).subscribe(
        () => {
          this.loadAppointments();
          this.appointmentForm.reset();
        },
        error => this.errorMessage = error
      );
    }
  }
}
