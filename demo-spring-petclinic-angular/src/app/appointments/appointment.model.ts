export interface Appointment {
  id: number;
  date: string; // ISO string
  time: string; // ISO string or HH:mm:ss
  reason: string;
  petId: number;
  petName: string;
  vetId: number;
  vetName: string;
}
