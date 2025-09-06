import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-cadastro',
  templateUrl: './cadastro.component.html',
  styleUrls: ['./cadastro.component.scss']
})
export class CadastroComponent {

  cadastroForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.cadastroForm = this.fb.group({
      codigo: [{ value: '00001', disabled: true }],
      nome: ['', Validators.required],
      nascimento: ['', Validators.required],
      andar: ['', Validators.required],
      internacao: [true],
      doenca: [''],
      alergia: ['']
    });
  }

  salvar() {
    if (this.cadastroForm.valid) {
      console.log(this.cadastroForm.value);
      alert('Paciente cadastrado com sucesso!');
    } else {
      alert('Preencha todos os campos obrigatórios.');
    }
  }

  cancelar() {
    this.cadastroForm.reset({
      codigo: '00001',
      internacao: true
    });
  }

}
