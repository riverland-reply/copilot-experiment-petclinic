import { Component, OnInit } from '@angular/core';
import { Appointment } from '../appointment';
import { AppointmentService } from '../appointment.service';

@Component({
  selector: 'app-appointment-list',
  templateUrl: './appointment-list.component.html',
  styleUrls: ['./appointment-list.component.css']
})
export class AppointmentListComponent implements OnInit {
  appointments: Appointment[] = [];
  errorMessage: string | undefined;

  constructor(private appointmentService: AppointmentService) {}

  ngOnInit() {
    this.appointmentService.getAppointments().subscribe({
      next: appts => (this.appointments = appts),
      error: err => (this.errorMessage = err as any)
    });
  }
}
