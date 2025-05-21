import { Component, OnInit } from '@angular/core';
import { Appointment } from '../appointment';
import { AppointmentService } from '../appointment.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-appointment-list',
  templateUrl: './appointment-list.component.html',
  styleUrls: ['./appointment-list.component.css']
})
export class AppointmentListComponent implements OnInit {
  appointments: Appointment[] = [];
  errorMessage: string | undefined;
  vetId: number | null = null;

  constructor(private appointmentService: AppointmentService, private router: Router) {}

  ngOnInit() {
    this.appointmentService.getAppointments().subscribe({
      next: appts => (this.appointments = appts),
      error: err => (this.errorMessage = err as any)
    });
  }

  search() {
    if (this.vetId == null) {
      this.appointmentService.getAppointments().subscribe(appts => this.appointments = appts);
    } else {
      this.appointmentService.searchAppointments(this.vetId).subscribe(appts => this.appointments = appts);
    }
  }

  addAppointment() {
    this.router.navigate(['/appointments/add']);
  }

  editAppointment(appt: Appointment) {
    this.router.navigate(['/appointments', appt.id, 'edit']);
  }

  deleteAppointment(appt: Appointment) {
    if (appt.id != null)
      this.appointmentService.deleteAppointment(appt.id).subscribe(() => this.search());
  }
}
