import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Appointment } from '../appointment';
import { AppointmentService } from '../appointment.service';
import { VetService } from '../../vets/vet.service';
import { Vet } from '../../vets/vet';


@Component({
  selector: 'app-appointment-add',
  templateUrl: './appointment-add.component.html',
  styleUrls: ['./appointment-add.component.css']
})

export class AppointmentAddComponent implements OnInit {
  appointment: Appointment = {id: null, vetId: 0, dateTime: '', description: ''};
  errorMessage: string | undefined;
  vets: Vet[] = [];
  constructor(private appointmentService: AppointmentService, private vetService: VetService, private router: Router) {}

  ngOnInit() {
    this.vetService.getVets().subscribe(vs => this.vets = vs);
  }


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
