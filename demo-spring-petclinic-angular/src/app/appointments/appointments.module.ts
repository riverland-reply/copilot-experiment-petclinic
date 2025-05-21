import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppointmentListComponent } from './appointment-list/appointment-list.component';
import { AppointmentAddComponent } from './appointment-add/appointment-add.component';
import { AppointmentEditComponent } from './appointment-edit/appointment-edit.component';
import { AppointmentService } from './appointment.service';
import { AppointmentsRoutingModule } from './appointments-routing.module';

@NgModule({
  imports: [CommonModule, FormsModule, AppointmentsRoutingModule],
  declarations: [AppointmentListComponent, AppointmentAddComponent, AppointmentEditComponent],
  exports: [AppointmentListComponent, AppointmentAddComponent, AppointmentEditComponent],
  providers: [AppointmentService]
})
export class AppointmentsModule {}
