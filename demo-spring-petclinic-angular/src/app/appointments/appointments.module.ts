import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppointmentListComponent } from './appointment-list/appointment-list.component';
import { AppointmentAddComponent } from './appointment-add/appointment-add.component';
import { AppointmentService } from './appointment.service';
import { AppointmentsRoutingModule } from './appointments-routing.module';

@NgModule({
  imports: [CommonModule, FormsModule, AppointmentsRoutingModule],
  declarations: [AppointmentListComponent, AppointmentAddComponent],
  exports: [AppointmentListComponent, AppointmentAddComponent],
  providers: [AppointmentService]
})
export class AppointmentsModule {}
