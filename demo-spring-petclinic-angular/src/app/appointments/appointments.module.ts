
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { AppointmentListComponent } from './appointment-list/appointment-list.component';
import { AppointmentService } from './appointment.service';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  declarations: [AppointmentListComponent],
  providers: [AppointmentService]
})
export class AppointmentsModule { }
