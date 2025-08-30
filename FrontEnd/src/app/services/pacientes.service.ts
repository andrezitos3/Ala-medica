import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Paciente {
  id: number, 
  nome: string, 
  internacao: boolean, 
  nascimento: string,
  enfermidade: string,
  alergia: string,
  quarto: string
}

@Injectable({
  providedIn: 'root'
})
export class PacientesService {

  private apiUrl = 'http://localhost:3000/pacientes';

  constructor(private httpClient: HttpClient) {}

  getAllPacientes(): Observable<Paciente[]> {
    return this.httpClient.get<Paciente[]>(this.apiUrl);
  }

  getPaciente(id: number): Observable<Paciente> {
    return this.httpClient.get<Paciente>(`${this.apiUrl}/${id}`)
  }
}
