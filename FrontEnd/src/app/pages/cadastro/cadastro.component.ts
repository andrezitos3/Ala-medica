import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Paciente, PacientesService } from 'src/app/services/pacientes.service';

@Component({
  selector: 'app-cadastro',
  templateUrl: './cadastro.component.html',
  styleUrls: ['./cadastro.component.scss']
})
export class CadastroComponent {

  cadastroForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private pacienteService: PacientesService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cadastroForm = this.fb.group({
      id: [''],
      nome: ['', Validators.required],
      nascimento: ['', Validators.required],
      andar: ['', Validators.required],
      quarto: [''],
      internacao: [false],
      enfermidade: [''],
      alergia: ['']
    });
  }

  salvar() {
    if (this.cadastroForm.valid) {
      const paciente = this.cadastroForm.value as Omit<Paciente, 'id'>
      console.log(this.cadastroForm.value);

      this.pacienteService.addPaciente(paciente).subscribe({
        next: (novo) => {
          console.log('Paciente criado:', novo);
          this.router.navigate(['/pacientes']);
        } ,
        error: (err) => console.error('Erro ao salvar paciente:', err)
      })

      alert('Paciente cadastrado com sucesso!');


    } else {
      alert('Preencha todos os campos obrigatórios.');
    }
  }

  cancelar() {
    this.cadastroForm.reset({
      codigo: '',
      internacao: false
    });
  }

}
