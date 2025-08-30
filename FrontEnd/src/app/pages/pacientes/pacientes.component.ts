import { Component } from '@angular/core';
import { Paciente, PacientesService } from 'src/app/services/pacientes.service';

@Component({
  selector: 'app-pacientes',
  templateUrl: './pacientes.component.html',
  styleUrls: ['./pacientes.component.scss']
})

export class PacientesComponent {
  
  andar = 'Todos';
  pacientes: Paciente[] = [];

  constructor(private pacientesService: PacientesService) {}

  ngOnInit() {
    this.pacientesService.getAllPacientes().subscribe( data => {
      this.pacientes = data;
    });
  }
}
