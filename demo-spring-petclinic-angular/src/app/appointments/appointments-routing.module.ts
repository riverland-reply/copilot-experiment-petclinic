import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppointmentListComponent } from './appointment-list/appointment-list.component';
import { AppointmentAddComponent } from './appointment-add/appointment-add.component';

const appointmentRoutes: Routes = [
  { path: 'appointments', component: AppointmentListComponent },
  { path: 'appointments/add', component: AppointmentAddComponent }
];

@NgModule({
  imports: [RouterModule.forChild(appointmentRoutes)],
  exports: [RouterModule]
})
export class AppointmentsRoutingModule {}
