import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Appointment } from './appointment';
import { environment } from '../../environments/environment';
import { HandleError, HttpErrorHandler } from '../error.service';

@Injectable()
export class AppointmentService {
  private entityUrl = environment.REST_API_URL + 'appointments';
  private readonly handlerError: HandleError;

  constructor(private http: HttpClient, httpErrorHandler: HttpErrorHandler) {
    this.handlerError = httpErrorHandler.createHandleError('AppointmentService');
  }

  getAppointments(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(this.entityUrl).pipe(
      catchError(this.handlerError('getAppointments', []))
    );
  }

  searchAppointments(vetId: number): Observable<Appointment[]> {
    const url = `${this.entityUrl}/search?vetId=${vetId}`;
    return this.http.get<Appointment[]>(url).pipe(
      catchError(this.handlerError('searchAppointments', []))
    );
  }

  getAppointment(id: number): Observable<Appointment> {
    return this.http.get<Appointment>(`${this.entityUrl}/${id}`).pipe(
      catchError(this.handlerError('getAppointment', {} as Appointment))
    );
  }

  updateAppointment(id: number, appointment: Appointment): Observable<Appointment> {
    return this.http.put<Appointment>(`${this.entityUrl}/${id}`, appointment).pipe(
      catchError(this.handlerError('updateAppointment', appointment))
    );
  }

  deleteAppointment(id: number): Observable<number> {
    return this.http.delete<number>(`${this.entityUrl}/${id}`).pipe(
      catchError(this.handlerError('deleteAppointment', 0))
    );
  }


  addAppointment(appointment: Appointment): Observable<Appointment> {
    return this.http.post<Appointment>(this.entityUrl, appointment).pipe(
      catchError(this.handlerError('addAppointment', appointment))
    );
  }
}
