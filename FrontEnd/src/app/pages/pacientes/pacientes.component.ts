import { Component } from '@angular/core';

interface Paciente {
  nome: string;
  id: string;
  internacao: boolean;
  nascimento: string;
  enfermidade: string;
  alergia: string;
  quarto: string;
}

@Component({
  selector: 'app-pacientes',
  templateUrl: './pacientes.component.html',
  styleUrls: ['./pacientes.component.scss']
})

export class PacientesComponent {
  
  andar = 'Todos';

  pacientes: Paciente[] = [
    {
      nome: 'Eddie Chen',
      id: '515122',
      internacao: false,
      nascimento: '02/07/2002',
      enfermidade: 'Dores Intestinais',
      alergia: 'Rivotril',
      quarto: '01-102'
    },
    {
      nome: 'Lurdes Gomes',
      id: '00012',
      internacao: true,
      nascimento: '27/02/1890',
      enfermidade: 'Osteoporose',
      alergia: 'Monoclorreto de Sódio',
      quarto: '07-705'
    }
  ];
}
