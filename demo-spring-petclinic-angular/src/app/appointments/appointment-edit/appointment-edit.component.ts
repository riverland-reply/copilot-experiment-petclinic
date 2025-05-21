import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Appointment } from '../appointment';
import { AppointmentService } from '../appointment.service';
import { VetService } from '../../vets/vet.service';
import { Vet } from '../../vets/vet';

@Component({
  selector: 'app-appointment-edit',
  templateUrl: './appointment-edit.component.html',
  styleUrls: ['./appointment-edit.component.css']
})
export class AppointmentEditComponent implements OnInit {
  appointment: Appointment = {id: null, vetId: 0, dateTime: '', description: ''};
  vets: Vet[] = [];
  errorMessage: string | undefined;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private appointmentService: AppointmentService,
              private vetService: VetService) {}

  ngOnInit() {
    const id = this.route.snapshot.params.id;
    this.vetService.getVets().subscribe(v => this.vets = v);
    this.appointmentService.getAppointment(+id).subscribe(appt => this.appointment = appt);
  }

  onSubmit() {
    if (this.appointment.id != null) {
      this.appointmentService.updateAppointment(this.appointment.id, this.appointment).subscribe({
        next: () => this.gotoList(),
        error: err => this.errorMessage = err as any
      });
    }
  }

  gotoList() {
    this.router.navigate(['/appointments']);
  }
}
