import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Appointment } from '../appointment';
import { AppointmentService } from '../appointment.service';

@Component({
  selector: 'app-appointment-add',
  templateUrl: './appointment-add.component.html',
  styleUrls: ['./appointment-add.component.css']
})
export class AppointmentAddComponent {
  appointment: Appointment = {} as Appointment;
  errorMessage: string | undefined;

  constructor(private appointmentService: AppointmentService, private router: Router) {}

  onSubmit(appointment: Appointment) {
    this.appointmentService.addAppointment(appointment).subscribe({
      next: () => this.gotoList(),
      error: err => this.errorMessage = err as any
    });
  }

  gotoList() {
    this.router.navigate(['/appointments']);
  }
}
