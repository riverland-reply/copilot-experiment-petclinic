
export interface Appointment {
  id?: number;
  appointmentDateTimeStart: string;
  appointmentDateTimeEnd: string;
  reason: string;
  petId: number;
  vetId: number;
  vetFirstName?: string;
  vetLastName?: string;
  petName?: string;
}
