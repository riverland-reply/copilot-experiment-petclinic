import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Appointment } from './appointment.model';
import { environment } from '../../environments/environment';

interface ApiResponse {
  value: Appointment[];
  Count: number;
}

@Injectable({ providedIn: 'root' })
export class AppointmentService {
  private apiUrl = environment.REST_API_URL + 'appointments/upcoming';

  constructor(private http: HttpClient) {}

  getUpcomingAppointments(): Observable<Appointment[]> {
    return new Observable<Appointment[]>(observer => {
      this.http.get<ApiResponse>(this.apiUrl).subscribe({
        next: (response) => {
          if (response && response.value) {
            observer.next(response.value);
          } else {
            observer.next([]);
          }
          observer.complete();
        },
        error: (err) => {
          console.error('Error fetching appointments', err);
          observer.error(err);
        }
      });
    });
  }
}
